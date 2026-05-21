package com.nhnacademy.accountapi.controller;

import com.nhnacademy.accountapi.dto.MemberIdNameResponse;
import com.nhnacademy.accountapi.entity.Member;
import com.nhnacademy.accountapi.entity.Status;
import com.nhnacademy.accountapi.exception.ErrorCode;
import com.nhnacademy.accountapi.exception.MemberAlreadyTerminateException;
import com.nhnacademy.accountapi.exception.MemberNotActiveException;
import com.nhnacademy.accountapi.exception.MemberNotFoundException;
import com.nhnacademy.accountapi.service.MemberQueryService;
import com.nhnacademy.accountapi.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MemberService memberService;

    @MockitoBean
    private MemberQueryService memberQueryService;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원 가입 성공 테스트")
    void createMembersSuccessTest() throws Exception {
        mockMvc.perform(post("/api/account/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "email":"test@test.com",
                        "password":"test1234!",
                        "name":"test"
                    }
                """))
                .andDo(print())
                .andExpect(status().isCreated());

        verify(memberService, times(1)).createMember("test@test.com", "test1234!", "test");
    }

    @Test
    @DisplayName("회원 가입 실패 테스트")
    void createMembersFailTest() throws Exception {
        mockMvc.perform(post("/api/account/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "email":"test@",
                        "password":"test1234!",
                        "name":"test"
                    }
                """))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(memberService, times(0)).createMember(anyString(), anyString(), anyString());

        mockMvc.perform(post("/api/account/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "email":"test@test.com",
                        "password":"test123412",
                        "name":"test"
                    }
                """))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(memberService, times(0)).createMember(anyString(), anyString(), anyString());

        mockMvc.perform(post("/api/account/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "email":"test@test.com",
                        "password":"test1234!",
                        "name":""
                    }
                """))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(memberService, times(0)).createMember(anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("email 회원 조회 성공 테스트")
    void loginMemberSuccessTest() throws Exception {
        Member member = new Member("test@test.com", "test1234!", "test");

        when(memberQueryService.getMemberByEmail("test@test.com")).thenReturn(member);

        mockMvc.perform(get("/api/account/members/by-email")
                        .param("email", "test@test.com"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@test.com"))
                .andExpect(jsonPath("$.password").value("test1234!"))
                .andExpect(jsonPath("$.status").value(Status.ACTIVE.name()));

        verify(memberQueryService, times(1)).getMemberByEmail("test@test.com");
    }

    @Test
    @DisplayName("email 회원 조회 실패 테스트")
    void loginMemberFailTest() throws Exception {
        when(memberQueryService.getMemberByEmail(anyString())).thenThrow(new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        mockMvc.perform(get("/api/account/members/by-email")
                        .param("email", "test@test.com"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.code").value("A001"))
                .andExpect(jsonPath("$.message").value("존재하지 않는 사용자 입니다."));

        verify(memberQueryService, times(1)).getMemberByEmail("test@test.com");
    }

    @Test
    @DisplayName("회원 리스트 조회 성공 테스트")
    void getMemberListSuccessTest() throws Exception {
        Member member1 = new Member("test1@test.com", "test1234!", "test1");
        Member member2 = new Member("test2@test.com", "test1234!", "test2");
        List<MemberIdNameResponse> memberList = new ArrayList<>();
        memberList.add(new MemberIdNameResponse(member1.getId(), member1.getName()));
        memberList.add(new MemberIdNameResponse(member2.getId(), member2.getName()));

        List<Long> list = new ArrayList<>();
        list.add(1L);
        list.add(2L);

        when(memberQueryService.getMembersListById(list)).thenReturn(memberList);

        mockMvc.perform(post("/api/account/members/batch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "ids":[1, 2]
                                }
                                """))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());

        verify(memberQueryService, times(1)).getMembersListById(list);
    }

    @Test
    @DisplayName("회원 리스트 조회 성공 공백 테스트")
    void getMemberListEmptySuccessTest() throws Exception {
        List<Long> list = new ArrayList<>();

        when(memberQueryService.getMembersListById(list)).thenReturn(new ArrayList<>());

        mockMvc.perform(post("/api/account/members/batch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "ids":[]
                                }
                                """))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isEmpty());

        verify(memberQueryService, times(1)).getMembersListById(list);
    }

    @Test
    @DisplayName("회원 id반환 성공 테스트")
    void getMemberByIdSuccessTest() throws Exception {
        Member member = new Member("test@test.com", "test1234!", "test");

        when(memberQueryService.getMemberByEmailAndStatus("test@test.com", Status.ACTIVE)).thenReturn(member);

        mockMvc.perform(post("/api/account/members/id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "email":"test@test.com"
                                }
                                """))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty());

        verify(memberQueryService, times(1)).getMemberByEmailAndStatus("test@test.com", Status.ACTIVE);
    }

    @Test
    @DisplayName("회원 id반환 실패 테스트")
    void getMemberByIdFailTest() throws Exception {
        when(memberQueryService.getMemberByEmailAndStatus("test@test.com", Status.ACTIVE)).thenThrow(new MemberNotActiveException(ErrorCode.MEMBER_NOT_ACTIVE));

        mockMvc.perform(post("/api/account/members/id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "email":"test@test.com"
                                }
                                """))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.code").value("A002"))
                .andExpect(jsonPath("$.message").value("활동상태가 아닌 사용자 입니다."));

        verify(memberQueryService, times(1)).getMemberByEmailAndStatus("test@test.com", Status.ACTIVE);
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    void loginMembersSuccessTest() throws Exception {
        doNothing().when(memberService).updateLastLoginAt(1L);

        mockMvc.perform(patch("/api/account/members/me")
                        .header("X-USER-ID", 1L))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(memberService, times(1)).updateLastLoginAt(1L);
    }

    @Test
    @DisplayName("로그인 실패 테스트")
    void loginMembersFailTest() throws Exception {
        doThrow(new MemberAlreadyTerminateException(ErrorCode.MEMBER_TERMINATED)).when(memberService).updateLastLoginAt(1L);

        mockMvc.perform(patch("/api/account/members/me")
                        .header("X-USER-ID", 1L))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.code").value("A011"))
                .andExpect(jsonPath("$.message").value("이미 탈퇴한 회원 입니다."));

        verify(memberService, times(1)).updateLastLoginAt(1L);
    }

    @Test
    @DisplayName("회원정보조회 성공 테스트")
    void getMembersSuccessTest() throws Exception {
        Member member = new Member("test@test.com", "test1234!", "test");

        when(memberQueryService.getMember(1L)).thenReturn(member);

        mockMvc.perform(get("/api/account/members/me")
                        .header("X-USER-ID", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@test.com"))
                .andExpect(jsonPath("$.name").value("test"));

        verify(memberQueryService, times(1)).getMember(1L);
    }

    @Test
    @DisplayName("회원정보조회 실패 테스트")
    void getMembersFailTest() throws Exception {
        when(memberQueryService.getMember(1L)).thenThrow(new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        mockMvc.perform(get("/api/account/members/me")
                        .header("X-USER-ID", 1L))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.code").value("A001"))
                .andExpect(jsonPath("$.message").value("존재하지 않는 사용자 입니다."));

        verify(memberQueryService, times(1)).getMember(1L);
    }

    @Test
    @DisplayName("회원정보수정 성공 테스트")
    void updateMembersSuccessTest() throws Exception {
        doNothing().when(memberService).updateMember(1L, "test1234@", "test1");

        mockMvc.perform(put("/api/account/members/me")
                        .header("X-USER-ID", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "password":"test1234@",
                                    "name":"test1"
                                }
                                """))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(memberService, times(1)).updateMember(1L, "test1234@", "test1");
    }

    @Test
    @DisplayName("회원정보수정 성공 테스트 - password 공백")
    void updateMembersPasswordEmtpySuccessTest() throws Exception {
        doNothing().when(memberService).updateMember(1L, "", "test1");

        mockMvc.perform(put("/api/account/members/me")
                        .header("X-USER-ID", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "password":"",
                                    "name":"test1"
                                }
                                """))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(memberService, times(1)).updateMember(1L, "", "test1");
    }

    @Test
    @DisplayName("회원정보수정 실패 테스트")
    void updateMembersFailTest() throws Exception {
        doThrow(new MemberAlreadyTerminateException(ErrorCode.MEMBER_TERMINATED)).when(memberService).updateMember(1L, "test1234@", "test1");

        mockMvc.perform(put("/api/account/members/me")
                        .header("X-USER-ID", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "password":"test1234@",
                                    "name":"test1"
                                }
                                """))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.code").value("A011"))
                .andExpect(jsonPath("$.message").value("이미 탈퇴한 회원 입니다."));

        verify(memberService, times(1)).updateMember(1L, "test1234@", "test1");
    }

    @Test
    @DisplayName("회원 휴면 해제 성공 테스트")
    void activeMembersSuccessTest() throws Exception {
        doNothing().when(memberService).activateMember(1L);

        mockMvc.perform(put("/api/account/members/1/active"))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(memberService, times(1)).activateMember(1L);
    }

    @Test
    @DisplayName("회원 휴면 해제 실패 테스트")
    void activeMembersFailTest() throws Exception {
        doThrow(new MemberAlreadyTerminateException(ErrorCode.MEMBER_TERMINATED)).when(memberService).activateMember(1L);

        mockMvc.perform(put("/api/account/members/1/active"))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.code").value("A011"))
                .andExpect(jsonPath("$.message").value("이미 탈퇴한 회원 입니다."));

        verify(memberService, times(1)).activateMember(1L);
    }

    @Test
    @DisplayName("회원 탈퇴 성공 테스트")
    void deleteMembersSuccessTest() throws Exception {
        doNothing().when(memberService).deleteMember(1L);

        mockMvc.perform(delete("/api/account/members/me/withdraw")
                        .header("X-USER-ID", 1L))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(memberService, times(1)).deleteMember(1L);
    }

    @Test
    @DisplayName("회원 이름 반환 성공 테스트")
    void getMemberNameSuccessTest() throws Exception {
        Member member = new Member("test@test.com", "test1234!", "test");

        when(memberQueryService.getMember(1L)).thenReturn(member);

        mockMvc.perform(get("/api/account/members/me/name")
                        .header("X-USER-ID", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("test"));

        verify(memberQueryService, times(1)).getMember(1L);
    }

    @Test
    @DisplayName("회원 이름 반환 실패 테스트")
    void getMemberNameFailTest() throws Exception {
        doThrow(new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND)).when(memberQueryService).getMember(1L);

        mockMvc.perform(get("/api/account/members/me/name")
                        .header("X-USER-ID", 1L))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.code").value("A001"))
                .andExpect(jsonPath("$.message").value("존재하지 않는 사용자 입니다."));

        verify(memberQueryService, times(1)).getMember(1L);
    }
}
