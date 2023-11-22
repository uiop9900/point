package com.jia.point.application.dtos;

import com.jia.point.application.enums.ResultCode;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommonResponse<T> {

    private ResultCode result;
    private T data;

    public static <T> CommonResponse<T> success(T data) {
        return CommonResponse.<T>builder()
                .result(ResultCode.SUCCESS)
                .data(data)
                .build();
    }

    public static <T> CommonResponse<T> fail(T data) {
        return CommonResponse.<T>builder()
                .result(ResultCode.FAIL)
                .data(data)
                .build();
    }
}
