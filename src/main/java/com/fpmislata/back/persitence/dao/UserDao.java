package com.fpmislata.back.persitence.dao;

import com.fpmislata.back.persitence.dao.impl.entity.UserJpaEntity;

public interface UserDao extends GenericDao<UserJpaEntity> {
    UserJpaEntity getByName(String name);

    String createSessionToken(Long userId);
}