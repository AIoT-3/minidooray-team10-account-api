package com.nhnacademy.accountapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Column(length = 30, nullable = false)
    private String email;

    @NotBlank
    @Column(length = 100, nullable = false)
    private String password;
    @NotBlank
    @Column(length = 20, nullable = false)
    private String name;
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;
    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime lastLoginAt;

    public Member(String email, String password, String name) {

        if (email.isEmpty() || !email.contains("@")) {
            throw new IllegalArgumentException();
        }

        if (name.isEmpty()) {
            throw new IllegalArgumentException();
        }

        this.email = email;
        this.password = password;
        this.name = name;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateName(String name) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException();
        }

        this.name = name;
    }

    public void updateStatus(Status status) {
        this.status = status;
    }

    public void updateLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }
}
