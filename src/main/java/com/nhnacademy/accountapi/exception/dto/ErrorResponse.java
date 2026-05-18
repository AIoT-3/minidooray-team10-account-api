package com.nhnacademy.accountapi.exception.dto;

public record ErrorResponse(
        int status,
        String code,
        String message
) {}