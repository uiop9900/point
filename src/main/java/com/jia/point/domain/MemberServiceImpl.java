package com.jia.point.domain;

import com.jia.point.common.annotation.RedissonLock;
import com.jia.point.domain.dtos.MemberDto;
import com.jia.point.domain.entity.Member;
import com.jia.point.domain.exceptions.MemberException;
import com.jia.point.infrastructure.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.jia.point.domain.exceptions.ErrorMessage.ALREADY_SIGN_UP;

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
            throw new MemberException(ALREADY_SIGN_UP, member.get().getMemberIdx());
        }

        Member toSave = Member.toEntity(command);
        Member save = memberRepository.save(toSave);
    }


}
