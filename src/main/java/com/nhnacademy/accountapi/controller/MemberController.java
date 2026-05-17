package com.nhnacademy.accountapi.controller;

import com.nhnacademy.accountapi.dto.MemberCreateRequest;
import com.nhnacademy.accountapi.dto.MemberNameResponse;
import com.nhnacademy.accountapi.dto.MemberUpdateRequest;
import com.nhnacademy.accountapi.dto.MembersResponse;
import com.nhnacademy.accountapi.entity.Member;
import com.nhnacademy.accountapi.service.MemberQueryService;
import com.nhnacademy.accountapi.service.MemberService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

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

    //회원정보조회
    @GetMapping("/members")
    public ResponseEntity<MembersResponse> getMembers(@RequestHeader(name = "memberId") @Positive long memberId) {
        Member member = memberQueryService.getMember(memberId);

        return ResponseEntity.ok(new MembersResponse(member.getEmail(), member.getPassword(), member.getName(), member.getStatus()));
    }

    //회원정보수정
    @PutMapping("/members")
    public ResponseEntity<Void> updateMembers(@RequestHeader(name = "memberId") @Positive long memberId,
                                              @RequestBody @Valid MemberUpdateRequest memberUpdateRequest) {
        memberService.updateMember(memberId, memberUpdateRequest.password(), memberUpdateRequest.name());

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
    public ResponseEntity<Void> deleteMembers(@RequestHeader(name = "memberId") @Positive long memberId) {
        memberService.deleteMember(memberId);

        return ResponseEntity.noContent().build();
    }

    //회원이름반환
    @GetMapping("/member")
    public ResponseEntity<MemberNameResponse> getMemberName(@RequestHeader(name = "memberId") @Positive long memberId) {
        Member member = memberQueryService.getMember(memberId);

        return ResponseEntity.ok(new MemberNameResponse(member.getName()));
    }
}
