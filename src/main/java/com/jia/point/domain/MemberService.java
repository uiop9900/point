package com.jia.point.domain;


import com.jia.point.domain.dtos.MemberCommand;
import org.springframework.transaction.annotation.Transactional;

public interface MemberService {

    @Transactional void insertMember(MemberCommand.Create command);

}
