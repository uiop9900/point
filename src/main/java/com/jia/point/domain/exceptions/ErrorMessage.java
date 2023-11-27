package com.jia.point.domain.exceptions;

import lombok.Getter;

@Getter
public class ErrorMessage {
    // member
    public static final String NOT_FOUND_MEMBER = "해당 사용자를 찾을 수 없습니다.";

    public static final String ALREADY_SIGN_UP = "이미 가입한 사용자입니다.";

    public static final String BACKEND_ERROR = "서버에서 에러가 발생했습니다.";

    // 내부용 메세지
    public static final String INVALID_REQUEST = "요청값을 확인하세요.";
}
