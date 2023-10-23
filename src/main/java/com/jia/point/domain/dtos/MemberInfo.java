package com.jia.point.domain.dtos;

import com.jia.point.domain.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class MemberInfo {

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Main {

        private String name;
        private String phoneNumber;

        public static Main of(Member member) {
            return Main.builder()
                    .name(member.getName())
                    .phoneNumber(member.getPhoneNumber())
                    .build();
        }
    }

}
