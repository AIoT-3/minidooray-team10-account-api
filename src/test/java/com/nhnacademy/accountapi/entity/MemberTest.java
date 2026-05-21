package com.nhnacademy.accountapi.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

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
    @DisplayName("Member 생성 실패")
    void newMemberFailTest() {
        assertThrows(IllegalArgumentException.class, () ->
                new Member("", "test1234!", "test"));
        assertThrows(IllegalArgumentException.class, () ->
                new Member("test@test.com", "test1234!", ""));
    }

    @Test
    @DisplayName("Member 업데아트 성공")
    void updateMemberSuccessTest() {
        Member member = new Member("test@test.com", "test1234!", "test");

        member.updatePassword("testtest!");
        Assertions.assertEquals("testtest!", member.getPassword());

        member.updateName("test2");
        Assertions.assertEquals("test2", member.getName());

        member.updateStatus(Status.TERMINATE);
        Assertions.assertEquals(Status.TERMINATE, member.getStatus());

        member.updateLastLoginAt(LocalDateTime.now());
        Assertions.assertTrue(member.getLastLoginAt().isBefore(LocalDateTime.now()));
    }

    @Test
    @DisplayName("Member 업데이트 실패")
    void updateMemberFailTest() {
        Member member = new Member("test@test.com", "test1234!", "test");

        assertThrows(IllegalArgumentException.class, () -> {
            member.updateName("");
        });
    }

}
