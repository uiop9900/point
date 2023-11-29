package com.jia.point.domain;

import com.jia.point.domain.entity.Point;
import com.jia.point.domain.entity.PointHst;
import com.jia.point.domain.entity.PointHstRecord;
import com.jia.point.infrastructure.PointHistoryRepository;
import com.jia.point.infrastructure.PointHstRecordRepository;
import com.jia.point.infrastructure.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PointStoreImpl implements PointStore {

	private final PointRepository pointRepository;

	private final PointHistoryRepository pointHistoryRepository;

	private final PointHstRecordRepository pointHstRecordRepository;

	@Override public void save(final Point point) {
		pointRepository.save(point);
	}

	@Override public PointHst save(final PointHst pointHst) {
		return pointHistoryRepository.save(pointHst);
	}

	@Override public void save(final PointHstRecord pointHstRecord) {
		pointHstRecordRepository.save(pointHstRecord);
	}


}
