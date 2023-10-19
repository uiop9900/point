package com.jia.point.infrastructure;

import com.jia.point.domain.MemberReader;
import com.jia.point.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class MemberReaderImpl implements MemberReader {

    private final MemberRepository memberRepository;
    @Override
    public Member findByMemberId(Long memberId) {
        return memberRepository.findByMemberIdx(memberId).orElseThrow();
    }
}
