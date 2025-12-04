package com.fpmislata.back.persitence.repository.mapper;

import com.fpmislata.back.domain.repository.entity.UserEntity;
import com.fpmislata.back.persitence.dao.impl.entity.UserJpaEntity;

public class UserMapper {

    private static UserMapper instance;

    private UserMapper() {
    }

    public static UserMapper getInstance() {
        if (instance == null) {
            instance = new UserMapper();
        }
        return instance;
    }

    public UserJpaEntity fromUserEntitytoJpaEntity(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }
        return new UserJpaEntity(
                userEntity.id(),
                userEntity.name(),
                userEntity.passwordHash(),
                userEntity.role());
    }

    public UserEntity fromUserJpaEntityfromJpaEntity(UserJpaEntity jpaEntity) {
        if (jpaEntity == null) {
            return null;
        }
        return new UserEntity(
                jpaEntity.getId(),
                jpaEntity.getName(),
                jpaEntity.getPasswordHash(),
                jpaEntity.getRole());
    }
}
