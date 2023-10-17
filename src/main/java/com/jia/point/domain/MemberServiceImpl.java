package com.jia.point.domain;

import com.jia.point.domain.entity.Member;
import com.jia.point.infrastructure.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    @Override
    public void insertMember(MemberDto.Create command) {
        Member toSave = Member.toEntity(command);
        memberRepository.save(toSave);
    }
}
