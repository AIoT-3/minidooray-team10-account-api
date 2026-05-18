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

@Validated
@RestController
@RequestMapping("/api")
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
    @GetMapping("/account")
    public ResponseEntity<MemberLoginResponse> loginMember(@RequestParam @Email String email) {
        Member member = memberQueryService.getMemberByEmail(email);

        return ResponseEntity.ok(new MemberLoginResponse(member.getId(), member.getEmail(), member.getPassword(), member.getStatus()));
    }

    //회원정보조회
    @GetMapping("/members")
    public ResponseEntity<MembersResponse> getMembers(@RequestHeader(name = HEADERUSERID) @Positive long memberId) {
        Member member = memberQueryService.getMember(memberId);

        return ResponseEntity.ok(new MembersResponse(member.getEmail(), member.getName()));
    }

    //회원정보수정
    @PutMapping("/members")
    public ResponseEntity<Void> updateMembers(@RequestHeader(name = HEADERUSERID) @Positive long memberId,
                                              @RequestBody @Valid MemberUpdateRequest memberUpdateRequest) {
        memberService.updateMember(memberId,
                passwordEncoder.encode(memberUpdateRequest.password()),
                memberUpdateRequest.name());

        return ResponseEntity.ok().build();
    }

    //휴면전환
    @PutMapping("/members/{memberId}/inactive")
    public ResponseEntity<Void> inactiveMembers(@PathVariable(name = "memberId") @Positive long memberId) {
        memberService.disableMember(memberId);

        return ResponseEntity.ok().build();
    }

    //휴면해제
    @PutMapping("/members/{memberId}/active")
    public ResponseEntity<Void> activeMembers(@PathVariable(name = "memberId") @Positive long memberId) {
        memberService.activateMember(memberId);

        return ResponseEntity.ok().build();
    }

    //회원탈퇴
    @DeleteMapping("/members")
    public ResponseEntity<Void> deleteMembers(@RequestHeader(name = HEADERUSERID) @Positive long memberId) {
        memberService.deleteMember(memberId);

        return ResponseEntity.noContent().build();
    }

    //회원이름반환
    @GetMapping("/member")
    public ResponseEntity<MemberNameResponse> getMemberName(@RequestHeader(name = HEADERUSERID) @Positive long memberId) {
        Member member = memberQueryService.getMember(memberId);

        return ResponseEntity.ok(new MemberNameResponse(member.getName()));
    }
}
