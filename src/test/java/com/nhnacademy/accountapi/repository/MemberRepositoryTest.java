package com.nhnacademy.accountapi.repository;

import com.nhnacademy.accountapi.entity.Member;
import com.nhnacademy.accountapi.entity.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    Member member;
    Member saved;

    @BeforeEach
    void setUp() {
        member = new Member("test@test.com", "test1234!", "test");

        saved = memberRepository.save(member);
    }

    @Test
    @DisplayName("회원 저장 테스트")
    void saveMemberTest() {

        assertThat(saved.getId()).isPositive();
        assertThat(saved.getEmail()).isEqualTo("test@test.com");
        assertThat(saved.getPassword()).isEqualTo("test1234!");
        assertThat(saved.getName()).isEqualTo("test");
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getLastLoginAt()).isNull();
    }

    @Test
    @DisplayName("회원 찾기 테스트")
    void findByIdTest() {
        Optional<Member> findMember = memberRepository.findById(member.getId());

        assertThat(findMember).isNotNull();
        assertThat(findMember.get().getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    @DisplayName("회원 이메일 존재 확인 테스트")
    void existsMemberByEmailTest() {
        assertThat(memberRepository.existsMemberByEmail(member.getEmail())).isTrue();
        assertThat(memberRepository.existsMemberByEmail("abcd@test.com")).isFalse();
    }

    @Test
    @DisplayName("회원 이메일일로 찾기 테스트")
    void findMemberByEmailSuccessTest() {
        Optional<Member> findMember = memberRepository.findMemberByEmail(member.getEmail());

        assertThat(findMember).isNotNull();
        assertThat(findMember.get().getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    @DisplayName("휴면 전환 계정 찾기 테스트")
    void findMembersByLastLoginAtBeforeAndStatusTest() {
        member.updateLastLoginAt(LocalDateTime.now());
        List<Member> memberList = memberRepository.findMembersByLastLoginAtBeforeAndStatus(LocalDateTime.now(), Status.ACTIVE);

        assertThat(memberList.getFirst().getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    @DisplayName("이메일 상태로 계정 찾기 테스트")
    void findMEmberByEmailAndStatusTest() {
        Optional<Member> findMember = memberRepository.findMEmberByEmailAndStatus(member.getEmail(), Status.ACTIVE);

        assertThat(findMember.get().getEmail()).isEqualTo(member.getEmail());
        assertThat(findMember.get().getPassword()).isEqualTo(member.getPassword());
    }

}
