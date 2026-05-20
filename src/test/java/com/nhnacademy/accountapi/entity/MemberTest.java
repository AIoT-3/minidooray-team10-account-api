package com.nhnacademy.accountapi.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class MemberTest {

    @Test
    @DisplayName("Member 생성 성공")
    void newMemberSuccessTest() {
        Member member = new Member("test@test.com", "test1234!", "test");

        Assertions.assertEquals("test@test.com", member.getEmail());
        Assertions.assertEquals("test1234!", member.getPassword());
        Assertions.assertEquals("test", member.getName());
        Assertions.assertEquals(Status.ACTIVE, member.getStatus());
    }

    @Test
    @DisplayName("Member 업데아트")
    void updateMemberTest() {
        Member member = new Member("test@test.com", "test1234!", "test");

        member.updatePassword("testtest!");
        Assertions.assertEquals("testtest!", member.getPassword());

        member.updateName("test2");
        Assertions.assertEquals("test2", member.getName());

        member.updateStatus(Status.TERMINATE);
        Assertions.assertEquals(Status.TERMINATE, member.getStatus());

        member.updatePasswordAndName("test1234!", "test");
        Assertions.assertEquals("test1234!", member.getPassword());
        Assertions.assertEquals("test", member.getName());

        member.updateLastLoginAt(LocalDateTime.now());
        Assertions.assertTrue(member.getLastLoginAt().isBefore(LocalDateTime.now()));

    }

}
