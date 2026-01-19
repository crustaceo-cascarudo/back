package com.fpmislata.back.persistence.dao;

import java.util.List;

import com.fpmislata.back.persistence.dao.impl.entity.UserJpaEntity;

public interface UserDao extends GenericDao<UserJpaEntity> {
  String createSessionToken(Long userId);

  UserJpaEntity findByToken(String token);

  void deleteToken(String token);

  List<UserJpaEntity> findByEmail(String email);
}
