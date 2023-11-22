package com.jia.point.domain.exceptions;

import lombok.Getter;

@Getter
public class ErrorMessage {
    // member
    public static final String NOT_FOUND_MEMBER = "해당 사용자를 찾을 수 없습니다.";

    public static final String ALREADY_SIGN_UP = "이미 가입한 사용자입니다.";
}
