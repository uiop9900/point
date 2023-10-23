package com.jia.point.domain;


import com.jia.point.domain.dtos.MemberDto;
import org.springframework.transaction.annotation.Transactional;

public interface MemberService {
    @Transactional
    void insertMember(MemberDto.Create command);
}
