package com.jia.point.common.handler;

import com.jia.point.interfaces.dtos.CommonResponse;
import com.jia.point.interfaces.dtos.ServerErrorResponse;
import com.jia.point.interfaces.enums.ResultCode;
import jodd.net.HttpStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.jia.point.domain.exceptions.ErrorMessage.BACKEND_ERROR;

@Slf4j
@ControllerAdvice
public class PointExceptionHandler {

    // IllegalArgumentException 터뜨리면 여기서 잡아서 준다.
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ServerErrorResponse> handlerBackEndException() {
        return new ResponseEntity<ServerErrorResponse>(ServerErrorResponse.response(), HttpStatusCode.valueOf(500));
    }
}

