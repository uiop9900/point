package com.jia.point.domain;

import com.jia.point.domain.entity.Member;
import com.jia.point.domain.entity.Point;
import com.jia.point.domain.entity.PointHst;
import com.jia.point.domain.entity.PointHstRecord;
import java.time.LocalDate;
import java.util.List;

public interface PointReader {

    Point findPointByIdx(Long pointIdx);
    List<Point> findPointsByMemberId(Long memberId);

    List<Point> findPointAfterToday(LocalDate today);

    List<PointHst> findPointsByMember(Member member, Integer page);

    PointHst findPointHstByIdx(Long pointHstIdx);

    List<PointHstRecord> findRecordsByPointHst(PointHst pointHst);

}
