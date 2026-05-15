package com.nhnacademy.accountapi.service.Impl;

import com.nhnacademy.accountapi.entity.Member;
import com.nhnacademy.accountapi.entity.Status;
import com.nhnacademy.accountapi.exception.DuplicateEmailException;
import com.nhnacademy.accountapi.exception.MemberNotFoundException;
import com.nhnacademy.accountapi.repository.MemberRepository;
import com.nhnacademy.accountapi.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    @Override
    public Member getMember(long id) {

        Optional<Member> member = memberRepository.findById(id);

        if (member.isEmpty()) {
            throw new MemberNotFoundException("존재하지 않는 사용자 입니다.");
        }

        return member.get();
    }

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
        Member member = getMember(id);

        member.updatePassword(password);
        member.updateName(name);
    }

    @Transactional
    @Override
    public void deleteMember(long id) {
        Member member = getMember(id);

        member.updateStatus(Status.TERMINATE);
    }

    @Transactional
    @Override
    public void disableMember(long id) {
        Member member = getMember(id);

        member.updateStatus(Status.SLEEP);
    }

    @Transactional
    @Override
    public void activateMember(long id) {
        Member member = getMember(id);

        member.updateStatus(Status.ACTIVE);
    }
}
