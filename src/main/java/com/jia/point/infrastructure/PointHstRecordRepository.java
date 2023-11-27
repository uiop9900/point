package com.jia.point.infrastructure;

import com.jia.point.domain.entity.PointHst;
import com.jia.point.domain.entity.PointHstRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointHstRecordRepository extends JpaRepository<PointHstRecord, Long> {

    List<PointHstRecord> findAllByPointHst(PointHst pointHst);

}
