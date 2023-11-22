package com.jia.point.domain.dtos;

import com.jia.point.domain.entity.PointHst;
import com.jia.point.domain.enums.PointUseType;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PointHstInfo {

    private MemberInfo.Main member;
    private BigDecimal value;
    private PointUseType pointType;

    public static PointHstInfo of (PointHst hst) {
        return PointHstInfo.builder()
                .member(MemberInfo.Main.of(hst.getMember()))
                .value(hst.getValue())
                .pointType(hst.getPointType())
                .build();
    }
}
