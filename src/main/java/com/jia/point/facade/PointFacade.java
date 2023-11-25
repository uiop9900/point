package com.jia.point.facade;

import com.jia.point.domain.dtos.PointCommand;
import com.jia.point.domain.PointService;
import com.jia.point.domain.dtos.PointHstInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PointFacade {

    private final PointService pointService;

    public BigDecimal earnPoint(PointCommand.Create command) {
        return pointService.createPoint(command);
    }

    public BigDecimal usePoint(PointCommand.Use command) {
        return pointService.usePoint(command);
    }


    public List<PointHstInfo> getPointHistories(String memberIdx, Integer page) {
        return pointService.getPointHistories(memberIdx, page);
    }

    public Integer expirePoints() {
        return pointService.expirePoints();
    }

}
