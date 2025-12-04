package com.fpmislata.back.persistence.dao;

import com.fpmislata.back.persistence.dao.impl.entity.UserJpaEntity;

public interface UserDao extends GenericDao<UserJpaEntity> {
    UserJpaEntity getByName(String name);

    String createSessionToken(Long userId);
}