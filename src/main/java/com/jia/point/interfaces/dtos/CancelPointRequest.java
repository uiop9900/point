package com.jia.point.interfaces.dtos;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Builder
@Data
public class CancelPointRequest {

    @NonNull
    private Long pointHstIdx;

    @NonNull
    private Long memberIdx;

}
