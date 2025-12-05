package com.fpmislata.back.persistence.repository.impl;

import java.util.Optional;

import com.fpmislata.back.domain.repository.UserRepository;
import com.fpmislata.back.domain.repository.entity.UserEntity;
import com.fpmislata.back.persistence.dao.UserDao;
import com.fpmislata.back.persistence.dao.impl.entity.UserJpaEntity;
import com.fpmislata.back.persistence.repository.mapper.UserMapper;

public class UserRepositoryImpl implements UserRepository {

    private final UserDao userDao;

    public UserRepositoryImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void delete(Long id) {
        userDao.delete(id);
    }

    @Override
    public Optional<UserEntity> findById(Long id) {
        UserJpaEntity jpaEntity = userDao.findById(id).orElse(null);
        UserEntity entity = UserMapper.getInstance().fromUserJpaEntityfromEntity(jpaEntity);
        return Optional.ofNullable(entity);
    }

    @Override
    public Optional<UserEntity> findByName(String name) {
        UserJpaEntity jpaEntity = userDao.findByName(name).orElse(null);
        UserEntity entity = UserMapper.getInstance().fromUserJpaEntityfromEntity(jpaEntity);
        return Optional.ofNullable(entity);
    }

    @Override
    public UserEntity logByName(String name) {
        UserJpaEntity jpaEntity = userDao.findByName(name).orElse(null);
        return UserMapper.getInstance().fromUserJpaEntityfromEntity(jpaEntity);
    }

    @Override
    public UserEntity save(UserEntity userEntity) {
        UserJpaEntity jpaEntity = UserMapper.getInstance().fromUserEntitytoJpaEntity(userEntity);        
        if (userEntity.id() != null) {
            UserJpaEntity existingEntity = userDao.findById(userEntity.id()).orElse(null);
            if (existingEntity != null) {
                userDao.update(jpaEntity);
                return userEntity;
            }
        }
        return UserMapper.getInstance().fromUserJpaEntityfromEntity(
            userDao.insert(jpaEntity));
    }

    @Override
    public String createSessionToken(Long userId) {
        return userDao.createSessionToken(userId);
    }

}
