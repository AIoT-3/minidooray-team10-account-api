package com.nhnacademy.accountapi.service;

import com.nhnacademy.accountapi.entity.Member;

public interface MemberQueryService {

    Member getMember(long id);

    Member getMemberByEmail(String email);
}
