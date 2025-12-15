package com.fpmislata.back.persistence.dao.impl.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "session")
public class SessionTokenJpaEntity {

    @Id
    @Column(name = "token", nullable = false, unique = true)
    private String token;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public SessionTokenJpaEntity() {
    }

    public SessionTokenJpaEntity(String token, Long userId, LocalDateTime createdAt) {
        this.token = token;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    public String getToken() {
        return token;
    }

    public Long getUserId() {
        return userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
