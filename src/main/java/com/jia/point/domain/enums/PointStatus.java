package com.jia.point.domain.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PointStatus {
    UNUSED("미사용", true),
    USING("사용중", true),
    CANCEL("취소", true),
    COMPLETE("사용완료", false),

    EXPIRED("만료", false);

    private final String description;

    private final Boolean canUse;

}
