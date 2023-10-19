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
                .build();
        pointRepository.save(point);

        // point_hst 에 적립
        PointHst pointHst = PointHst.entityBuilder()
                .member(member)
                .value(command.getPoint())
                .pointType(PointType.EARN)
                .build();
        pointHistoryRepository.save(pointHst);

        // redis에 저장
        BigDecimal value = redisService.getValue(member.getMemberIdx());
        BigDecimal totalPointValue = value.add(command.getPoint());
        redisService.saveValue(member.getMemberIdx(), totalPointValue);

        return totalPointValue;
    }

}
