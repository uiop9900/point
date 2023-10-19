package com.jia.point.facade;

import com.jia.point.domain.PointDto;
import com.jia.point.domain.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class PointFacade {

    private final PointService pointService;


    public BigDecimal earnPoint(PointDto.Create command) {
        return pointService.createPoint(command);
    }





}
