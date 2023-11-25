package com.jia.point.common.annotation;

import com.jia.point.domain.MemberReader;
import com.jia.point.domain.MemberService;
import com.jia.point.domain.dtos.MemberCommand;
import com.jia.point.domain.entity.Member;
import com.jia.point.infrastructure.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
@Slf4j
class RedissonLockAopTest {

    @Autowired
    MemberService memberService;

    @Autowired MemberReader memberReader;

    @Autowired
    MemberRepository memberRepository;
    @Test
    void 동시성_멤버_저장() throws InterruptedException {
        String phoneNumber = "12341234";

        int numberOfThreads = 10;

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            MemberCommand.Create toCreate = MemberCommand.Create.builder()
                    .name("이지아")
                    .phoneNumber(phoneNumber)
                    .build();

            executorService.submit(() -> {
                try {
                    // 분산락 적용 메서드 호출
                    memberService.insertMember(toCreate);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        List<Member> members = memberRepository.findAll();


        Assertions.assertThat(members.size()).isOne();

    }





}