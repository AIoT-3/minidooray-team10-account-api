package com.nhnacademy.accountapi.service.impl;

import com.nhnacademy.accountapi.entity.Member;
import com.nhnacademy.accountapi.exception.BaseException;
import com.nhnacademy.accountapi.exception.ErrorCode;
import com.nhnacademy.accountapi.exception.MemberNotFoundException;
import com.nhnacademy.accountapi.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

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
    }

    @Test
    @DisplayName("getMember 실패 테스트")
    void getMemberFailTest() {
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        Throwable throwable = catchThrowable(() -> memberQueryService.getMember(1L));

        assertThat(throwable).isInstanceOf(MemberNotFoundException.class);
        assertThat(((BaseException) throwable).getErrorCode()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND);
    }

    @Test
    @DisplayName("email로 찾기 성공 테스트")
    void getMemberByEmailSuccessTest() {

    }
}
