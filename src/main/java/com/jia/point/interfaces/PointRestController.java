package com.jia.point.interfaces;

import com.jia.point.interfaces.dtos.CommonResponse;
import com.jia.point.interfaces.dtos.CreatePointRequest;
import com.jia.point.interfaces.dtos.UsePointRequest;
import com.jia.point.domain.dtos.PointDto;
import com.jia.point.domain.dtos.PointHstInfo;
import com.jia.point.facade.MemberFacade;
import com.jia.point.facade.PointFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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
     * 포인트 적립
     */
    @PostMapping("/point/{memberIdx}")
    public CommonResponse<BigDecimal> createPoint(@RequestBody CreatePointRequest request, @PathVariable("memberIdx") String memberIdx) {
        return CommonResponse.success(pointFacade.earnPoint(PointDto.Create.toCommand(request, memberIdx)));
    }

    /**
     * 포인트 사용
     */
    @PostMapping("/point:use")
    public CommonResponse<BigDecimal> usePoint(@RequestBody UsePointRequest request) {
        return CommonResponse.success(pointFacade.usePoint(PointDto.Use.toCommand(request)));
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
