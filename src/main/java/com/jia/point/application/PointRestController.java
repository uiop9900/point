package com.jia.point.application;

import com.jia.point.application.create_member.CreateMemberRequest;
import com.jia.point.domain.dtos.MemberDto;
import com.jia.point.domain.dtos.PointDto;
import com.jia.point.facade.MemberFacade;
import com.jia.point.facade.PointFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PointRestController {

    private final MemberFacade memberFacade;

    private final PointFacade pointFacade;

    /**
     * 레디스 데이터 초기화
     */
    @PostMapping("/redis")
    public void resetRedisValue() {
        memberFacade.resetRedisValue();
    }

    /**
     * 회원가입
     */
    @PostMapping("/member")
    public void createMember(@RequestBody CreateMemberRequest request) {
        memberFacade.signUpMember(MemberDto.Create.toCommand(request));
    }

    /**
     * 포인트 적립
     */
    @PostMapping("/point:earn")
    public BigDecimal createPoint(@RequestBody CreatePointRequest request) {
        return pointFacade.earnPoint(PointDto.Create.toCommand(request));
    }

    /**
     * 포인트 사용
     */
    @PostMapping("/point:use")
    public BigDecimal usePoint(@RequestBody UsePointRequest request) {
        return pointFacade.usePoint(PointDto.Use.toCommand(request));
    }


    /**
     * 포인트 내역 조회
     */
    @GetMapping("/point/{page}")
    public GetPointHistoriesResponse getPointHistories(@PathVariable String page) {
        return GetPointHistoriesResponse.of(pointFacade.getPointHistories(Integer.valueOf(page == null ? "0" : page)));
    }

    /**
     * 포인트 만료
     */
    @PostMapping("/point/{today}")
    @Scheduled(cron = "0 4 * * * ") // 매일 새벽 5시
    public void expirePoints() {
        log.info("[SCHEDULED] 포인트 만료 시작합니다. today = {}", LocalDate.now());
        Integer points = pointFacade.expirePoints();
        log.info("[SCHEDULED] 포인트 만료 종료합니다. expirePontsNumber ={}", points);
    }
}
