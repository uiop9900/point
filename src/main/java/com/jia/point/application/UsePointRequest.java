package com.jia.point.application;

import com.jia.point.domain.PointDto;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class UsePointRequest {

    private Long memberId;
    private BigDecimal usePoint;

}
