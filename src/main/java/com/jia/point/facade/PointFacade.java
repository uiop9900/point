package com.jia.point.facade;

import com.jia.point.domain.dtos.PointDto;
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

    public BigDecimal earnPoint(PointDto.Create command) {
        return pointService.createPoint(command);
    }

    public BigDecimal usePoint(PointDto.Use command) {
        return pointService.usePoint(command);
    }


    public List<PointHstInfo> getPointHistories(Integer page) {
        return pointService.getPointHistories(page);
    }


}
