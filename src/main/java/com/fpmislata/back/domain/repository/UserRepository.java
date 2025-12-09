package com.fpmislata.back.domain.repository;

import java.util.List;
import java.util.Optional;

import com.fpmislata.back.domain.repository.entity.UserEntity;

public interface UserRepository {
    UserEntity save(UserEntity userEntity);
    List<UserEntity> findAll();
    Optional<UserEntity> findById(Long id);
    Optional<UserEntity> findByName(String name);
    UserEntity logByName(String name);
    void delete(Long id);
    String createSessionToken(Long userId);
}
