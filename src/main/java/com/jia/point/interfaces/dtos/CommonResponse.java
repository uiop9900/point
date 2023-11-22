package com.jia.point.interfaces.dtos;

import com.jia.point.interfaces.enums.ResultCode;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommonResponse<T> {

    private ResultCode result; // 성공여부
    private T data; // 성공시 반환하는 객체
    private String message; // 실패시 에러메세지


    public static <T> CommonResponse<T> success(T data) {
        return CommonResponse.<T>builder()
                .result(ResultCode.SUCCESS)
                .data(data)
                .build();
    }

    public static <T> CommonResponse<T> fail(String message) {
        return CommonResponse.<T>builder()
                .result(ResultCode.FAIL)
                .message(message)
                .data(null)
                .build();
    }
}
