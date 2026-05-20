package com.nhnacademy.accountapi.repository;

import com.nhnacademy.accountapi.entity.Member;
import com.nhnacademy.accountapi.entity.Status;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsMemberByEmail(String email);

    Optional<Member> findMemberByEmail(String email);

    List<Member> findMembersByLastLoginAtBeforeAndStatus(LocalDateTime localDateTime, Status status);

    Optional<Member> findMEmberByEmailAndStatus(String email, Status status);
}
