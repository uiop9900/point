package com.jia.point.domain;

import com.jia.point.common.annotation.RedissonLock;
import com.jia.point.domain.dtos.PointDto;
import com.jia.point.domain.entity.Member;
import com.jia.point.domain.entity.Point;
import com.jia.point.domain.entity.PointHst;
import com.jia.point.domain.entity.PointHstPoint;
import com.jia.point.domain.enums.PointType;
import com.jia.point.domain.enums.PointStatus;
import com.jia.point.domain.dtos.PointHstInfo;
import com.jia.point.infrastructure.PointHistoryPointRepository;
import com.jia.point.infrastructure.PointHistoryRepository;
import com.jia.point.infrastructure.PointRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PointServiceImpl implements PointService {

    private final Integer SELECT_SIZE = 10;

    private final PointRepository pointRepository;

    private final PointHistoryRepository pointHistoryRepository;

    private final PointHistoryPointRepository pointHistoryPointRepository;

    private final MemberReader memberReader;

    private final RedisService redisService;

    @Transactional
    @Override
    @RedissonLock(key = "point")
    public BigDecimal createPoint(PointDto.Create command) {
        Member member = memberReader.findByMemberId(command.getMemberId());

        // point 적립
        Point point = Point.builder()
                .member(member)
                .originValue(command.getPoint())
                .remainValue(command.getPoint())
                .expiredDate(LocalDate.now().plusYears(1))
                .useStatus(PointStatus.UNUSED)
                .regDt(LocalDateTime.now())
                .build();
        pointRepository.save(point);

        // point_hst 에 적립
        PointHst pointHst = PointHst.builder()
                .member(member)
                .value(command.getPoint())
                .pointType(PointType.EARN)
                .regDt(LocalDateTime.now())
                .build();
        pointHistoryRepository.save(pointHst);

        // redis에 저장
        BigDecimal value = redisService.getValue(member.getMemberIdx());
        BigDecimal totalPointValue = value.add(command.getPoint());
        redisService.saveValue(member.getMemberIdx(), totalPointValue);

        return totalPointValue;
    }

    @Override
    @Transactional
    @RedissonLock(key = "point")
    public BigDecimal usePoint(PointDto.Use command) {
        // member 확인
        Member member = memberReader.findByMemberId(command.getMemberId());

        // redis 에서 확인
        BigDecimal canUsePoint = redisService.getValue(command.getMemberId());

        BigDecimal toUse = command.getUsePoint(); // 사용할 포인트
        if (toUse.compareTo(canUsePoint) == 1) { // 사용불가
         throw new IllegalArgumentException("It is exceed saved point");
        }

        //point에서 오래된 point 꺼내서 상태변경
        List<Point> points = pointRepository.findPointsByMemberId(command.getMemberId());

        Map<Point, BigDecimal> usePoint = new HashMap<>();
        Boolean endWhenZero = true;
        for (Point point : points) {
            if (endWhenZero) {
                if (toUse.compareTo(point.getRemainValue()) == 0) {
                    point.useAllPoint();
                    endWhenZero = false;
                    usePoint.put(point, point.getRemainValue());
                } else if (toUse.compareTo(point.getRemainValue()) < 0) { // 사용할 포인트가 더 적음
                    point.usePartOfPoint(toUse);
                    endWhenZero = false;
                    usePoint.put(point, toUse);
                } else if (toUse.compareTo(point.getRemainValue()) > 0) {
                    toUse = toUse.subtract(point.getRemainValue());
                    point.useAllPoint();
                    usePoint.put(point, point.getRemainValue());
                }
            } else {
                break;
            }
        }

        //point_hst 저장
        PointHst pointHst = PointHst.builder()
                .member(member)
                .value(command.getUsePoint())
                .pointType(PointType.USE)
                .regDt(LocalDateTime.now())
                .build();

        // point_hst_point에 저장
        PointHst saveHst = pointHistoryRepository.save(pointHst);
        for (Point point : usePoint.keySet()) {
            PointHstPoint pointHstPoint = PointHstPoint.builder()
                    .point(point)
                    .useValue(usePoint.get(point))
                    .pointHst(saveHst)
                    .build();
            pointHistoryPointRepository.save(pointHstPoint);
        }

        //레디스에 새로운 값 저장
        BigDecimal totalPoint = canUsePoint.subtract(command.getUsePoint());
        redisService.saveValue(member.getMemberIdx(), totalPoint);

        return totalPoint;
    }

    @Override
    public List<PointHstInfo> getPointHistories(Integer page) {
        Pageable pageable = PageRequest.of(page, SELECT_SIZE);
        Page<PointHst> list = pointHistoryRepository.findAll(pageable);
        return list.stream().map(PointHstInfo::of).toList();
    }

    @Override
    @Transactional
//    @RedissonLock(key = "point")
    public Integer expirePoints() {
        List<Point> pointAfterToday = pointRepository.findPointAfterToday(LocalDate.now());

        for (Point point : pointAfterToday) {
            // point - 만료
            point.expired();
            // pointHst - 만료로 쌓는다.
            PointHst toSave = PointHst.builder()
                    .value(point.getRemainValue())
                    .pointType(PointType.EXPIRED)
                    .member(point.getMember())
                    .build();

            pointHistoryRepository.save(toSave);

            // redis - 계산해서 담는다.
            BigDecimal value = redisService.getValue(point.getMember().getMemberIdx());
            redisService.saveValue(point.getMember().getMemberIdx(), value.subtract(point.getRemainValue()));
        }
        return pointAfterToday.size();
    }
}
