package com.jia.point.domain.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MemberException extends RuntimeException{

    public MemberException(String message) {
        super(message);
    }

    public MemberException(String message, Long memberIdx) {
        super(message + "memberIdx =" + memberIdx.toString());
    }
}
