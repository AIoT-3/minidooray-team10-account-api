package com.nhnacademy.accountapi.exception;

public class MemberAlreadyTerminateException extends RuntimeException {
    public MemberAlreadyTerminateException(String message) {
        super(message);
    }
}
