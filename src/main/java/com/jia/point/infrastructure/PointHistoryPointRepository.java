package com.jia.point.infrastructure;

import com.jia.point.domain.entity.PointHst;
import com.jia.point.domain.entity.PointHstPoint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointHistoryPointRepository extends JpaRepository<PointHstPoint, Long> {

}
