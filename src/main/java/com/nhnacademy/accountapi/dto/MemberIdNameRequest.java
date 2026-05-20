package com.nhnacademy.accountapi.dto;

import java.util.List;

public record MemberIdNameRequest(
        List<Long> ids
){
}
