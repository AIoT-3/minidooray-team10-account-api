package com.nhnacademy.accountapi.service;

import com.nhnacademy.accountapi.dto.MemberIdNameResponse;
import com.nhnacademy.accountapi.entity.Member;
import com.nhnacademy.accountapi.entity.Status;

import java.util.List;

public interface MemberQueryService {

    Member getMember(long id);

    Member getMemberByEmail(String email);

    Member getMemberByEmailAndStatus(String email, Status status);

    List<MemberIdNameResponse> getMembersListById(List<Long> memberIdList);
}
