package com.fpmislata.back.persistence.dao.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.fpmislata.back.persistence.dao.UserDao;
import com.fpmislata.back.persistence.dao.impl.entity.SessionTokenJpaEntity;
import com.fpmislata.back.persistence.dao.impl.entity.UserJpaEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

public class UserDaoJpa implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void delete(Long id) {
        UserJpaEntity entity = entityManager.find(UserJpaEntity.class, id);
        if (entity != null) {
            entityManager.remove(entity);
        }
    }

    @Override
    public Optional<UserJpaEntity> findByName(String name) {
        try {
            String jpql = "SELECT u FROM UserJpaEntity u WHERE u.name = :name";
            return Optional.of(entityManager.createQuery(jpql, UserJpaEntity.class)
                    .setParameter("name", name)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<UserJpaEntity> findById(Long id) {
        return Optional.ofNullable(entityManager.find(UserJpaEntity.class, id));
    }

    @Override
    public UserJpaEntity insert(UserJpaEntity userJpaEntity) {
        entityManager.persist(userJpaEntity);
        entityManager.flush();
        return userJpaEntity;
    }

    @Override
    public UserJpaEntity update(UserJpaEntity userJpaEntity) {
        return entityManager.merge(userJpaEntity);
    }

    @Override
    public String createSessionToken(Long userId) {
        String token = UUID.randomUUID().toString();

        SessionTokenJpaEntity sessionToken = new SessionTokenJpaEntity(null, token, userId);

        entityManager.persist(sessionToken);
        entityManager.flush();

        return token;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public List<UserJpaEntity> findAll(int pageNumber, int pageSize) {
        return null;
    }

    

}
