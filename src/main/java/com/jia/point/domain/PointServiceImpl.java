package com.jia.point.domain;

import com.jia.point.domain.entity.Member;
import com.jia.point.domain.entity.Point;
import com.jia.point.domain.entity.PointHst;
import com.jia.point.domain.enums.PointType;
import com.jia.point.domain.enums.UseStatus;
import com.jia.point.infrastructure.PointHistoryRepository;
import com.jia.point.infrastructure.PointRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PointServiceImpl implements PointService {

    private final PointRepository pointRepository;

    private final PointHistoryRepository pointHistoryRepository;

    private final MemberReader memberReader;

    private final RedisService redisService;

    @Transactional
    @Override
    public BigDecimal createPoint(PointDto.Create command) {
        Member member = memberReader.findByMemberId(command.getMemberId());

        // point 적립
        Point point = Point.entityBuilder()
                .member(member)
                .originValue(command.getPoint())
                .remainValue(command.getPoint())
                .useStatus(UseStatus.UNUSED)
                .regDt(LocalDateTime.now())
                .build();
        pointRepository.save(point);

        // point_hst 에 적립
        PointHst pointHst = PointHst.entityBuilder()
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

        Boolean endWhenZero = true;
        for (Point point : points) {
            if (endWhenZero) {
                if (toUse.compareTo(point.getRemainValue()) == 0) {
                    point.useAllPoint();
                    endWhenZero = false;
                } else if (toUse.compareTo(point.getRemainValue()) < 0) { // 사용할 포인트가 더 적음
                    point.usingPoint(toUse);
                    endWhenZero = false;
                } else if (toUse.compareTo(point.getRemainValue()) > 0) {
                    toUse = toUse.subtract(point.getRemainValue());
                    point.useAllPoint();
                }
            } else {
                break;
            }

        }

        //point_hst 저장
        PointHst pointHst = PointHst.entityBuilder()
                .member(member)
                .value(command.getUsePoint())
                .pointType(PointType.USE)
                .regDt(LocalDateTime.now())
                .build();
        pointHistoryRepository.save(pointHst);

        //레디스에 새로운 값 저장
        BigDecimal totalPoint = canUsePoint.subtract(command.getUsePoint());
        redisService.saveValue(member.getMemberIdx(), totalPoint);

        return totalPoint;
    }

}
