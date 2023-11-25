package com.jia.point.interfaces.dtos;

import com.jia.point.domain.exceptions.ErrorMessage;
import com.jia.point.interfaces.enums.ResultCode;
import jodd.net.HttpStatus;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Getter
@Builder
public class ServerErrorResponse {

    private String errorCode;
    private String message; // 실패시 에러메세지


    public static ServerErrorResponse response() {
        return ServerErrorResponse.builder()
                .errorCode(INTERNAL_SERVER_ERROR.toString())
                .message(ErrorMessage.BACKEND_ERROR)
                .build();
    }

}
