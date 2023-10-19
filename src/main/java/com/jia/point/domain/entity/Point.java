package com.jia.point.domain.entity;

import com.jia.point.domain.enums.UseStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder(builderMethodName = "entityBuilder", toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name = "POINT")
public class Point {

    @Id
    @Column(name = "point_idx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pointIdx;

    @Enumerated(EnumType.STRING)
    private UseStatus useStatus; // 사용여부

    private BigDecimal originValue; // 포인트 적립금
    private BigDecimal remainValue; // 남은 금액(사용가능 금액)

    private LocalDateTime regDt;
    private LocalDateTime updDt;

    //============= 연관관계 ============
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_idx")
    private Member member;

}
