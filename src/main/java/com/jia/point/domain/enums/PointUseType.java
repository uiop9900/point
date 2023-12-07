package com.jia.point.domain.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PointUseType {
    EARN("적립"),
    USE("사용"),
    EXPIRED("만료"),

    CANCEL("취소")
    ;

    private final String description;

}
