package com.jia.point.common.handler;

import com.jia.point.domain.exceptions.MemberException;
import com.jia.point.interfaces.dtos.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class MemberExceptionHandler {

    @ExceptionHandler(MemberException.class)
    public CommonResponse memberExceptionHandler(MemberException e) {
        return CommonResponse.fail(e.getMessage());
    }

}

