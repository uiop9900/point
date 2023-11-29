package com.jia.point.common.handler;

import com.jia.point.domain.exceptions.PointException;
import com.jia.point.interfaces.dtos.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class PointExceptionHandler {

    @ExceptionHandler(PointException.class)
    public CommonResponse pointExceptionHandler(PointException e) {
        return CommonResponse.fail(e.getMessage());
    }

}

