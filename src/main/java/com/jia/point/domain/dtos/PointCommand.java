package com.jia.point.domain.dtos;

import com.jia.point.domain.entity.Member;
import com.jia.point.domain.entity.Point;
import com.jia.point.domain.entity.PointHst;
import com.jia.point.domain.enums.PointStatus;
import com.jia.point.domain.enums.PointUseType;
import com.jia.point.interfaces.dtos.CreatePointRequest;
import com.jia.point.interfaces.dtos.UsePointRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
public class PointCommand {

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create {

        private Long memberId;
        private BigDecimal point;

        public static Create toCommand(CreatePointRequest request) {
            return Create.builder()
                    .memberId(Long.valueOf(request.getMemberIdx()))
                    .point(request.getPoint())
                    .build();
        }

        // point 적립
        public Point toPointEntity(Member member) {
            return Point.builder()
                .member(member)
                .originValue(point)
                .remainValue(point)
                .expiredDate(LocalDate.now().plusYears(1))
                .useStatus(PointStatus.UNUSED)
                .regDt(LocalDateTime.now())
                .build();
        }

        // pointHst 적립
        public PointHst toPointHstEntity(Member member) {
            return PointHst.builder()
                .member(member)
                .value(point)
                .pointUseType(PointUseType.EARN)
                .regDt(LocalDateTime.now())
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

        public PointHst toHstEntity(Member member) {
            return PointHst.builder()
                .member(member)
                .value(usePoint)
                .pointUseType(PointUseType.USE)
                .regDt(LocalDateTime.now())
                .build();
        }
    }


    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor(staticName = "of")
    public static class Expired {

        private Point point;

        public PointHst toHstEntity() {
            Member member = point.getMember();
            return PointHst.builder()
                    .member(member)
                    .value(point.getRemainValue())
                    .pointUseType(PointUseType.EXPIRED)
                    .regDt(LocalDateTime.now())
                    .build();
        }
    }
}
