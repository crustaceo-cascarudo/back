package com.fpmislata.back.persistence.repository.impl;

import java.util.List;
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
    public List<UserEntity> findAll() {
        return userDao.findAll(0, 5).stream()
                .map(UserMapper.getInstance()::fromUserJpaEntitytoUserEntity)
                .toList();
    }

    @Override
    public Optional<UserEntity> findById(Long id) {
        UserJpaEntity jpaEntity = userDao.findById(id).orElse(null);
        UserEntity entity = UserMapper.getInstance().fromUserJpaEntitytoUserEntity(jpaEntity);
        return Optional.ofNullable(entity);
    }

    @Override
    public List<UserEntity> findByName(String name) {
        List<UserJpaEntity> jpaEntities = userDao.findByName(name).
                stream().toList();
        if (jpaEntities.isEmpty()) {
            return List.of();
        }
        return jpaEntities.stream()
                .map(UserMapper.getInstance()::fromUserJpaEntitytoUserEntity)
                .toList();
    }

    @Override
    public UserEntity logByName(String name) {
        List<UserJpaEntity> jpaEntities = userDao.findByName(name).
                stream().toList();
        if (jpaEntities.isEmpty()) {
            return null;
        }
        return UserMapper.getInstance().fromUserJpaEntitytoUserEntity(jpaEntities.get(0));
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
        return UserMapper.getInstance().fromUserJpaEntitytoUserEntity(
                userDao.insert(jpaEntity));
    }

    @Override
    public String createSessionToken(Long userId) {
        return userDao.createSessionToken(userId);
    }

    @Override
    public UserEntity findByToken(String token) {
        UserJpaEntity jpaEntity = userDao.findByToken(token);
        return UserMapper.getInstance().fromUserJpaEntitytoUserEntity(jpaEntity);
    }

    

}
