package com.nhnacademy.accountapi.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class MemberUpdateRequestTest {

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    @DisplayName("MemberUpdateRequest 생성 테스트")
    void requestCreateSuccessTest() {
        MemberUpdateRequest memberUpdateRequest = new MemberUpdateRequest("test1234!", "test");

        Set<ConstraintViolation<MemberUpdateRequest>> violation = validator.validate(memberUpdateRequest);

        assertThat(violation).isEmpty();
    }

    @Test
    @DisplayName("password 실패 테스트")
    void requestEmailFailTest() {
        MemberUpdateRequest memberUpdateRequest = new MemberUpdateRequest("test123412", "test");
        Set<ConstraintViolation<MemberUpdateRequest>> violation = validator.validate(memberUpdateRequest);
        assertThat(violation)
                .extracting(v -> v.getPropertyPath().toString())
                .contains("password");

        memberUpdateRequest = new MemberUpdateRequest("tt1234!", "test");
        violation = validator.validate(memberUpdateRequest);
        assertThat(violation)
                .extracting(v -> v.getPropertyPath().toString())
                .contains("password");

        memberUpdateRequest = new MemberUpdateRequest("test1234!test1234!test1234!test1234!test1234!test1234!test1234!test1234!", "test");
        violation = validator.validate(memberUpdateRequest);
        assertThat(violation)
                .extracting(v -> v.getPropertyPath().toString())
                .contains("password");

        memberUpdateRequest = new MemberUpdateRequest("", "test");
        violation = validator.validate(memberUpdateRequest);
        assertThat(violation)
                .extracting(v -> v.getPropertyPath().toString())
                .contains("password");
    }

    @Test
    @DisplayName("name 실패 테스트")
    void requestNameFailTest() {
        MemberUpdateRequest memberUpdateRequest = new MemberUpdateRequest("test1234!", "");
        Set<ConstraintViolation<MemberUpdateRequest>> violation = validator.validate(memberUpdateRequest);
        assertThat(violation)
                .extracting(v -> v.getPropertyPath().toString())
                .contains("name");

        memberUpdateRequest = new MemberUpdateRequest("test1234!", "testestestetsetesetsetst");
        violation = validator.validate(memberUpdateRequest);
        assertThat(violation)
                .extracting(v -> v.getPropertyPath().toString())
                .contains("name");
    }
}