package com.nhnacademy.accountapi.dto;

public record MemberLoginResponse(
        long id,
        String email,
        String password
) {
}
