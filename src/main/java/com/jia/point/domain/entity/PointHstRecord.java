package com.jia.point.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;

/**
 * point와 point_hst의 중간 테이블<br>
 * 포인트 사용 취소를 위해 생성함.
 */
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name = "POINT_HST_POINT_RECORD")
@DynamicUpdate
public class PointHstRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(name = "use_vale")
    private BigDecimal useValue; // 당시 사용한 point의 remainValue

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_idx")
    private Point point;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_hst_idx")
    private PointHst pointHst;

}
