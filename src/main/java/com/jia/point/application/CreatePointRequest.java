package com.jia.point.application;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class CreatePointRequest {

    private Long memberId;
    private BigDecimal point;

}
