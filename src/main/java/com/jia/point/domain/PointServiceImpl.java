package com.jia.point.domain;

import static com.jia.point.domain.entity.Point.canUse;
import static com.jia.point.domain.exceptions.ErrorMessage.CANNOT_EXCEED_PRESENT_POINT;
import static com.jia.point.domain.exceptions.ErrorMessage.INVALID_REQUEST;

import com.jia.point.common.annotation.RedissonLock;
import com.jia.point.domain.dtos.PointCommand;
import com.jia.point.domain.dtos.PointHstInfo;
import com.jia.point.domain.entity.Member;
import com.jia.point.domain.entity.Point;
import com.jia.point.domain.entity.PointHst;
import com.jia.point.domain.entity.PointHstRecord;
import com.jia.point.domain.enums.PointUseType;
import com.jia.point.domain.exceptions.PointException;
import com.jia.point.infrastructure.PointHistoryRepository;
import com.jia.point.infrastructure.PointHstRecordRepository;
import com.jia.point.infrastructure.PointRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PointServiceImpl implements PointService {

    private final Integer SELECT_SIZE = 10;

    private final PointRepository pointRepository;

    private final PointHistoryRepository pointHistoryRepository;

    private final PointHstRecordRepository pointHstRecordRepository;

    private final MemberReader memberReader;

    private final RedisService redisService;

    @Override
    @RedissonLock(key = "point")
    public BigDecimal createPoint(PointCommand.Create command) {
        Member member = memberReader.findByMemberId(command.getMemberId());

        // point 적립
        final Point pointSave = command.toPointEntity(member);
        pointRepository.save(pointSave);

        // point_hst 에 적립
        final PointHst pointHstSave = command.toPointHstEntity(member);
        pointHistoryRepository.save(pointHstSave);

        // redis에 저장
        BigDecimal value = redisService.getValue(member.getMemberIdx());
        BigDecimal totalPointValue = value.add(command.getPoint());
        redisService.saveValue(member.getMemberIdx(), totalPointValue);

        return totalPointValue;
    }

    @Override
    @RedissonLock(key = "point")
    public BigDecimal usePoint(PointCommand.Use command) {
        // 사용할 포인트
        BigDecimal toUse = command.getUsePoint();

        // member 확인
        Member member = memberReader.findByMemberId(command.getMemberId());

        // redis 에서 확인
        BigDecimal myPoint = redisService.getValue(command.getMemberId());

        if (!canUse(myPoint, toUse)) {
            throw new PointException(CANNOT_EXCEED_PRESENT_POINT);
        }

        // point 조회
        List<Point> points = pointRepository.findPointsByMemberId(command.getMemberId());

        // point 사용
        Map<Point, BigDecimal> usePoint = spendPoint(toUse, points);

        //point_hst 저장
        final PointHst hstEntity = command.toHstEntity(member);
        PointHst saveHst = pointHistoryRepository.save(hstEntity);

        // point_hst_point에 저장(롤백을 위해)
        for (Point point : usePoint.keySet()) {
            PointHstRecord pointHstRecord = toPointHstRecordEntity(point, saveHst, usePoint.get(point));
            pointHstRecordRepository.save(pointHstRecord);
        }

        //레디스에 새로운 값 저장
        BigDecimal totalPoint = myPoint.subtract(command.getUsePoint());
        redisService.saveValue(member.getMemberIdx(), totalPoint);

        return totalPoint;
    }

    private PointHstRecord toPointHstRecordEntity(Point point, PointHst pointHst, BigDecimal usePoint) {
        return PointHstRecord.builder()
                .point(point)
                .pointHst(pointHst)
                .useValue(usePoint)
                .build();
    }

    private Map<Point, BigDecimal> spendPoint(BigDecimal toUse, List<Point> points) {
        Map<Point, BigDecimal> usePoint = new HashMap<>();

        boolean endWhenZero = true;
        for (Point point : points) {
            if (endWhenZero) {
                if (toUse.compareTo(point.getRemainValue()) == 0) {
                    usePoint.put(point, point.getRemainValue());
                    point.useAllPoint();
                    endWhenZero = false;
                } else if (toUse.compareTo(point.getRemainValue()) < 0) { // 사용할 포인트가 더 적음
                    usePoint.put(point, toUse);
                    point.usePartOfPoint(toUse);
                    endWhenZero = false;
                } else if (toUse.compareTo(point.getRemainValue()) > 0) {
                    usePoint.put(point, point.getRemainValue());
                    toUse = toUse.subtract(point.getRemainValue());
                    point.useAllPoint();
                }
            } else {
                break;
            }
        }
        return usePoint;
    }

    @Override
    public List<PointHstInfo> getPointHistories(String memberIdx, Integer page) {
        Member member = memberReader.findByMemberId(Long.valueOf(memberIdx));

        Pageable pageable = PageRequest.of(page, SELECT_SIZE);
        Page<PointHst> list = pointHistoryRepository.findAllByMember(member, pageable);

        return list.stream().map(PointHstInfo::of).toList();
    }

    @Override
    @Transactional
    public BigDecimal cancelPoint(Long pointHstIdx, Long memberIdx) {
        // 해당하는 record를 가지고 온다. -> list로
        PointHst pointHst = pointHistoryRepository.findById(pointHstIdx)
                .orElseThrow(() -> new IllegalArgumentException(INVALID_REQUEST));

        List<PointHstRecord> records = pointHstRecordRepository.findAllByPointHst(pointHst);

        // 해당 포인트의 만료시간을 확인하고 만료시간이 지났으면 그대로 만료 처리
        LocalDate today = LocalDate.now();
        BigDecimal toAdd = null;

        for (PointHstRecord record : records) {
            if (today.isAfter(record.getPoint().getExpiredDate())) { // 오늘이 이후다.
                // 만료
                record.getPoint().expired();
            }
            // 원복
            record.getPoint().canceled(record.getUseValue());
            toAdd = toAdd.add(record.getUseValue());
        }

        BigDecimal currentPoint = redisService.getValue(memberIdx);
        redisService.saveValue(memberIdx, currentPoint.add(toAdd));

        return currentPoint.add(toAdd);
    }


    @Override
    public List<Long> findPointsAfterToday() {
        List<Point> points = pointRepository.findPointAfterToday(LocalDate.now());
        return points.stream().map(Point::getPointIdx).toList();
    }

    @Override
    @RedissonLock(key = "point")
    public Integer expirePoints(Long pointIdx) {
        Point point = pointRepository.findById(pointIdx)
                .orElseThrow(() -> new IllegalArgumentException(INVALID_REQUEST));

            // point - 만료
            point.expired();

            // pointHst - 만료로 쌓는다.
            PointHst toSave = PointHst.builder()
                    .value(point.getRemainValue())
                    .pointUseType(PointUseType.EXPIRED)
                    .member(point.getMember())
                    .build();
            pointHistoryRepository.save(toSave);

            // redis - 계산해서 담는다.
            BigDecimal value = redisService.getValue(point.getMember().getMemberIdx());
            redisService.saveValue(point.getMember().getMemberIdx(), value.subtract(point.getRemainValue()));

        return 1;
    }
}
