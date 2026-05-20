package com.nhnacademy.accountapi.controller;

import com.nhnacademy.accountapi.dto.*;
import com.nhnacademy.accountapi.entity.Member;
import com.nhnacademy.accountapi.service.MemberQueryService;
import com.nhnacademy.accountapi.service.MemberService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class MemberController {

    private static final String HEADERUSERID = "X-USER-ID";

    private final MemberService memberService;
    private final MemberQueryService memberQueryService;

    private final PasswordEncoder passwordEncoder;

    //회원가입
    @PostMapping("/signup")
    public ResponseEntity<Void> createMember(@RequestBody @Valid MemberCreateRequest memberCreateRequest) {
        memberService.createMember(memberCreateRequest.email(),
                passwordEncoder.encode(memberCreateRequest.password()),
                memberCreateRequest.name());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //회원로그인조회
    @GetMapping("/members/by-email")
    public ResponseEntity<MemberLoginResponse> loginMember(@RequestParam(name = "email") @Email String email) {
        Member member = memberQueryService.getMemberByEmail(email);

        return ResponseEntity.ok(new MemberLoginResponse(member.getId(), member.getEmail(), member.getPassword(), member.getStatus()));
    }

    //로그인 성공
    @PatchMapping("/members/me")
    public ResponseEntity<Void> loginMembers(@RequestHeader(name = HEADERUSERID) @Positive long memberId) {
        memberService.updateLastLoginAt(memberId);

        return ResponseEntity.noContent().build();
    }

    //회원정보조회
    @GetMapping("/members/me")
    public ResponseEntity<MembersEmailNameResponse> getMembers(@RequestHeader(name = HEADERUSERID) @Positive long memberId) {
        Member member = memberQueryService.getMember(memberId);

        return ResponseEntity.ok(new MembersEmailNameResponse(member.getEmail(), member.getName()));
    }

    //회원정보수정
    // /api/members/{member-id}
    // /api/members/me  id가 되는게 그러면 이렇게
    @PutMapping("/members/me")
    public ResponseEntity<Void> updateMembers(@RequestHeader(name = HEADERUSERID) @Positive long memberId,
                                              @RequestBody @Valid MemberUpdateRequest memberUpdateRequest) {
        memberService.updateMember(memberId,
                memberUpdateRequest.password(),
                memberUpdateRequest.name());

        return ResponseEntity.noContent().build();  //noContent()
    }

    //휴면해제
    @PutMapping("/members/{member-id}/active")
    public ResponseEntity<Void> activeMembers(@PathVariable(name = "member-id") @Positive long memberId) {
        memberService.activateMember(memberId);

        return ResponseEntity.noContent().build();  //noContent()
    }

    //회원탈퇴
    // /api/members/{member-id}/withdraw
    // 메소드로 표현하기 애매한건 url에 넣어줘도됨
    // /api/members/me/withdraw
    @DeleteMapping("/members/me/withdraw")
    public ResponseEntity<Void> deleteMembers(@RequestHeader(name = HEADERUSERID) @Positive long memberId) {
        memberService.deleteMember(memberId);

        return ResponseEntity.noContent().build();
    }

    //회원이름반환
    // /api/members/{member-id}/name
    // /api/members/me/name
    @GetMapping("/members/me/name")
    public ResponseEntity<MemberNameResponse> getMemberName(@RequestHeader(name = HEADERUSERID) @Positive long memberId) {
        Member member = memberQueryService.getMember(memberId);

        return ResponseEntity.ok(new MemberNameResponse(member.getName()));
    }

    //회원 리스트 반환
    @PostMapping("/members/batch")
    public ResponseEntity<MemberListResponse> getMemberList(@RequestBody @Valid MemberIdNameRequest memberListRequestIdName) {
        List<MemberIdNameResponse> memberIdNameResponseList = memberQueryService.getMembersListById(memberListRequestIdName.ids());

        MemberListResponse memberListResponse = new MemberListResponse(memberIdNameResponseList);

        return ResponseEntity.ok(memberListResponse);
    }

    //회원 id반환
    @PostMapping("/members/id")
    public ResponseEntity<MemberIdResponse> getMemberById(@RequestBody @Valid MemberEmailRequest memberEmailRequest) {
        Member member = memberQueryService.getMemberByEmail(memberEmailRequest.getEmail());

        MemberIdResponse memberIdResponse = new MemberIdResponse(member.getId());

        return ResponseEntity.ok(memberIdResponse);
    }
}
