package com.jia.point.facade;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class ExpiredPointData {

    private Integer toExpire; // 만료시킬 개수
    private Integer expired; // 만료된 개수

}
