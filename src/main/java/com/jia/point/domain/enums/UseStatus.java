package com.jia.point.domain.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UseStatus {
    UNUSED("미사용"),
    USING("사용중"),
    EXPIRED("만료");

    private final String description;

}
