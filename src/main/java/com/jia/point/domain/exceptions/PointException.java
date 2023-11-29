package com.jia.point.domain.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PointException extends RuntimeException {

    public PointException(String message) {
        super(message);
    }
}
