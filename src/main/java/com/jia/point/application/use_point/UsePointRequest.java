package com.jia.point.application.use_point;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class UsePointRequest {

    private Long memberId;
    private BigDecimal usePoint;

}
