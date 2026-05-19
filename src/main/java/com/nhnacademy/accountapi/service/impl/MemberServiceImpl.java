package com.nhnacademy.accountapi.service.impl;

import com.nhnacademy.accountapi.entity.Member;
import com.nhnacademy.accountapi.entity.Status;
import com.nhnacademy.accountapi.exception.DuplicateEmailException;
import com.nhnacademy.accountapi.exception.ErrorCode;
import com.nhnacademy.accountapi.exception.MemberAlreadyTerminateException;
import com.nhnacademy.accountapi.repository.MemberRepository;
import com.nhnacademy.accountapi.service.MemberQueryService;
import com.nhnacademy.accountapi.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberQueryService memberQueryService;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void createMember(String email, String password, String name) {
        if (memberRepository.existsMemberByEmail(email)) {
            throw new DuplicateEmailException(ErrorCode.DUPLICATE_EMAIL);
        }

        memberRepository.save(new Member(email, password, name));
    }

    @Transactional
    @Override
    public void updateMember(long id, String password, String name) {
        Member member = memberQueryService.getMember(id);

        if (Status.TERMINATE == member.getStatus()) {
            throw new MemberAlreadyTerminateException(ErrorCode.MEMBER_TERMINATED);
        }

        if (StringUtils.hasText(password)) {
            member.updatePassword(passwordEncoder.encode(password));
        }
        if (StringUtils.hasText(name)) {
            member.updateName(name);
        }
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
            throw new MemberAlreadyTerminateException(ErrorCode.MEMBER_TERMINATED);
        }

        member.updateStatus(Status.SLEEP);
    }

    @Transactional
    @Override
    public void activateMember(long id) {
        Member member = memberQueryService.getMember(id);

        if (Status.TERMINATE == member.getStatus()) {
            throw new MemberAlreadyTerminateException(ErrorCode.MEMBER_TERMINATED);
        }

        member.updateStatus(Status.ACTIVE);
    }

    @Transactional
    @Override
    public void updateLastLoginAt(long id) {
        Member member = memberQueryService.getMember(id);

        if (Status.TERMINATE == member.getStatus()) {
            throw new MemberAlreadyTerminateException(ErrorCode.MEMBER_TERMINATED);
        }

        member.updateLastLoginAt(LocalDateTime.now());
    }
}
