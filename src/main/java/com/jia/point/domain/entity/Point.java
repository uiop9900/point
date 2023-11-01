package com.jia.point.domain.entity;

import com.jia.point.common.converter.LocalDateConverter;
import com.jia.point.domain.enums.UseStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private UseStatus useStatus; // 사용여부

    private BigDecimal originValue; // 포인트 적립금
    private BigDecimal remainValue; // 남은 금액(사용가능 금액)

    @Convert(converter = LocalDateConverter.class)
    private LocalDate expiredDate; // 만료일자

    private LocalDateTime regDt;
    private LocalDateTime updDt;

    //============= 연관관계 ============
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_idx")
    private Member member;

    public void usingPoint(BigDecimal useValue) {
        this.useStatus = UseStatus.USING;
        this.remainValue = this.remainValue.subtract(useValue);
        this.updDt = LocalDateTime.now();
    }

    public void useAllPoint() {
        this.useStatus = UseStatus.COMPLETE;
        this.remainValue = BigDecimal.ZERO;
        this.updDt = LocalDateTime.now();
    }

    public void expired() {
        this.useStatus = UseStatus.EXPIRED;
        this.updDt = LocalDateTime.now();
    }
}
