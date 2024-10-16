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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PointServiceImpl implements PointService {

    private final PointReader pointReader;

    private final PointStore pointStore;

    private final MemberReader memberReader;

    private final RedisService redisService;

    // TODO 히스토리 저장 방식에 대해 좀 더 생각하기
    // TODO redis 저장 방식에 대해 좀 더 생각하기
    // TODO creatPoint가 맞나? 함수명을 좀 더 생각하기
    // TODO 비즈니스 로직에 hst, redis가 너무 많다.


    @Override
    @RedissonLock(key = "point")
    public BigDecimal createPoint(PointCommand.Create command) {
        Member member = memberReader.findByMemberId(command.getMemberId());

        // point 적립
        final Point pointSave = command.toPointEntity(member);
        pointStore.save(pointSave);

        // point_hst 에 적립
        final PointHst pointHstSave = command.toPointHstEntity(member);
        pointStore.save(pointHstSave);

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
        List<Point> points = pointReader.findPointsByMemberId(command.getMemberId());

        // point 사용
        Map<Point, BigDecimal> usePoint = spendPoint(toUse, points);

        //point_hst 저장
        final PointHst hstEntity = command.toHstEntity(member);
        PointHst saveHst = pointStore.save(hstEntity);

        // point_hst_point에 저장(롤백을 위해)
        for (Point point : usePoint.keySet()) {
            PointHstRecord pointHstRecord = toPointHstRecordEntity(point, saveHst, usePoint.get(point));
            pointStore.save(pointHstRecord);
        }

        //레디스에 새로운 값 저장
        BigDecimal totalPoint = myPoint.subtract(command.getUsePoint());
        redisService.saveValue(member.getMemberIdx(), totalPoint);

        return totalPoint;
    }

    // TODO: 객체변환은 command 안에서 하기
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
        final List<PointHst> list = pointReader.findPointsByMember(member, page);
        return list.stream().map(PointHstInfo::of).toList();
    }

    @Override
    @Transactional
    public BigDecimal cancelPoint(Long pointHstIdx, Long memberIdx) {
        // 해당하는 record를 가지고 온다. -> list로
        PointHst pointHst = pointReader.findPointHstByIdx(pointHstIdx);

        // 취소 금액
        List<PointHstRecord> records = pointReader.findRecordsByPointHst(pointHst);
        BigDecimal canceledValue = records.stream().map(PointHstRecord::getUseValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        // 취소 금액 원복
        BigDecimal currentPoint = redisService.getValue(memberIdx);
        redisService.saveValue(memberIdx, currentPoint.add(canceledValue));

        // 해당 포인트의 만료시간을 확인하고 만료시간이 지났으면 그대로 만료 처리
        LocalDate today = LocalDate.now();

        for (PointHstRecord record : records) {
            if (today.isAfter(record.getPoint().getExpiredDate())) { // 오늘이 이후다.
                // 만료
                record.getPoint().expired();
                this.expirePoints(record.getPoint().getPointIdx());
            }

            // 취소
            record.getPoint().canceled(record.getUseValue());
            PointHst expired = PointCommand.Expired.of(record.getPoint()).toHstEntity();
            pointStore.save(expired);
        }

        return redisService.getValue(memberIdx);
    }


    @Override
    public List<Long> findPointsAfterToday() {
        List<Point> points = pointReader.findPointAfterToday(LocalDate.now());
        return points.stream().map(Point::getPointIdx).toList();
    }

    @Override
    @RedissonLock(key = "point")
    public Integer expirePoints(Long pointIdx) {
        Point point = pointReader.findPointByIdx(pointIdx);

            // point - 만료
            point.expired();

            // pointHst - 만료로 쌓는다.
            PointCommand.Expired expired = PointCommand.Expired.of(point);
            PointHst toSave = expired.toHstEntity();
            pointStore.save(toSave);

            // redis - 계산해서 담는다.
            BigDecimal value = redisService.getValue(point.getMember().getMemberIdx());
            redisService.saveValue(point.getMember().getMemberIdx(), value.subtract(point.getRemainValue()));

        return 1;
    }
}
