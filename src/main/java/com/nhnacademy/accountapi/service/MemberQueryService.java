package com.nhnacademy.accountapi.service;

import com.nhnacademy.accountapi.dto.MemberListRequest;
import com.nhnacademy.accountapi.dto.MemberListResponse;
import com.nhnacademy.accountapi.entity.Member;

import java.util.List;

public interface MemberQueryService {

    Member getMember(long id);

    Member getMemberByEmail(String email);

    List<MemberListResponse> getMembersListById(List<MemberListRequest> memberIdList);
}
