package com.nhnacademy.accountapi.exception;

public class MemberAlreadyTerminateException extends BaseException {
    public MemberAlreadyTerminateException(ErrorCode errorCode) {
        super(errorCode);
    }
}
