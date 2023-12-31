package com.jia.point.facade;

import com.jia.point.domain.dtos.MemberCommand;
import com.jia.point.domain.MemberService;
import com.jia.point.domain.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberFacade {

    private final MemberService memberService;

    private final RedisService redisService;

    public void signUpMember(MemberCommand.Create command) {
     memberService.insertMember(command);
    }

    public void resetRedisValue() {
        redisService.resetRedisValue();
    }


}
