package com.nhnacademy.accountapi.service.impl;

import com.nhnacademy.accountapi.dto.MemberIdNameResponse;
import com.nhnacademy.accountapi.entity.Member;
import com.nhnacademy.accountapi.entity.Status;
import com.nhnacademy.accountapi.exception.BaseException;
import com.nhnacademy.accountapi.exception.ErrorCode;
import com.nhnacademy.accountapi.exception.MemberNotActiveException;
import com.nhnacademy.accountapi.exception.MemberNotFoundException;
import com.nhnacademy.accountapi.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberQueryServiceImplTest {

    @Mock
    MemberRepository memberRepository;

    @InjectMocks
    MemberQueryServiceImpl memberQueryService;

    @Test
    @DisplayName("getMember 성공 테스트")
    void getMemberSuccessTest() {
        Member member = new Member("test@test.com", "test1234!", "test");

        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));

        Member newMember = memberQueryService.getMember(1L);

        assertThat(newMember).isEqualTo(member);
        verify(memberRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("getMember 실패 테스트")
    void getMemberFailTest() {
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        Throwable throwable = catchThrowable(() -> memberQueryService.getMember(1L));

        assertThat(throwable).isInstanceOf(MemberNotFoundException.class);
        assertThat(((BaseException) throwable).getErrorCode()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND);

        verify(memberRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("email로 찾기 성공 테스트")
    void getMemberByEmailSuccessTest() {
        Member member = new Member("test@test.com", "test1234!", "test");

        when(memberRepository.findMemberByEmail("test@test.com")).thenReturn(Optional.of(member));

        Member newMember = memberQueryService.getMemberByEmail("test@test.com");

        assertThat(newMember).isEqualTo(member);

        verify(memberRepository, times(1)).findMemberByEmail(anyString());
    }

    @Test
    @DisplayName("email로 찾기 실패 테스트")
    void getMemberByEmailFailTest() {
        when(memberRepository.findMemberByEmail("test@test.com")).thenReturn(Optional.empty());

       Throwable throwable = catchThrowable(() -> memberQueryService.getMemberByEmail("test@test.com"));

        assertThat(throwable).isInstanceOf(MemberNotFoundException.class);
        assertThat(((BaseException) throwable).getErrorCode()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND);

        verify(memberRepository, times(1)).findMemberByEmail(anyString());
    }

    @Test
    @DisplayName("email and status 찾기 성공 테스트")
    void getMemberByEmailAndStatusSuccessTest() {
        Member member = new Member("test@test.com", "test1234!", "test");

        when(memberRepository.findMEmberByEmailAndStatus("test@test.com", Status.ACTIVE)).thenReturn(Optional.of(member));

        Member newMember = memberQueryService.getMemberByEmailAndStatus("test@test.com", Status.ACTIVE);

        assertThat(newMember).isEqualTo(member);

        verify(memberRepository, times(1)).findMEmberByEmailAndStatus(anyString(), any());
    }

    @Test
    @DisplayName("email and status 찾기 실패 테스트")
    void getMemberByEmailAndStatusFailTest() {
        when(memberRepository.findMEmberByEmailAndStatus("test@test.com", Status.ACTIVE)).thenReturn(Optional.empty());

        Throwable throwable = catchThrowable(() -> memberQueryService.getMemberByEmailAndStatus("test@test.com", Status.ACTIVE));

        assertThat(throwable).isInstanceOf(MemberNotActiveException.class);
        assertThat(((BaseException) throwable).getErrorCode()).isEqualTo(ErrorCode.MEMBER_NOT_ACTIVE);

        verify(memberRepository, times(1)).findMEmberByEmailAndStatus(anyString(), any());
    }

    @Test
    @DisplayName("id로 memberList 찾기 성공 테스트")
    void getMembersListByIdSuccessTest() {
        List<Long> ids = List.of(1L, 2L);
        List<Member> members = List.of(
                new Member("test1@test.com", "test1234!", "test1"),
                new Member("test2@test.com", "test1234!", "test2")
        );

        ReflectionTestUtils.setField(members.get(0), "id", 1L);
        ReflectionTestUtils.setField(members.get(1), "id", 2L);

        when(memberRepository.findAllById(ids)).thenReturn(members);

        List<MemberIdNameResponse> result = memberQueryService.getMembersListById(ids);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).id()).isEqualTo(1L);
        assertThat(result.get(0).name()).isEqualTo("test1");
        assertThat(result.get(1).id()).isEqualTo(2L);
        assertThat(result.get(1).name()).isEqualTo("test2");

        verify(memberRepository, times(1)).findAllById(anyCollection());
    }

    @Test
    @DisplayName("id로 memberList 찾기 공백 테스트")
    void getMembersListByIdFailTest() {
        List<Long> ids = new ArrayList<>();

        when(memberRepository.findAllById(ids)).thenReturn(new ArrayList<>());

        List<MemberIdNameResponse> result = memberQueryService.getMembersListById(ids);

        assertThat(result).isEmpty();

        verify(memberRepository, times(1)).findAllById(anyCollection());
    }
}
