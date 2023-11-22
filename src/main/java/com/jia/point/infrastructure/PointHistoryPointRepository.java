package com.jia.point.infrastructure;

import com.jia.point.domain.entity.PointHstRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointHistoryPointRepository extends JpaRepository<PointHstRecord, Long> {

}
