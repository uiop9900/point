package com.jia.point.infrastructure;

import com.jia.point.domain.entity.Point;
import com.jia.point.domain.entity.PointHst;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointHistoryRepository extends JpaRepository<PointHst, Long> {

}
