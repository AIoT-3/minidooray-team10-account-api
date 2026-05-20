package com.nhnacademy.accountapi.dto;

import jakarta.validation.constraints.NotBlank;

public record MemberListRequest (
        @NotBlank
        Long id
){
}
