package com.jia.point.interfaces;

import com.jia.point.facade.MemberFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admins")
public class AdminController {

    private final MemberFacade memberFacade;

    /**
     * 레디스 데이터 초기화<br>
     * 레디스의 값을 초기화할때 사용한다.<br>
     * 실무에서는 사용할 일이 거의 없을 것으로 판단.
     */
    @PostMapping("/reset")
    public void resetRedisValue() {
        memberFacade.resetRedisValue();
    }

}
