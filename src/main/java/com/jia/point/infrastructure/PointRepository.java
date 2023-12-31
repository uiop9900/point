package com.jia.point.infrastructure;

import com.jia.point.domain.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointRepository extends JpaRepository<Point, Long>, PointRepositoryCustom {

}
