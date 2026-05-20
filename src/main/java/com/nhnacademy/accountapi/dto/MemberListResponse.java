package com.nhnacademy.accountapi.dto;

import java.util.List;

public record MemberListResponse (
    List<MemberIdNameResponse> data
) {
}
