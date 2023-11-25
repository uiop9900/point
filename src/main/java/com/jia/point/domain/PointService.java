package com.jia.point.domain;

import com.jia.point.domain.dtos.PointCommand;
import com.jia.point.domain.dtos.PointHstInfo;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

public interface PointService {
    @Transactional
    BigDecimal createPoint(PointCommand.Create command);

    @Transactional
    BigDecimal usePoint(PointCommand.Use command);
    List<PointHstInfo> getPointHistories(String memberIdx, Integer page);

    Integer expirePoints();
}
