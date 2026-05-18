package com.nhnacademy.accountapi.exception;

public class DuplicateEmailException extends BaseException {
    public DuplicateEmailException(ErrorCode errorCode) {
        super(errorCode);
    }
}
