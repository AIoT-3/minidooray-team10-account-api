package com.nhnacademy.accountapi.service.impl;

import com.nhnacademy.accountapi.entity.Member;
import com.nhnacademy.accountapi.entity.Status;
import com.nhnacademy.accountapi.exception.DuplicateEmailException;
import com.nhnacademy.accountapi.exception.MemberAlreadyTerminateException;
import com.nhnacademy.accountapi.repository.MemberRepository;
import com.nhnacademy.accountapi.service.MemberQueryService;
import com.nhnacademy.accountapi.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberQueryService memberQueryService;

    @Transactional
    @Override
    public void createMember(String email, String password, String name) {
        if (memberRepository.existsMemberByEmail(email)) {
            throw new DuplicateEmailException("이미 존재하는 이메일입니다.");
        }

        memberRepository.save(new Member(email, password, name));
    }

    @Transactional
    @Override
    public void updateMember(long id, String password, String name) {
        Member member = memberQueryService.getMember(id);

        member.updatePassword(password);
        member.updateName(name);
    }

    @Transactional
    @Override
    public void deleteMember(long id) {
        Member member = memberQueryService.getMember(id);

        member.updateStatus(Status.TERMINATE);
    }

    @Transactional
    @Override
    public void disableMember(long id) {
        Member member = memberQueryService.getMember(id);

        if (Status.TERMINATE == member.getStatus()) {
            throw new MemberAlreadyTerminateException("탈퇴한 회원 입니다.");
        }

        member.updateStatus(Status.SLEEP);
    }

    @Transactional
    @Override
    public void activateMember(long id) {
        Member member = memberQueryService.getMember(id);

        if (Status.TERMINATE == member.getStatus()) {
            throw new MemberAlreadyTerminateException("탈퇴한 회원 입니다.");
        }

        member.updateStatus(Status.ACTIVE);
    }
}
