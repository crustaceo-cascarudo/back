package com.fpmislata.back.domain.repository;

import java.util.Optional;

import com.fpmislata.back.domain.repository.entity.UserEntity;

public interface UserRepository {
    Long save(UserEntity userEntity);
    Optional<UserEntity> findById(Long id);
    Optional<UserEntity> findByName(String name);
    UserEntity logByName(String name);
    void delete(Long id);
    String createSessionToken(Long userId);
}
