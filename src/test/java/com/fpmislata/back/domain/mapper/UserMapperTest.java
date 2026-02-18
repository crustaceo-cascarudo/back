package com.fpmislata.back.domain.mapper;

import com.fpmislata.back.domain.enumerado.Role;
import com.fpmislata.back.domain.model.User;
import com.fpmislata.back.domain.repository.entity.UserEntity;
import com.fpmislata.back.domain.service.dto.UserDto;
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
    void testFromUserToUserDto() {
        User user = new User(1L, "testUser", "test@gmail.com", "hashedPassword", Role.NORMAL);
        UserDto userDto = userMapper.fromUserToUserDto(user);

        assertNotNull(userDto);
        assertEquals(user.getId(), userDto.id());
        assertEquals(user.getName(), userDto.name());
        assertNull(userDto.plainPassword());
        assertEquals(user.getPasswordHash(), userDto.passwordHash());
        assertEquals(user.getRole(), userDto.role());
    }

    @Test
    void testFromUserToUserDtoWithNullUser() {
        assertNull(userMapper.fromUserToUserDto(null));
    }

    @Test
    void testFromUserDtoToUser() {
        UserDto userDto = new UserDto(1L, "testUserDto", "test@gmail.com", "plainPassword", "hashedPasswordDto", Role.ADMIN);
        User user = userMapper.fromUserDtoToUser(userDto);

        assertNotNull(user);
        assertEquals(userDto.id(), user.getId());
        assertEquals(userDto.name(), user.getName());
        assertEquals(userDto.passwordHash(), user.getPasswordHash());
        assertEquals(userDto.role(), user.getRole());
    }

    @Test
    void testFromUserDtoToUserWithNullUserDto() {
        assertNull(userMapper.fromUserDtoToUser(null));
    }

    @Test
    void testFromUserToUserEntity() {
        User user = new User(2L, "entityUser", "test@gmail.com", "hashedEntityPassword", Role.NORMAL);
        UserEntity userEntity = userMapper.fromUserToUserEntity(user);

        assertNotNull(userEntity);
        assertEquals(user.getId(), userEntity.id());
        assertEquals(user.getName(), userEntity.name());
        assertEquals(user.getPasswordHash(), userEntity.passwordHash());
        assertEquals(user.getRole(), userEntity.role());
    }

    @Test
    void testFromUserToUserEntityWithNullUser() {
        assertNull(userMapper.fromUserToUserEntity(null));
    }

    @Test
    void testFromUserEntityToUser() {
        UserEntity userEntity = new UserEntity(3L, "userFromEntity", "test@gmail.com", "hashedUserEntityPassword", Role.ADMIN);
        User user = userMapper.fromUserEntityToUser(userEntity);

        assertNotNull(user);
        assertEquals(userEntity.id(), user.getId());
        assertEquals(userEntity.name(), user.getName());
        assertEquals(userEntity.passwordHash(), user.getPasswordHash());
        assertEquals(userEntity.role(), user.getRole());
    }

    @Test
    void testFromUserEntityToUserWithNullUserEntity() {
        assertNull(userMapper.fromUserEntityToUser(null));
    }
}