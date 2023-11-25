package com.jia.point.interfaces.dtos;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;

@Builder
@Data
public class UsePointRequest {

    @NonNull
    private Long memberId;
    private BigDecimal usePoint;

}
