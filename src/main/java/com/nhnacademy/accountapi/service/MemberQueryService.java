package com.nhnacademy.accountapi.service;

import com.nhnacademy.accountapi.dto.MemberIdNameResponse;
import com.nhnacademy.accountapi.entity.Member;

import java.util.List;

public interface MemberQueryService {

    Member getMember(long id);

    Member getMemberByEmail(String email);

    List<MemberIdNameResponse> getMembersListById(List<Long> memberIdList);
}
