package com.nhnacademy.accountapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    NOT_VALID_PARAMETER(HttpStatus.BAD_REQUEST, "M001", "요청 헤더가 올바르지 않습니다."),
    NOT_VALID_BODY(HttpStatus.BAD_REQUEST, "M002", "요청 본문이 올바르지 않습니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "A001", "존재하지 않는 사용자 입니다."),
    MEMBER_NOT_ACTIVE(HttpStatus.NOT_FOUND, "A002", "활동상태가 아닌 사용자 입니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "A010", "이미 존재하는 이메일입니다."),
    MEMBER_TERMINATED(HttpStatus.CONFLICT, "A011", "이미 탈퇴한 회원 입니다."),
    UNHANDLED_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "S500", "서버 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
