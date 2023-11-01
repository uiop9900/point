package com.jia.point.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;

/**
 * point와 point_hst의 중간 테이블
 */
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name = "POINT_HST_POINT")
@DynamicUpdate
public class PointHstPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_idx")
    private Point point;

    @Column(name = "use_vale")
    private BigDecimal useValue; // 당시 사용한 point의 remainValue

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_hst_idx")
    private PointHst pointHst;

}