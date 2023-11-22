package com.jia.point.domain.entity;

import com.jia.point.common.converter.LocalDateConverter;
import com.jia.point.domain.enums.PointStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


/**
 * 포인트<br>
 * : 포인트가 쌓이면 생성된다.
 */
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name = "POINT")
@DynamicUpdate
public class Point {

    @Id
    @Column(name = "point_idx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pointIdx;

    @Enumerated(EnumType.STRING)
    private PointStatus useStatus; // 포인트 상태

    private BigDecimal originValue; // 포인트 적립금
    private BigDecimal remainValue; // 남은 금액(사용가능 금액)

    @Convert(converter = LocalDateConverter.class)
    private LocalDate expiredDate; // 만료일자

    private LocalDateTime regDt;
    private LocalDateTime updDt;

    //============= 연관관계 ============
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST) // cascade: 영속성 전이 : 특정 엔티티를 영속화 할때 같이 영속화된다.
    @JoinColumn(name = "member_idx") // point 저장할때 member도 같이 영속성에 들어가서 저장된다.
    private Member member;

    public void usePartOfPoint(BigDecimal useValue) {
        this.useStatus = PointStatus.USING;
        this.remainValue = this.remainValue.subtract(useValue);
        this.updDt = LocalDateTime.now();
    }

    public void useAllPoint() {
        this.useStatus = PointStatus.COMPLETE;
        this.remainValue = BigDecimal.ZERO;
        this.updDt = LocalDateTime.now();
    }

    public void expired() {
        this.useStatus = PointStatus.EXPIRED;
        this.updDt = LocalDateTime.now();
    }
}
