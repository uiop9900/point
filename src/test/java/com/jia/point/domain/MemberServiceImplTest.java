package com.jia.point.domain;

import com.jia.point.domain.dtos.MemberDto;
import com.jia.point.domain.entity.Member;
import com.jia.point.infrastructure.MemberRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
class MemberServiceImplTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;

    @Test
    void member_crud_success() {
        //given
        MemberDto.Create create = MemberDto.Create.builder()
                .name("이지아")
                .phoneNumber("01099735424")
                .build();

        // when
        memberService.insertMember(create);
        flushAndClear();

        // then
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(1);
        assertThat(all.get(0).getName()).isEqualTo("이지아");
        assertThat(all.get(0).getPhoneNumber()).isEqualTo("01099735424");
    }

    private void flushAndClear() {
        em.flush();
        em.clear();
    }
}