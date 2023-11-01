package com.jia.point.domain.entity;

import com.jia.point.domain.enums.PointType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name = "POINT_HISTORY")
@DynamicUpdate
public class PointHst {

    @Id
    @Column(name = "point_hst_idx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pointHstIdx;

    private BigDecimal value; // 금액

    @Enumerated(EnumType.STRING)
    private PointType pointType; // 적립 or 사용

    //============= 연관관계 ============
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_idx")
    private Member member;

    private LocalDateTime regDt;
    private LocalDateTime updDt;
}
