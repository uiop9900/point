package com.jia.point.domain;

import com.jia.point.domain.entity.Point;
import com.jia.point.domain.entity.PointHst;
import com.jia.point.domain.entity.PointHstRecord;

public interface PointStore {

	void save(Point point);

	PointHst save(PointHst pointHst);

	void save(PointHstRecord pointHstRecord);

}
