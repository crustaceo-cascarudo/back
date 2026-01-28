package com.fpmislata.back.domain.repository;

import java.util.List;
import java.util.Optional;

import com.fpmislata.back.domain.model.Page;
import com.fpmislata.back.domain.repository.entity.UserEntity;

public interface UserRepository {
  UserEntity save(UserEntity userEntity);

  Page<UserEntity> findAll(int page, int size);

  Optional<UserEntity> findById(Long id);

  List<UserEntity> findByName(String name);

  List<UserEntity> findByEmail(String email);

  UserEntity logByEmail(String email);

  void delete(Long id);

  String createSessionToken(Long userId);

  UserEntity findByToken(String token);

  void deleteSessionToken(String token);
}
