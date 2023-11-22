package com.jia.point.infrastructure;

import com.jia.point.domain.entity.Point;
import com.jia.point.domain.enums.PointStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.jia.point.domain.entity.QPoint.point;

@Repository
@RequiredArgsConstructor
public class PointRepositoryCustomImpl implements PointRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Point> findPointsByMemberId(Long memberId) {
        return queryFactory.selectFrom(point)
                .where(
                        point.member.memberIdx.eq(memberId)
                                .and(point.useStatus.in(PointStatus.USING, PointStatus.UNUSED))
                )
                .orderBy(point.regDt.asc())
                .fetch();
    }

    @Override
    public List<Point> findPointAfterToday(LocalDate today) {
        return queryFactory.selectFrom(point)
                .where(
                        point.expiredDate.eq(today)
                                .and(point.useStatus.in(PointStatus.USING, PointStatus.UNUSED))
                ).fetch();
    }

}
