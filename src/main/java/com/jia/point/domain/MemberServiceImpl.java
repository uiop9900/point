package com.jia.point.domain;

import com.jia.point.common.annotation.RedissonLock;
import com.jia.point.domain.dtos.MemberDto;
import com.jia.point.domain.entity.Member;
import com.jia.point.infrastructure.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.procedure.ParameterMisuseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    @RedissonLock(key = "member")
    public void insertMember(MemberDto.Create command) {
        Optional<Member> member = memberRepository.findMemberByPhoneNumber(command.getPhoneNumber());

        if (member.isPresent()) {
            log.error("이미 등록된 회원입니다.");
            throw new ParameterMisuseException("이미 등록된 회원입니다.");
        }

        Member toSave = Member.toEntity(command);
        Member save = memberRepository.save(toSave);
        log.error("memberIdx = {}", save.getMemberIdx().toString());
    }


}
