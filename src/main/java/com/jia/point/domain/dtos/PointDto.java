package com.jia.point.domain.dtos;

import com.jia.point.application.create_point.CreatePointRequest;
import com.jia.point.application.use_point.UsePointRequest;
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


    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Use {

        private Long memberId;
        private BigDecimal usePoint;

        public static Use toCommand(UsePointRequest request) {
            return Use.builder()
                    .memberId(request.getMemberId())
                    .usePoint(request.getUsePoint())
                    .build();
        }
    }
}
