package com.jia.point.infrastructure;

import com.jia.point.domain.entity.Member;
import com.jia.point.domain.entity.PointHst;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointHistoryRepository extends JpaRepository<PointHst, Long> {

    Page<PointHst> findAllByMember(Member member, Pageable pageable);

}
