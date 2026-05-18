package com.nhnacademy.accountapi.exception;

public class MemberNotFoundException extends BaseException {
    public MemberNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
