package com.nhnacademy.accountapi.service;

public interface MemberService {
    void createMember(String email, String password, String name);

    void updateMember(long id, String password, String name);

    void deleteMember(long id);

    void disableMember(long id);

    void activateMember(long id);
}
