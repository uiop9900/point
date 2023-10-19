package com.jia.point.domain;

import com.jia.point.application.CreatePointRequest;
import com.jia.point.application.create_member.CreateMemberRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
public class PointDto {

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create {

        private Long memberId;
        private BigDecimal point;

        public static Create toCommand(CreatePointRequest request) {
            return Create.builder()
                    .memberId(request.getMemberId())
                    .point(request.getPoint())
                    .build();
        }
    }

}
