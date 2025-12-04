package com.fpmislata.back.persitence.dao.impl.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "sessions")
public class SessionTokenJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String token;
    @Column(name = "user_id", nullable = false)
    private Long userId;

    public SessionTokenJpaEntity() {
    }

    public SessionTokenJpaEntity(Long id, String token, Long userId) {
        this.id = id;
        this.token = token;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public Long getUserId() {
        return userId;
    }

    
}
