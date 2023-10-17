package com.jia.point.domain;

import com.jia.point.application.dto.CreateMemberRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class MemberDto {

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create {

        private String name;
        private String phoneNumber;

        public static Create toCommand(CreateMemberRequest request) {
            return Create.builder()
                    .name(request.getName())
                    .phoneNumber(request.getPhoneNumber())
                    .build();
        }
    }

}
