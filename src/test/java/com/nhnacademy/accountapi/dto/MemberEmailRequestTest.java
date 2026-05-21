package com.nhnacademy.accountapi.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class MemberEmailRequestTest {

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    @DisplayName("MemberEmailRequeset 생성 성공 테스트")
    void memberEmailRequestSuccessTest() {
        MemberEmailRequest memberEmailRequest = new MemberEmailRequest("test@test.com");

        assertThat(memberEmailRequest.getEmail()).isEqualTo("test@test.com");
    }

    @Test
    @DisplayName("MemberEmailRequeset 생성 실패 테스트")
    void memberEmailRequestFailTest() {
        MemberEmailRequest memberEmailRequest = new MemberEmailRequest("test@test");

        Set<ConstraintViolation<MemberEmailRequest>> validation = validator.validate(memberEmailRequest);
        assertThat(validation)
                .extracting(v -> v.getPropertyPath().toString())
                .contains("email");

        memberEmailRequest = new MemberEmailRequest("test@testcom");
        validation = validator.validate(memberEmailRequest);
        assertThat(validation)
                .extracting(v -> v.getPropertyPath().toString())
                .contains("email");

        memberEmailRequest = new MemberEmailRequest("testtest.com");
        validation = validator.validate(memberEmailRequest);
        assertThat(validation)
                .extracting(v -> v.getPropertyPath().toString())
                .contains("email");

        memberEmailRequest = new MemberEmailRequest("test@@test.com");
        validation = validator.validate(memberEmailRequest);
        assertThat(validation)
                .extracting(v -> v.getPropertyPath().toString())
                .contains("email");

        memberEmailRequest = new MemberEmailRequest("");
        validation = validator.validate(memberEmailRequest);
        assertThat(validation)
                .extracting(v -> v.getPropertyPath().toString())
                .contains("email");
    }
}
