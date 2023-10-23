package com.jia.point.domain;

import com.jia.point.domain.dtos.MemberDto;
import com.jia.point.domain.entity.Member;
import com.jia.point.infrastructure.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void insertMember(MemberDto.Create command) {
        Member toSave = Member.toEntity(command);
        Member save = memberRepository.save(toSave);
        log.error("memberIdx = {}", save.getMemberIdx().toString());
    }


}
