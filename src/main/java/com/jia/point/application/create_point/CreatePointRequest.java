package com.jia.point.application.create_point;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class CreatePointRequest {

    private Long memberId;
    private BigDecimal point;

}
