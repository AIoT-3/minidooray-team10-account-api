package com.nhnacademy.accountapi.dto;

import com.nhnacademy.accountapi.entity.Status;

public record MembersResponse (
        String email,
        String password,
        String name,
        Status status
){
}
