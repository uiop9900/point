package com.jia.point.application;

import com.jia.point.application.dto.CreateMemberRequest;
import com.jia.point.domain.MemberDto;
import com.jia.point.facade.MemberFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PointRestController {

    private final MemberFacade memberFacade;

    @PostMapping("/point")
    public void createMember(CreateMemberRequest request) {
        memberFacade.signUpMember(MemberDto.Create.toCommand(request));
    }


}
