package com.jia.point.domain;

import com.jia.point.domain.entity.Member;
import com.jia.point.infrastructure.MemberRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberServiceImplTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;

    @Test
    @Transactional
    void memberCrud() {
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
        Assertions.assertThat(all.size()).isEqualTo(1);
    }

    private void flushAndClear() {
        em.flush();
        em.clear();
    }
}