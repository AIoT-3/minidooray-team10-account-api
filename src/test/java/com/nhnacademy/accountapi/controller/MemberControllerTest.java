package com.nhnacademy.accountapi.controller;

import com.nhnacademy.accountapi.entity.Member;
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
        mockMvc.perform(post("/api/signup")
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
    }

    @Test
    @DisplayName("회원 가입 실패 테스트")
    void createMembersFailTest() throws Exception {
        mockMvc.perform(post("/api/signup")
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


        mockMvc.perform(post("/api/signup")
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


        mockMvc.perform(post("/api/signup")
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
    }

    @Test
    @DisplayName("회원 조회 성공 테스트")
    void getMembersSuccessTest() throws Exception {
        Member member = new Member("test@test.com", "test1234!", "test");

        when(memberQueryService.getMember(1L)).thenReturn(member);

        mockMvc.perform(get("/api/members")
                        .header("memberId", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@test.com"))
                .andExpect(jsonPath("$.name").value("test"));
    }

    @Test
    @DisplayName("회원 수정 성공 테스트")
    void updateMembersSuccessTest() throws Exception {
        Member member = new Member("test@test.com", "test1234!", "test");

        when(memberQueryService.getMember(1L)).thenReturn(member);

        mockMvc.perform(put("/api/members")
                        .header("memberId", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "password":"test1234@",
                                    "name":"test1"
                                }
                                """))
                .andDo(print())
                .andExpect(status().isOk());

        verify(memberService, times(1)).updateMember(1L, "test1234@", "test1");
    }

    @Test
    @DisplayName("회원 휴면 해제 성공 테스트")
    void activeMembersSuccessTest() throws Exception {
        Member member = new Member("test@test.com", "test1234!", "test");

        when(memberQueryService.getMember(1L)).thenReturn(member);

        mockMvc.perform(put("/api/members/1/active"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(memberService, times(1)).activateMember(1L);
    }

    @Test
    @DisplayName("회원 탈퇴 성공 테스트")
    void deleteMembersSuccessTest() throws Exception {
        Member member = new Member("test@test.com", "test1234!", "test");

        when(memberQueryService.getMember(1L)).thenReturn(member);

        mockMvc.perform(delete("/api/members")
                        .header("memberId", 1L))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(memberService, times(1)).deleteMember(1L);
    }

    @Test
    @DisplayName("회원 이름 반환 성공 테스트")
    void getMemberNameSuccessTest() throws Exception {
        Member member = new Member("test@test.com", "test1234!", "test");

        when(memberQueryService.getMember(1L)).thenReturn(member);

        mockMvc.perform(get("/api/member")
                        .header("memberId", 1L))
                .andDo(print())
                .andExpect(status().isOk());

        verify(memberQueryService, times(1)).getMember(1L);
    }
}
