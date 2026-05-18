package com.nhnacademy.accountapi.dto;

import com.nhnacademy.accountapi.entity.Status;

public record MemberLoginResponse(
        long id,
        String email,
        String password,
        Status status
) {
}
