package com.jia.point.domain;

import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

public interface PointService {
    @Transactional
    BigDecimal createPoint(PointDto.Create command);
}
