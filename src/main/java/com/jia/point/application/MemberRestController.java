package com.jia.point.application;

import com.jia.point.application.create_member.CreateMemberRequest;
import com.jia.point.domain.dtos.MemberDto;
import com.jia.point.facade.MemberFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberRestController {

    private final MemberFacade memberFacade;

    /**
     * 회원가입
     */
    @PostMapping("/member")
    public void createMember(@RequestBody CreateMemberRequest request) {
        memberFacade.signUpMember(MemberDto.Create.toCommand(request));
    }

}
