package com.jia.point.interfaces.dtos;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class CreatePointRequest {

    private Long memberId;
    private BigDecimal point;

}
