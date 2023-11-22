package com.jia.point.application.get_histories;

import com.jia.point.domain.dtos.PointHstInfo;
import com.jia.point.domain.enums.PointType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor(staticName = "of")
public class GetPointHistoriesResponse {

    List<PointHstInfo> pointHstInfos;

}
