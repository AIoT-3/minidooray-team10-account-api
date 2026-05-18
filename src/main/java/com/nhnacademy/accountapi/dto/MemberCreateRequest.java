package com.nhnacademy.accountapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record MemberCreateRequest(
        @NotBlank
        @Email
        @Pattern(
                regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.(com|net|org|co\\.kr)$"
        )
        String email,
        @NotBlank
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*]).{8,20}$"
        )
        String password,
        @NotBlank
        @Length(max = 20)
        String name
)
{

}