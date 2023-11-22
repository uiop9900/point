package com.jia.point.interfaces;

import com.jia.point.domain.dtos.MemberDto;
import com.jia.point.domain.exceptions.MemberException;
import com.jia.point.facade.MemberFacade;
import com.jia.point.interfaces.dtos.CommonResponse;
import com.jia.point.interfaces.dtos.CreateMemberRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        try {
            memberFacade.signUpMember(MemberDto.Create.toCommand(request));
            return CommonResponse.success(Boolean.TRUE);
        } catch (MemberException e) {
            return CommonResponse.fail(e.getMessage());
        }
    }

}
