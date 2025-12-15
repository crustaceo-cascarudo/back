package com.fpmislata.back.persistence.dao.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

import com.fpmislata.back.persistence.dao.UserDao;
import com.fpmislata.back.persistence.dao.impl.entity.SessionTokenJpaEntity;
import com.fpmislata.back.persistence.dao.impl.entity.UserJpaEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class UserDaoJpa implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void delete(Long id) {
        UserJpaEntity entity = entityManager.find(UserJpaEntity.class, id);
        if (entity != null) {
            entityManager.remove(entity);
        }
    }

    @Override
    public List<UserJpaEntity> findByName(String name) {
        String jpql = "SELECT u FROM UserJpaEntity u WHERE LOWER(u.name) LIKE LOWER(:name)";
        return entityManager.createQuery(jpql, UserJpaEntity.class)
                .setParameter("name", "%" + name + "%")
                .getResultList();
    }

    @Override
    public Optional<UserJpaEntity> findById(Long id) {
        return Optional.ofNullable(entityManager.find(UserJpaEntity.class, id));
    }

    @Override
    @Transactional
    public UserJpaEntity insert(UserJpaEntity userJpaEntity) {
        entityManager.persist(userJpaEntity);
        entityManager.flush();
        return userJpaEntity;
    }

    @Override
    @Transactional
    public UserJpaEntity update(UserJpaEntity userJpaEntity) {
        return entityManager.merge(userJpaEntity);
    }

    @Override
    @Transactional
    public String createSessionToken(Long userId) {
        String token = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();

        SessionTokenJpaEntity sessionToken = new SessionTokenJpaEntity(token, userId, now);

        entityManager.persist(sessionToken);
        entityManager.flush();

        return token;
    }

    @Override
    public UserJpaEntity findByToken(String token) {
        String jpql = "SELECT u FROM UserJpaEntity u JOIN SessionTokenJpaEntity s ON u.id = s.userId WHERE s.token = :token";
        List<UserJpaEntity> users = entityManager.createQuery(jpql, UserJpaEntity.class)
                .setParameter("token", token)
                .getResultList();
        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    @Transactional
    public void deleteToken(String token) {
        SessionTokenJpaEntity sessionToken = entityManager.find(SessionTokenJpaEntity.class, token);
        if (sessionToken != null) {
            entityManager.remove(sessionToken);
        }
    }

    @Override
    public long count() {
        return entityManager.createQuery("SELECT COUNT(u) FROM UserJpaEntity u", Long.class)
                .getSingleResult();
    }

    @Override
    public List<UserJpaEntity> findAll(int pageNumber, int pageSize) {
        int pageIndex = Math.max(pageNumber - 1, 0);
        return entityManager.createQuery("SELECT u FROM UserJpaEntity u", UserJpaEntity.class)
                .setFirstResult(pageIndex * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

}
