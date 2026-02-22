package com.fpmislata.back.web.mapper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.fpmislata.back.domain.enumerado.Role;
import com.fpmislata.back.domain.service.dto.UserDto;
import com.fpmislata.back.web.webModel.request.RegisterUserRequest;
import com.fpmislata.back.web.webModel.response.UserResponse;

class UserMapperTest {

    private final UserMapper mapper = UserMapper.getInstance();

    @Test
    void fromUserRequestToUserDto_validInput_mapsAllFields() {
        RegisterUserRequest request = new RegisterUserRequest("John", "john@mail.com", "pass123", Role.NORMAL);

        UserDto dto = mapper.fromUserRequestToUserDto(request);

        assertNull(dto.id());
        assertEquals("John", dto.name());
        assertEquals("john@mail.com", dto.email());
        assertEquals("pass123", dto.plainPassword());
        assertNull(dto.passwordHash());
        assertEquals(Role.NORMAL, dto.role());
    }

    @Test
    void fromUserRequestToUserDto_null_returnsNull() {
        assertNull(mapper.fromUserRequestToUserDto(null));
    }

    @Test
    void fromUserDtoToUserResponse_validInput_mapsAllFields() {
        UserDto dto = new UserDto(1L, "John", "john@mail.com", null, "hash", Role.ADMIN);

        UserResponse response = mapper.fromUserDtoToUserResponse(dto);

        assertEquals(1L, response.id());
        assertEquals("John", response.name());
        assertEquals("john@mail.com", response.email());
        assertEquals(Role.ADMIN, response.role());
    }

    @Test
    void fromUserDtoToUserResponse_null_returnsNull() {
        assertNull(mapper.fromUserDtoToUserResponse(null));
    }

    @Test
    void fromUserDtoToUserResponse_doesNotExposePassword() {
        UserDto dto = new UserDto(1L, "John", "john@mail.com", "plainPass", "hashedPass", Role.NORMAL);

        UserResponse response = mapper.fromUserDtoToUserResponse(dto);

        assertNotNull(response);
        // UserResponse record has no password field – it only has id, name, email, role
        assertEquals("John", response.name());
    }
}
