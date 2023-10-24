package com.jia.point.domain.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PointType {
    EARN("적립"),
    USE("사용"),

    EXPIRED("만료");

    private final String description;

}
