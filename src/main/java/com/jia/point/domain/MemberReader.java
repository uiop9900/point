package com.jia.point.domain;

import com.jia.point.domain.entity.Member;

public interface MemberReader {
    Member findByMemberId(Long memberId);

}
