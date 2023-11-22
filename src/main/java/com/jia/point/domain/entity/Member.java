package com.jia.point.domain.entity;

import com.jia.point.domain.dtos.MemberDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 사용자
 */
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name = "MEMBER")
@DynamicUpdate
public class Member {

    @Id
    @Column(name = "member_idx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberIdx;

    private String name; // 이름
    private String phoneNumber; // 핸드폰 번호

    private LocalDateTime regDt;
    private LocalDateTime updDt;

    //============= 연관관계 ============
    @OneToMany(mappedBy = "member")
    private List<Point> points;

    @OneToMany(mappedBy = "member")
    private List<PointHst> pointHsts;


    public static Member toEntity(MemberDto .Create command) {
        return Member.builder()
                .name(command.getName())
                .phoneNumber(command.getPhoneNumber())
                .regDt(LocalDateTime.now())
                .build();
    }
}
