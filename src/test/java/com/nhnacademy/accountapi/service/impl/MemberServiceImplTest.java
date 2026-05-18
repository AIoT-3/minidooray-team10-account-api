package com.nhnacademy.accountapi.service.impl;

import com.nhnacademy.accountapi.entity.Member;
import com.nhnacademy.accountapi.entity.Status;
import com.nhnacademy.accountapi.exception.BaseException;
import com.nhnacademy.accountapi.exception.DuplicateEmailException;
import com.nhnacademy.accountapi.exception.ErrorCode;
import com.nhnacademy.accountapi.exception.MemberNotFoundException;
import com.nhnacademy.accountapi.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @Mock
    MemberRepository memberRepository;

    @Mock
    MemberQueryServiceImpl memberQueryService;

    @InjectMocks
    MemberServiceImpl memberService;

    @Test
    @DisplayName("save 성공 테스트")
    void saveSuccessTest() {
        Member member = new Member("test@test.com", "test1234!", "test");

        when(memberRepository.save(Mockito.any(Member.class))).thenReturn(member);
        when(memberRepository.existsMemberByEmail("test@test.com")).thenReturn(false);
        when(memberQueryService.getMember(1L)).thenReturn(member);

        memberService.createMember("test@test.com", "test1234!", "test");

        Member newMember = memberQueryService.getMember(1L);

        assertThat(newMember).isEqualTo(member);
    }

    @Test
    @DisplayName("save 실패 테스트")
    void saveFailTest() {
        when(memberRepository.existsMemberByEmail("test@test.com")).thenReturn(true);

        assertThatThrownBy(() -> memberService.createMember("test@test.com", "test1234!", "test"))
                .isInstanceOf(DuplicateEmailException.class)
                .hasMessage("이미 존재하는 이메일입니다.");
    }

    @Test
    @DisplayName("update 성공 테스트")
    void updateSuccessTest() {
        Member member = new Member("test@test.com", "test1234!", "test");
        when(memberQueryService.getMember(1L)).thenReturn(member);

        memberService.updateMember(1L, "test1234@", "test1234");

        assertThat(member.getPassword()).isEqualTo("test1234@");
        assertThat(member.getName()).isEqualTo("test1234");
    }

    @Test
    @DisplayName("update 실패 테스트")
    void updateFailTest() {
        when(memberQueryService.getMember(1L)).thenThrow(new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        Throwable throwable = catchThrowable(() -> memberService.updateMember(1L, "test1234@", "test1234"));

        assertThat(throwable).isInstanceOf(MemberNotFoundException.class);
        assertThat(((BaseException) throwable).getErrorCode()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND);
    }

    @Test
    @DisplayName("delete 성공 테스트")
    void deleteSuccessTest() {
        Member member = new Member("test@test.com", "test1234!", "test");
        when(memberQueryService.getMember(1L)).thenReturn(member);

        memberService.deleteMember(1L);

        assertThat(member.getStatus()).isEqualTo(Status.TERMINATE);
    }

    @Test
    @DisplayName("delete 실패 테스트")
    void deleteFailTest() {
        when(memberQueryService.getMember(1L)).thenThrow(new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        Throwable throwable = catchThrowable(() -> memberService.deleteMember(1L));

        assertThat(throwable).isInstanceOf(MemberNotFoundException.class);
        assertThat(((BaseException) throwable).getErrorCode()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND);
    }

    @Test
    @DisplayName("disable 성공 테스트")
    void disableSuccessTest() {
        Member member = new Member("test@test.com", "test1234!", "test");
        when(memberQueryService.getMember(1L)).thenReturn(member);
        memberService.disableMember(1L);

        assertThat(member.getStatus()).isEqualTo(Status.SLEEP);
    }

    @Test
    @DisplayName("disable 실패 테스트")
    void disableFailTest() {
        when(memberQueryService.getMember(1L)).thenThrow(new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        Throwable throwable = catchThrowable(() -> memberService.disableMember(1L));

        assertThat(throwable).isInstanceOf(MemberNotFoundException.class);
        assertThat(((BaseException) throwable).getErrorCode()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND);
    }

    @Test
    @DisplayName("activate 성공 테스트")
    void activateSuccessTest() {
        Member member = new Member("test@test.com", "test1234!", "test");
        when(memberQueryService.getMember(1L)).thenReturn(member);

        memberService.activateMember(1L);

        assertThat(member.getStatus()).isEqualTo(Status.ACTIVE);
    }

    @Test
    @DisplayName("disable 실패 테스트")
    void activateFailTest() {
        when(memberQueryService.getMember(1L)).thenThrow(new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        Throwable throwable = catchThrowable(() -> memberService.activateMember(1L));

        assertThat(throwable).isInstanceOf(MemberNotFoundException.class);
        assertThat(((BaseException) throwable).getErrorCode()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND);
    }
}
