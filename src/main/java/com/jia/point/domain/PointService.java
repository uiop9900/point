package com.jia.point.domain;

import com.jia.point.domain.dtos.PointDto;
import com.jia.point.domain.dtos.PointHstInfo;
import com.jia.point.domain.entity.PointHst;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

public interface PointService {
    @Transactional
    BigDecimal createPoint(PointDto.Create command);

    @Transactional
    BigDecimal usePoint(PointDto.Use command);

    List<PointHstInfo> getPointHistories(Integer page);
}
