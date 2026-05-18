package com.nhnacademy.accountapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record MemberUpdateRequest (
        @Pattern(
                regexp = "^$|^(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*]).{8,20}$"
        )
        String password,
        @NotBlank
        @Length(max = 20)
        String name
)
{
}
