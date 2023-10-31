package com.jia.point.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * point와 point_hst의 중간 테이블
 */
@Entity
@Getter
@Builder(builderMethodName = "entityBuilder", toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name = "POINT_HST_POINT")
public class PointHstPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_idx")
    private Point point;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_hst_idx")
    private PointHst pointHst;

}
