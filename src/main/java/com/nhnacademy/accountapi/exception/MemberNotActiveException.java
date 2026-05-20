package com.nhnacademy.accountapi.exception;

public class MemberNotActiveException extends BaseException {
    public MemberNotActiveException(ErrorCode errorCode) {
        super(errorCode);
    }
}
