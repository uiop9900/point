package com.jia.point.application;

import com.jia.point.application.dtos.CommonResponse;
import com.jia.point.application.dtos.CreateMemberRequest;
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
    public CommonResponse<Boolean> createMember(@RequestBody CreateMemberRequest request) {
        memberFacade.signUpMember(MemberDto.Create.toCommand(request));
        return CommonResponse.success(Boolean.TRUE);
    }

}
