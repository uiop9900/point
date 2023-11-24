package com.jia.point.infrastructure;

import static com.jia.point.domain.exceptions.ErrorMessage.NOT_FOUND_MEMBER;

import com.jia.point.domain.MemberReader;
import com.jia.point.domain.entity.Member;
import com.jia.point.domain.exceptions.MemberException;
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
        return memberRepository.findByMemberIdx(memberId)
            .orElseThrow(() -> new MemberException(NOT_FOUND_MEMBER));
    }
}
