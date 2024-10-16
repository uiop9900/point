package com.jia.point.interfaces;

import com.jia.point.domain.dtos.PointCommand;
import com.jia.point.facade.ExpiredPointData;
import com.jia.point.facade.MemberFacade;
import com.jia.point.facade.PointFacade;
import com.jia.point.interfaces.dtos.CancelPointRequest;
import com.jia.point.interfaces.dtos.CommonResponse;
import com.jia.point.interfaces.dtos.CreatePointRequest;
import com.jia.point.interfaces.dtos.UsePointRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/points")
public class PointRestController {

    private final MemberFacade memberFacade;

    private final PointFacade pointFacade;


    /**
     * 포인트 적립
     */
    @PostMapping("/earn")
    public CommonResponse<BigDecimal> createPoint(@RequestBody @Valid CreatePointRequest request) {
        return CommonResponse.success(pointFacade.earnPoint(PointCommand.Create.toCommand(request)));
    }

    /**
     * 포인트 사용
     */
    @PostMapping("/use")
    public CommonResponse<BigDecimal> usePoint(@RequestBody @Valid UsePointRequest request) {
        return CommonResponse.success(pointFacade.usePoint(PointCommand.Use.toCommand(request)));
    }

    /**
     * 포인트 취소<br>
     * 유저별 목록조회 화면에서 history 번호를 받아올거라 판단
     */
    @PostMapping("/cancel")
    public CommonResponse<BigDecimal> cancelPoint(@RequestBody @Valid CancelPointRequest request) {
        return CommonResponse.success(pointFacade.cancelPoint(request.getPointHstIdx(), request.getMemberIdx()));
    }


    /**
     * 포인트 만료
     */
    @PostMapping("/expire/{today}")
    @Scheduled(cron = "0 0 * * * ") // 매일 자정(밤 12시)
    public void expirePoints() {
        log.info("[SCHEDULED] 포인트 만료 시작합니다. today = {}", LocalDate.now());
        ExpiredPointData result = pointFacade.expirePoints();
        if (result.getExpired() != result.getToExpire()) {
            log.error("[SCHEDULED] 만료되지 않는 포인트가 존재합니다. {}건", result.getToExpire() - result.getExpired());
        }
        log.info("[SCHEDULED] 포인트 만료 종료합니다. toExpire ={}, expired = {}", result.getToExpire(), result.getExpired());
    }


}
