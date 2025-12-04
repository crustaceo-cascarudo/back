package com.fpmislata.back.persitence.repository.impl;

import java.util.Optional;

import com.fpmislata.back.domain.repository.UserRepository;
import com.fpmislata.back.domain.repository.entity.UserEntity;
import com.fpmislata.back.persitence.dao.UserDao;
import com.fpmislata.back.persitence.dao.impl.entity.UserJpaEntity;
import com.fpmislata.back.persitence.repository.mapper.UserMapper;

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
        UserJpaEntity jpaEntity = userDao.getById(id);
        UserEntity entity = UserMapper.getInstance().fromUserJpaEntityfromJpaEntity(jpaEntity);
        return Optional.ofNullable(entity);
    }

    @Override
    public Optional<UserEntity> findByName(String name) {
        UserJpaEntity jpaEntity = userDao.getByName(name);
        UserEntity entity = UserMapper.getInstance().fromUserJpaEntityfromJpaEntity(jpaEntity);
        return Optional.ofNullable(entity);
    }

    @Override
    public UserEntity logByName(String name) {
        UserJpaEntity jpaEntity = userDao.getByName(name);
        return UserMapper.getInstance().fromUserJpaEntityfromJpaEntity(jpaEntity);
    }

    @Override
    public Long save(UserEntity userEntity) {
        UserJpaEntity jpaEntity = UserMapper.getInstance().fromUserEntitytoJpaEntity(userEntity);        
        if (userEntity.id() != null) {
            UserJpaEntity existingEntity = userDao.getById(userEntity.id());
            if (existingEntity != null) {
                userDao.update(jpaEntity);
                return userEntity.id();
            }
        }
        return userDao.insert(jpaEntity);
    }

    @Override
    public String createSessionToken(Long userId) {
        return userDao.createSessionToken(userId);
    }

}
