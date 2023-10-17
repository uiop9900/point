package com.jia.point.facade;

import com.jia.point.domain.MemberDto;
import com.jia.point.domain.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberFacade {

    private final MemberService memberService;

    public void signUpMember(MemberDto.Create command) {
     memberService.insertMember(command);
    }


}
