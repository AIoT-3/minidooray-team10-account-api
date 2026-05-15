package com.nhnacademy.accountapi.exception;

public class DuplicateEmailException extends ConflictException {
    public DuplicateEmailException(String message) {
        super(message);
    }
}
