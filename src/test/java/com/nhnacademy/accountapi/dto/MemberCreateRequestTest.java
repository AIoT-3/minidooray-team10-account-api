package com.nhnacademy.accountapi.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class MemberCreateRequestTest {

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    @DisplayName("MemberCreateRequest 생성 테스트")
    void newMemberCreateRequest() {
        MemberCreateRequest memberCreateRequest = new MemberCreateRequest("test@test.com", "test1234!", "test");

        Set<ConstraintViolation<MemberCreateRequest>> violations = validator.validate(memberCreateRequest);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("이메일 validate 실패 테스트")
    void rquestEmailFailTest() {
        MemberCreateRequest memberCreateRequest = new MemberCreateRequest("test@", "test1234!", "test");

        Set<ConstraintViolation<MemberCreateRequest>> violations = validator.validate(memberCreateRequest);

        assertThat(violations)
                .extracting(v -> v.getPropertyPath().toString())
                .contains("email");
    }

    @Test
    @DisplayName("패스워드 validate 실패 테스트")
    void rquestPasswordFailTest() {
        MemberCreateRequest memberCreateRequest = new MemberCreateRequest("test@test.com", "test!", "test");

        Set<ConstraintViolation<MemberCreateRequest>> violations = validator.validate(memberCreateRequest);

        assertThat(violations)
                .extracting(v -> v.getPropertyPath().toString())
                .contains("password");

        memberCreateRequest = new MemberCreateRequest("test@test.com", "test123412", "test");

        violations = validator.validate(memberCreateRequest);

        assertThat(violations)
                .extracting(v -> v.getPropertyPath().toString())
                .contains("password");
    }

    @Test
    @DisplayName("이름 validate 실패 테스트")
    void rquestNameFailTest() {
        MemberCreateRequest memberCreateRequest = new MemberCreateRequest("test@test.com", "test1234!", "");

        Set<ConstraintViolation<MemberCreateRequest>> violations = validator.validate(memberCreateRequest);

        assertThat(violations)
                .extracting(v -> v.getPropertyPath().toString())
                .contains("name");

        memberCreateRequest = new MemberCreateRequest("test@test.com", "test1234!", "testestestestsetsetsetsetsetsets");

        violations = validator.validate(memberCreateRequest);

        assertThat(violations)
                .extracting(v -> v.getPropertyPath().toString())
                .contains("name");
    }

}
