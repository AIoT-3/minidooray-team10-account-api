package com.nhnacademy.accountapi.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record MemberIdNameRequest(
        @NotNull
        List<Long> ids
){
}
