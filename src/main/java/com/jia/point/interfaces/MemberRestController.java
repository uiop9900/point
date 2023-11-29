package com.jia.point.interfaces;

import com.jia.point.domain.dtos.MemberCommand;
import com.jia.point.domain.dtos.PointHstInfo;
import com.jia.point.domain.exceptions.MemberException;
import com.jia.point.facade.MemberFacade;
import com.jia.point.facade.PointFacade;
import com.jia.point.interfaces.dtos.CommonResponse;
import com.jia.point.interfaces.dtos.CreateMemberRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberRestController {

    private final MemberFacade memberFacade;

    private final PointFacade pointFacade;

    /**
     * 회원가입
     */
    @PostMapping("/sign-up")
    public CommonResponse<Boolean> createMember(@RequestBody CreateMemberRequest request) {
        memberFacade.signUpMember(MemberCommand.Create.toCommand(request));
        return CommonResponse.success(Boolean.TRUE);
    }

    /**
     * 사용자별 포인트 내역 조회
     */
    @GetMapping("/point/{memberIdx}/{page}")
    public CommonResponse<List<PointHstInfo>> getPointHistories(@PathVariable String memberIdx, @PathVariable String page) {
        return CommonResponse.success(pointFacade.getPointHistories(memberIdx, Integer.valueOf(page)));
    }

}
