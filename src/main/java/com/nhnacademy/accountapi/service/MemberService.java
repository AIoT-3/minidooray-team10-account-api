package com.nhnacademy.accountapi.service;

import com.nhnacademy.accountapi.entity.Member;

public interface MemberService {

    Member getMember(long id);

    void createMember(String email, String password, String name);

    void updateMember(long id, String password, String name);

    void deleteMember(long id);

    void disableMember(long id);

    void activateMember(long id);
}
