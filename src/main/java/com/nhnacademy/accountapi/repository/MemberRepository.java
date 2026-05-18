package com.nhnacademy.accountapi.repository;

import com.nhnacademy.accountapi.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsMemberByEmail(String email);

    Optional<Member> findMemberByEmail(String email);
}
