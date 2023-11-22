package com.jia.point.domain.entity;

import com.jia.point.domain.enums.PointUseType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 포인트 히스토리<br>
 * : 포인트의 변경사항 모두 쌓인다.<br>
 * 예시) 사용, 적립, 만료, 취소
 */
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
    private PointUseType pointType; // 적립 or 사용

    //============= 연관관계 ============
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_idx")
    private Member member;

    private LocalDateTime regDt;
    private LocalDateTime updDt;
}
