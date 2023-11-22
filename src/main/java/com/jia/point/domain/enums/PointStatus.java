package com.jia.point.domain.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PointStatus {
    UNUSED("미사용"),
    USING("사용중"),

    COMPLETE("사용완료"),
    EXPIRED("만료");

    private final String description;

}
