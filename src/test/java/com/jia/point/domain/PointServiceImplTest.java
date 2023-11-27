package com.jia.point.domain;

import com.jia.point.domain.dtos.MemberCommand;
import com.jia.point.domain.dtos.PointCommand;
import com.jia.point.domain.dtos.PointHstInfo;
import com.jia.point.domain.entity.Member;
import com.jia.point.domain.entity.Point;
import com.jia.point.domain.entity.PointHst;
import com.jia.point.domain.enums.PointStatus;
import com.jia.point.domain.enums.PointUseType;
import com.jia.point.infrastructure.MemberRepository;
import com.jia.point.infrastructure.PointHistoryRepository;
import com.jia.point.infrastructure.PointRepository;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Slf4j
@Transactional
class PointServiceImplTest {

    @Autowired PointService pointService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PointRepository pointRepository;

    @Autowired
    PointHistoryRepository pointHistoryRepository;

    @Autowired RedisService redisService;

    @Autowired
    EntityManager em;

//    @BeforeEach
    void insertInitData() {
        Member toSave = Member.builder()
                .name("이지아")
                .phoneNumber("01099735424")
                .regDt(LocalDateTime.now())
                .build();
        Member save = memberRepository.save(toSave);

        for (int i = 0; i < 100; i++) {
            PointHst pointHst = PointHst.builder()
                    .member(save)
                    .pointUseType(PointUseType.EARN)
                    .value(BigDecimal.ONE)
                    .build();
            pointHistoryRepository.save(pointHst);
        }

    }

    @Test
    @DisplayName("포인트적립_success")
    void create_point_레디스에_포인트가_저장된다() {
        // given
        Long memberId = 1L;
        BigDecimal point = BigDecimal.valueOf(500);
        PointCommand.Create toSave = PointCommand.Create.builder()
                .memberId(memberId)
                .point(point)
                .build();

        // when
        BigDecimal total = pointService.createPoint(toSave);
        flushAndClear();

        // then
        assertThat(redisService.getValue(memberId)).isEqualTo(total);
    }

    @Test
    @DisplayName("포인트사용_success")
    void use_point_레디스에_사용한만큼_포인트가_차감된다() {
        // given
        Long memberId = 1L;
        BigDecimal point = BigDecimal.valueOf(300);
        PointCommand.Use toUpdate = PointCommand.Use.builder()
                .memberId(memberId)
                .usePoint(point)
                .build();

        // when
        BigDecimal total = pointService.usePoint(toUpdate);
        flushAndClear();

        // then
        assertThat(redisService.getValue(memberId)).isEqualTo(total);
    }

    @Test
    @DisplayName("포인트사용_exception")
    void use_point_레디스의_포인트보다_많이_사용시_예외발생() {
        // given
        Long memberId = 1L;
        BigDecimal canUse = redisService.getValue(memberId);
        BigDecimal tryUse = canUse.add(BigDecimal.valueOf(1000));

        PointCommand.Use toUpdate = PointCommand.Use.builder()
                .memberId(memberId)
                .usePoint(tryUse)
                .build();

        // then
        Assertions.assertThrows(
                IllegalArgumentException.class, () -> {
                    pointService.usePoint(toUpdate);
                });
    }


    @Test
    @DisplayName("포인트조회_success")
    void get_point_histories_페이징되어서_조회된다() {
        // given
        String memberIdx = "1";
        Integer page = 1;

        // when
        List<PointHstInfo> list = pointService.getPointHistories(memberIdx, page);

        // then
        assertThat(list.size()).isEqualTo(10);
    }

    @Test
    @DisplayName("포인트만료_success")
    void expire_points_일정시간에_포인트들이_만료된다() {
        // given : 포인트들이 저장된 상태

        Optional<Member> member = memberRepository.findByMemberIdx(1L);
        for (int i = 0; i < 10; i++) {
            Point point = Point.builder()
                    .originValue(BigDecimal.ONE)
                    .remainValue(BigDecimal.ONE)
                    .expiredDate(i % 2 == 0 ? LocalDate.now() : LocalDate.now().plusYears(1))
                    .useStatus(PointStatus.UNUSED)
                    .regDt(LocalDateTime.now().minusYears(1))
                    .member(
                            member.get()
                    )
                    .build();

            Point saved = pointRepository.save(point);
            log.error("==============={}", saved.getExpiredDate());
        }

        // when
        Integer expirePoints = pointService.expirePoints();

        // then
        assertThat(expirePoints).isEqualTo(5);
    }

    @Test
    @DisplayName("적립과 사용이 동시에")
    @Transactional
    void point_적립과_사용을_동시에_한다() throws InterruptedException {
        // given
        BigDecimal point = new BigDecimal(100);
        BigDecimal usePoint = new BigDecimal(50);

        Member member = memberRepository.save(Member.toEntity(MemberCommand.Create
                .builder()
                .name("user")
                .phoneNumber("01012341234")
                .build()));

        ExecutorService executorService = Executors.newFixedThreadPool(2); // 스레드 풀 생성

        // earnPoint 메소드를 호출하는 스레드
        executorService.submit(() -> {
            pointService.createPoint(PointCommand.Create.builder()
                            .memberId(member.getMemberIdx())
                            .point(point)
                    .build());
        });

        // usePoint 메소드를 호출하는 스레드
        executorService.submit(() -> {
            pointService.usePoint(PointCommand.Use.builder()
                            .memberId(member.getMemberIdx())
                            .usePoint(usePoint)
                    .build());
        });


        executorService.shutdown(); // 스레드 풀 종료
        executorService.awaitTermination(5, TimeUnit.SECONDS); // 최대 5초까지 대기

        Page<PointHst> pointHsts = pointHistoryRepository.findAllByMember(member, PageRequest.of(0, 10));
        org.assertj.core.api.Assertions.assertThat(pointHsts.stream().toList().size()).isEqualTo(3);



    }

    private void flushAndClear() {
        em.flush();
        em.clear();
    }
}