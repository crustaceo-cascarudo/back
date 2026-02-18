package com.fpmislata.back.domain.service.dto;

import com.fpmislata.back.domain.enumerado.Role;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserDtoTest {

    @Test
    void testUserDtoCreationAndGetters() {
        Long id = 1L;
        String name = "testUser";
        String plainPassword = "password123";
        String passwordHash = "hashedPassword";
        Role role = Role.NORMAL;

        UserDto userDto = new UserDto(id, name, "test@gmail.com", plainPassword, passwordHash, role);

        assertEquals(id, userDto.id());
        assertEquals(name, userDto.name());
        assertEquals("test@gmail.com", userDto.email());
        assertEquals(plainPassword, userDto.plainPassword());
        assertEquals(passwordHash, userDto.passwordHash());
        assertEquals(role, userDto.role());
    }

    @Test
    void testUserDtoEquality() {
        UserDto userDto1 = new UserDto(1L, "testUser", "test@gmail.com", "password123", "hashedPassword", Role.NORMAL);
        UserDto userDto2 = new UserDto(1L, "testUser", "test@gmail.com", "password123", "hashedPassword", Role.NORMAL);
        UserDto userDto3 = new UserDto(2L, "anotherUser", "test@gmail.com", "password456", "anotherHash", Role.ADMIN);

        assertEquals(userDto1, userDto2);
        assertNotEquals(userDto1, userDto3);
        assertNotEquals(null, userDto1);
        assertNotEquals(new Object(), userDto1);
    }

    @Test
    void testUserDtoHashCode() {
        UserDto userDto1 = new UserDto(1L, "testUser", "test@gmail.com", "password123", "hashedPassword", Role.NORMAL);
        UserDto userDto2 = new UserDto(1L, "testUser", "test@gmail.com", "password123", "hashedPassword", Role.NORMAL);
        UserDto userDto3 = new UserDto(2L, "anotherUser", "test@gmail.com", "password456", "anotherHash", Role.ADMIN);

        assertEquals(userDto1.hashCode(), userDto2.hashCode());
        assertNotEquals(userDto1.hashCode(), userDto3.hashCode());
    }

    @Test
    void testUserDtoToString() {
        UserDto userDto = new UserDto(1L, "testUser", "test@gmail.com", "password123", "hashedPassword", Role.NORMAL);
        String expectedToString = "UserDto[id=1, name=testUser, email=test@gmail.com, plainPassword=password123, passwordHash=hashedPassword, role=NORMAL]";
        assertEquals(expectedToString, userDto.toString());
    }

    @Test
    void testUserDtoWithNullValues() {
        UserDto userDto = new UserDto(null, "name", "test@gmail.com", null, null, Role.NORMAL);
        assertNull(userDto.id());
        assertEquals("name", userDto.name());
        assertEquals("test@gmail.com", userDto.email());
        assertNull(userDto.plainPassword());
        assertNull(userDto.passwordHash());
        assertEquals(Role.NORMAL, userDto.role());
    }
}
