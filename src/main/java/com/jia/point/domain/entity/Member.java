package com.jia.point.domain.entity;

import com.jia.point.domain.dtos.MemberCommand;
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


    // TODO command에서 변환 진행, 엔티티에서는 하지 않는다.
    public static Member toEntity(MemberCommand.Create command) {
        return Member.builder()
                .name(command.getName())
                .phoneNumber(command.getPhoneNumber())
                .regDt(LocalDateTime.now())
                .build();
    }
}
