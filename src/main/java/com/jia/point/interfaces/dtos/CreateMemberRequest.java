package com.jia.point.interfaces.dtos;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateMemberRequest {

    private String name; // 이름
    private String phoneNumber; // 전화번호

}
