package com.jia.point.domain;


import org.springframework.transaction.annotation.Transactional;

public interface MemberService {
    @Transactional
    void insertMember(MemberDto.Create command);
}
