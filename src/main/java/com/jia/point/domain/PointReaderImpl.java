package com.jia.point.domain;

import static com.jia.point.domain.exceptions.ErrorMessage.INVALID_REQUEST;

import com.jia.point.domain.dtos.PointHstInfo;
import com.jia.point.domain.entity.Member;
import com.jia.point.domain.entity.Point;
import com.jia.point.domain.entity.PointHst;
import com.jia.point.domain.entity.PointHstRecord;
import com.jia.point.infrastructure.PointHistoryRepository;
import com.jia.point.infrastructure.PointHstRecordRepository;
import com.jia.point.infrastructure.PointRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PointReaderImpl implements PointReader {

    private final Integer SELECT_SIZE = 10;

    private final PointRepository pointRepository;

    private final PointHistoryRepository pointHistoryRepository;

    private final PointHstRecordRepository pointHstRecordRepository;

    @Override public Point findPointByIdx(final Long pointIdx) {
        return pointRepository.findById(pointIdx)
            .orElseThrow(() -> new IllegalArgumentException(INVALID_REQUEST));
    }

    @Override public List<Point> findPointsByMemberId(final Long memberId) {
        return pointRepository.findPointsByMemberId(memberId);
    }

    @Override public List<Point> findPointAfterToday(final LocalDate today) {
        return pointRepository.findPointAfterToday(today);
    }

    @Override public List<PointHst> findPointsByMember(final Member member, Integer page) {
        Pageable pageable = PageRequest.of(page, SELECT_SIZE);
        final Page<PointHst> pointPages = pointHistoryRepository.findAllByMember(member, pageable);
        return pointPages.stream().toList();
    }

    @Override public PointHst findPointHstByIdx(final Long pointHstIdx) {
        return pointHistoryRepository.findById(pointHstIdx)
            .orElseThrow(() -> new IllegalArgumentException(INVALID_REQUEST));
    }

    @Override public List<PointHstRecord> findRecordsByPointHst(final PointHst pointHst) {
        return pointHstRecordRepository.findAllByPointHst(pointHst);
    }
}
