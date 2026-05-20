package com.nhnacademy.accountapi.service.impl;

import com.nhnacademy.accountapi.dto.MemberListRequest;
import com.nhnacademy.accountapi.dto.MemberListResponse;
import com.nhnacademy.accountapi.entity.Member;
import com.nhnacademy.accountapi.exception.ErrorCode;
import com.nhnacademy.accountapi.exception.MemberNotFoundException;
import com.nhnacademy.accountapi.repository.MemberRepository;
import com.nhnacademy.accountapi.service.MemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberQueryServiceImpl implements MemberQueryService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    @Override
    public Member getMember(long id) {
        Optional<Member> member = memberRepository.findById(id);

        if (member.isEmpty()) {
            throw new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND);
        }

        return member.get();
    }

    @Transactional(readOnly = true)
    @Override
    public Member getMemberByEmail(String email) {
        Optional<Member> member = memberRepository.findMemberByEmail(email);

        if (member.isEmpty()) {
            throw new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND);
        }

        return member.get();
    }

    @Transactional(readOnly = true)
    @Override
    public List<MemberListResponse> getMembersListById(List<MemberListRequest> memberIdList) {
        List<Member> memberList = memberRepository.findAllById(memberIdList
                                                                .stream().map(MemberListRequest::id)
                                                                .toList());

        List<MemberListResponse> memberListRequests = new ArrayList<>();
        for (Member member : memberList) {
            memberListRequests.add(new MemberListResponse(member.getId(), member.getName()));
        }

        return memberListRequests;
    }
}
