package com.fpmislata.back.persistence.repository.mapper;

import com.fpmislata.back.domain.enumerado.Role;
import com.fpmislata.back.domain.repository.entity.UserEntity;
import com.fpmislata.back.persistence.dao.impl.entity.UserJpaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = UserMapper.getInstance();
    }

    @Test
    void testFromUserEntitytoJpaEntity() {
        UserEntity userEntity = new UserEntity(1L, "testUser", "hashedPassword", Role.NORMAL);
        UserJpaEntity jpaEntity = userMapper.fromUserEntitytoJpaEntity(userEntity);

        assertNotNull(jpaEntity);
        assertEquals(userEntity.id(), jpaEntity.getId());
        assertEquals(userEntity.name(), jpaEntity.getName());
        assertEquals(userEntity.passwordHash(), jpaEntity.getPasswordHash());
        assertEquals(userEntity.role(), jpaEntity.getRole());
    }

    @Test
    void testFromUserEntitytoJpaEntityWithNullUserEntity() {
        assertNull(userMapper.fromUserEntitytoJpaEntity(null));
    }

    @Test
    void testFromUserJpaEntitytoUserEntity() {
        UserJpaEntity jpaEntity = new UserJpaEntity(1L, "testJpa", "jpaHash", Role.ADMIN);
        UserEntity userEntity = userMapper.fromUserJpaEntitytoUserEntity(jpaEntity);

        assertNotNull(userEntity);
        assertEquals(jpaEntity.getId(), userEntity.id());
        assertEquals(jpaEntity.getName(), userEntity.name());
        assertEquals(jpaEntity.getPasswordHash(), userEntity.passwordHash());
        assertEquals(jpaEntity.getRole(), userEntity.role());
    }

    @Test
    void testFromUserJpaEntitytoUserEntityWithNullJpaEntity() {
        assertNull(userMapper.fromUserJpaEntitytoUserEntity(null));
    }
}