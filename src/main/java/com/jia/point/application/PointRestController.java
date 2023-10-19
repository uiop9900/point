package com.jia.point.application;

import com.jia.point.application.create_member.CreateMemberRequest;
import com.jia.point.domain.MemberDto;
import com.jia.point.domain.PointDto;
import com.jia.point.facade.MemberFacade;
import com.jia.point.facade.PointFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
public class PointRestController {

    private final MemberFacade memberFacade;

    private final PointFacade pointFacade;

    @PostMapping("/member")
    public void createMember(@RequestBody CreateMemberRequest request) {
        memberFacade.signUpMember(MemberDto.Create.toCommand(request));
    }

    /**
     * 포인트 적립
     */
    @PostMapping("/point")
    public BigDecimal createPoint(@RequestBody CreatePointRequest request) {
        return pointFacade.earnPoint(PointDto.Create.toCommand(request));
    }



}
