package com.nhnacademy.accountapi.service.impl;

import com.nhnacademy.accountapi.entity.Member;
import com.nhnacademy.accountapi.exception.ErrorCode;
import com.nhnacademy.accountapi.exception.MemberNotFoundException;
import com.nhnacademy.accountapi.repository.MemberRepository;
import com.nhnacademy.accountapi.service.MemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    public Member getMemberByEmail(String email) {
        Optional<Member> member = memberRepository.findMemberByEmail(email);

        if (member.isEmpty()) {
            throw new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND);
        }

        return member.get();
    }
}
