package com.jia.point.interfaces;

import com.jia.point.domain.dtos.PointCommand;
import com.jia.point.facade.MemberFacade;
import com.jia.point.facade.PointFacade;
import com.jia.point.interfaces.dtos.CommonResponse;
import com.jia.point.interfaces.dtos.CreatePointRequest;
import com.jia.point.interfaces.dtos.UsePointRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PointRestController {

    private final MemberFacade memberFacade;

    private final PointFacade pointFacade;


    /**
     * 포인트 적립
     */
    @PostMapping("/point/earn")
    public CommonResponse<BigDecimal> createPoint(@RequestBody CreatePointRequest request) {
        return CommonResponse.success(pointFacade.earnPoint(PointCommand.Create.toCommand(request)));
    }

    /**
     * 포인트 사용
     */
    @PostMapping("/point/use")
    public CommonResponse<BigDecimal> usePoint(@RequestBody UsePointRequest request) {
        return CommonResponse.success(pointFacade.usePoint(PointCommand.Use.toCommand(request)));
    }

    /**
     * 포인트 취소<br>
     * 유저별 목록조회 화면에서 history 번호를 받아올거라 판단
     */
    @PostMapping("/point/cancel")
    public CommonResponse<BigDecimal> cancelPoint(@RequestBody CancelPointRequest request) {
        return CommonResponse.success(pointFacade.cancelPoint(request.getPointHstIdx(), request.getMemberIdx()));
    }


    /**
     * 포인트 만료
     */
    @PostMapping("/point/expire/{today}")
    @Scheduled(cron = "0 0 * * * ") // 매일 자정(밤 12시)
    public void expirePoints() {
        log.info("[SCHEDULED] 포인트 만료 시작합니다. today = {}", LocalDate.now());
        Integer points = pointFacade.expirePoints();
        log.info("[SCHEDULED] 포인트 만료 종료합니다. expirePontsNumber ={}", points);
    }


    /**
     * 레디스 데이터 초기화<br>
     * 레디스의 값을 초기화할때 사용한다.<br>
     * 실무에서는 사용할 일이 거의 없을 것으로 판단.
     */
    @PostMapping("/redis")
    public void resetRedisValue() {
        memberFacade.resetRedisValue();
    }

}
