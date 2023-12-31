package com.jia.point.domain.dtos;

import com.jia.point.interfaces.dtos.CreateMemberRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class MemberCommand {

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
