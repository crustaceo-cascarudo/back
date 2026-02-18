package com.fpmislata.back.domain.repository.entity;

import com.fpmislata.back.domain.enumerado.Role;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserEntityTest {

    @Test
    void testUserEntityCreationAndGetters() {
        Long id = 1L;
        String name = "testUser";
        String passwordHash = "hashedPassword";
        Role role = Role.NORMAL;

        UserEntity userEntity = new UserEntity(id, name, "test@gmail.com", passwordHash, role);

        assertEquals(id, userEntity.id());
        assertEquals(name, userEntity.name());
        assertEquals("test@gmail.com", userEntity.email());
        assertEquals(passwordHash, userEntity.passwordHash());
        assertEquals(role, userEntity.role());
    }

    @Test
    void testUserEntityEquality() {
        UserEntity userEntity1 = new UserEntity(1L, "testUser", "test@gmail.com", "hashedPassword", Role.NORMAL);
        UserEntity userEntity2 = new UserEntity(1L, "testUser", "test@gmail.com", "hashedPassword", Role.NORMAL);
        UserEntity userEntity3 = new UserEntity(2L, "anotherUser", "test@gmail.com", "anotherHash", Role.ADMIN);

        assertEquals(userEntity1, userEntity2);
        assertNotEquals(userEntity1, userEntity3);
        assertNotEquals(null, userEntity1);
        assertNotEquals(new Object(), userEntity1);
    }

    @Test
    void testUserEntityHashCode() {
        UserEntity userEntity1 = new UserEntity(1L, "testUser", "test@gmail.com", "hashedPassword", Role.NORMAL);
        UserEntity userEntity2 = new UserEntity(1L, "testUser", "test@gmail.com", "hashedPassword", Role.NORMAL);
        UserEntity userEntity3 = new UserEntity(2L, "anotherUser", "test@gmail.com", "anotherHash", Role.ADMIN);

        assertEquals(userEntity1.hashCode(), userEntity2.hashCode());
        assertNotEquals(userEntity1.hashCode(), userEntity3.hashCode());
    }

    @Test
    void testUserEntityToString() {
        UserEntity userEntity = new UserEntity(1L, "testUser", "test@gmail.com", "hashedPassword", Role.NORMAL);
        String expectedToString = "UserEntity[id=1, name=testUser, email=test@gmail.com, passwordHash=hashedPassword, role=NORMAL]";
        assertEquals(expectedToString, userEntity.toString());
    }

    @Test
    void testUserEntityWithNullValues() {
        UserEntity userEntity = new UserEntity(null, "name", "test@gmail.com", null, Role.ADMIN);
        assertNull(userEntity.id());
        assertEquals("name", userEntity.name());
        assertEquals("test@gmail.com", userEntity.email());
        assertNull(userEntity.passwordHash());
        assertEquals(Role.ADMIN, userEntity.role());
    }
}