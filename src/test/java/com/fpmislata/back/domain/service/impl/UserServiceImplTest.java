package com.fpmislata.back.domain.service.impl;

import com.fpmislata.back.domain.enumerado.Role;
import com.fpmislata.back.domain.mapper.UserMapper;
import com.fpmislata.back.domain.model.Page;
import com.fpmislata.back.domain.model.User;
import com.fpmislata.back.domain.repository.UserRepository;
import com.fpmislata.back.domain.repository.entity.UserEntity;
import com.fpmislata.back.domain.service.PasswordEncoderService;
import com.fpmislata.back.domain.service.dto.UserDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoderService passwordEncoderService;
    @Mock
    private UserMapper userMapperMock; // Mock instance of UserMapper

    @InjectMocks
    private UserServiceImpl userService;

    private MockedStatic<UserMapper> mockedStaticUserMapper;

    @BeforeEach
    void setUp() {
        mockedStaticUserMapper = mockStatic(UserMapper.class);
        when(UserMapper.getInstance()).thenReturn(userMapperMock);
    }

    @AfterEach
    void tearDown() {
        mockedStaticUserMapper.close();
    }

    @Test
    void create_whenUserDoesNotExist_shouldCreateUser() {
        UserDto userDtoToCreate = new UserDto(null, "newUser", "test@gmail.com", "plainPassword123", null, Role.NORMAL);
        User userModel = new User(null, "newUser", "test@gmail.com", "hashedPassword", Role.NORMAL);
        UserEntity userEntityToSave = new UserEntity(null, "newUser", "test@gmail.com", "hashedPassword", Role.NORMAL);
        UserEntity savedUserEntity = new UserEntity(1L, "newUser", "test@gmail.com", "hashedPassword", Role.NORMAL);
        User savedUserModel = new User(1L, "newUser", "test@gmail.com", "hashedPassword", Role.NORMAL);
        UserDto expectedUserDto = new UserDto(1L, "newUser", "test@gmail.com", null, "hashedPassword", Role.NORMAL);

        when(userRepository.findByName(userDtoToCreate.name())).thenReturn(Collections.emptyList());
        when(passwordEncoderService.encode(userDtoToCreate.plainPassword())).thenReturn("hashedPassword");
        
        when(userMapperMock.fromUserDtoToUser(any(UserDto.class))).thenReturn(userModel);
        when(userMapperMock.fromUserToUserEntity(any(User.class))).thenReturn(userEntityToSave);

        when(userRepository.save(userEntityToSave)).thenReturn(savedUserEntity);

        when(userMapperMock.fromUserEntityToUser(any(UserEntity.class))).thenReturn(savedUserModel);
        when(userMapperMock.fromUserToUserDto(any(User.class))).thenReturn(expectedUserDto);
        
        UserDto createdUser = userService.create(userDtoToCreate);

        assertNotNull(createdUser);
        assertEquals(1L, createdUser.id());
        assertEquals("newUser", createdUser.name());
        assertEquals("test@gmail.com", createdUser.email());
        assertNull(createdUser.plainPassword());
        assertEquals("hashedPassword", createdUser.passwordHash());
        assertEquals(Role.NORMAL, createdUser.role());

        verify(userRepository, times(1)).findByName(userDtoToCreate.name());
        verify(passwordEncoderService, times(1)).encode(userDtoToCreate.plainPassword());
        verify(userRepository, times(1)).save(userEntityToSave);
    }

    @Test
    void create_shouldEncodePassword() {
        UserDto userDtoToCreate = new UserDto(null, "newUser", "test@gmail.com", "plainPassword123", null, Role.NORMAL);
        User userModel = new User(null, "newUser", "test@gmail.com", "hashedPassword", Role.NORMAL);
        UserEntity userEntityToSave = new UserEntity(null, "newUser", "test@gmail.com", "hashedPassword", Role.NORMAL);
        UserEntity savedUserEntity = new UserEntity(1L, "newUser", "test@gmail.com", "hashedPassword", Role.NORMAL);
        User savedUserModel = new User(1L, "newUser", "test@gmail.com", "hashedPassword", Role.NORMAL);
        UserDto expectedUserDto = new UserDto(1L, "newUser", "test@gmail.com", null, "hashedPassword", Role.NORMAL);


        when(userRepository.findByName(userDtoToCreate.name())).thenReturn(Collections.emptyList());
        when(passwordEncoderService.encode(userDtoToCreate.plainPassword())).thenReturn("hashedPassword");
        
        when(userMapperMock.fromUserDtoToUser(any(UserDto.class))).thenReturn(userModel);
        when(userMapperMock.fromUserToUserEntity(any(User.class))).thenReturn(userEntityToSave);
        when(userRepository.save(userEntityToSave)).thenReturn(savedUserEntity);
        when(userMapperMock.fromUserEntityToUser(any(UserEntity.class))).thenReturn(savedUserModel);
        when(userMapperMock.fromUserToUserDto(any(User.class))).thenReturn(expectedUserDto);

        userService.create(userDtoToCreate);

        verify(passwordEncoderService, times(1)).encode("plainPassword123");
    }

    @Test
    void create_shouldReturnUserWithGeneratedId() {
        UserDto userDtoToCreate = new UserDto(null, "newUser", "test@gmail.com", "plainPassword123", null, Role.NORMAL);
        User userModel = new User(null, "newUser", "test@gmail.com", "hashedPassword", Role.NORMAL);
        UserEntity userEntityToSave = new UserEntity(null, "newUser", "test@gmail.com", "hashedPassword", Role.NORMAL);
        UserEntity savedUserEntity = new UserEntity(100L, "newUser", "test@gmail.com", "hashedPassword", Role.NORMAL);
        User savedUserModel = new User(100L, "newUser", "test@gmail.com", "hashedPassword", Role.NORMAL);
        UserDto expectedUserDto = new UserDto(100L, "newUser", "test@gmail.com", null, "hashedPassword", Role.NORMAL);


        when(userRepository.findByName(userDtoToCreate.name())).thenReturn(Collections.emptyList());
        when(passwordEncoderService.encode(userDtoToCreate.plainPassword())).thenReturn("hashedPassword");
        
        when(userMapperMock.fromUserDtoToUser(any(UserDto.class))).thenReturn(userModel);
        when(userMapperMock.fromUserToUserEntity(any(User.class))).thenReturn(userEntityToSave);
        when(userRepository.save(userEntityToSave)).thenReturn(savedUserEntity);
        when(userMapperMock.fromUserEntityToUser(any(UserEntity.class))).thenReturn(savedUserModel);
        when(userMapperMock.fromUserToUserDto(any(User.class))).thenReturn(expectedUserDto);

        UserDto createdUser = userService.create(userDtoToCreate);

        assertEquals(100L, createdUser.id());
    }

    @Test
    void create_shouldSaveUserInRepository() {
        UserDto userDtoToCreate = new UserDto(null, "newUser", "test@gmail.com", "plainPassword123", null, Role.NORMAL);
        User userModel = new User(null, "newUser", "test@gmail.com", "hashedPassword", Role.NORMAL);
        UserEntity userEntityToSave = new UserEntity(null, "newUser", "test@gmail.com", "hashedPassword", Role.NORMAL);
        UserEntity savedUserEntity = new UserEntity(1L, "newUser", "test@gmail.com", "hashedPassword", Role.NORMAL);
        User savedUserModel = new User(1L, "newUser", "test@gmail.com", "hashedPassword", Role.NORMAL);
        UserDto expectedUserDto = new UserDto(1L, "newUser", "test@gmail.com", null, "hashedPassword", Role.NORMAL);


        when(userRepository.findByName(userDtoToCreate.name())).thenReturn(Collections.emptyList());
        when(passwordEncoderService.encode(userDtoToCreate.plainPassword())).thenReturn("hashedPassword");
        
        when(userMapperMock.fromUserDtoToUser(any(UserDto.class))).thenReturn(userModel);
        when(userMapperMock.fromUserToUserEntity(any(User.class))).thenReturn(userEntityToSave);
        when(userRepository.save(userEntityToSave)).thenReturn(savedUserEntity);
        when(userMapperMock.fromUserEntityToUser(any(UserEntity.class))).thenReturn(savedUserModel);
        when(userMapperMock.fromUserToUserDto(any(User.class))).thenReturn(expectedUserDto);

        userService.create(userDtoToCreate);

        verify(userRepository, times(1)).save(userEntityToSave);
    }

    @Test
    void create_shouldNotReturnPlainPassword() {
        UserDto userDtoToCreate = new UserDto(null, "newUser", "test@gmail.com", "plainPassword123", null, Role.NORMAL);
        User userModel = new User(null, "newUser", "test@gmail.com", "hashedPassword", Role.NORMAL);
        UserEntity userEntityToSave = new UserEntity(null, "newUser", "test@gmail.com", "hashedPassword", Role.NORMAL);
        UserEntity savedUserEntity = new UserEntity(1L, "newUser", "test@gmail.com", "hashedPassword", Role.NORMAL);
        User savedUserModel = new User(1L, "newUser", "test@gmail.com", "hashedPassword", Role.NORMAL);
        UserDto expectedUserDto = new UserDto(1L, "newUser", "test@gmail.com", null, "hashedPassword", Role.NORMAL);


        when(userRepository.findByName(userDtoToCreate.name())).thenReturn(Collections.emptyList());
        when(passwordEncoderService.encode(userDtoToCreate.plainPassword())).thenReturn("hashedPassword");
        
        when(userMapperMock.fromUserDtoToUser(any(UserDto.class))).thenReturn(userModel);
        when(userMapperMock.fromUserToUserEntity(any(User.class))).thenReturn(userEntityToSave);
        when(userRepository.save(userEntityToSave)).thenReturn(savedUserEntity);
        when(userMapperMock.fromUserEntityToUser(any(UserEntity.class))).thenReturn(savedUserModel);
        when(userMapperMock.fromUserToUserDto(any(User.class))).thenReturn(expectedUserDto);

        UserDto createdUser = userService.create(userDtoToCreate);

        assertNull(createdUser.plainPassword());
    }

    @Test
    void create_shouldAssignRoleCorrectly() {
        UserDto userDtoToCreate = new UserDto(null, "newUser", "test@gmail.com", "plainPassword123", null, Role.ADMIN);
        User userModel = new User(null, "newUser", "test@gmail.com", "hashedPassword", Role.ADMIN);
        UserEntity userEntityToSave = new UserEntity(null, "newUser", "test@gmail.com", "hashedPassword", Role.ADMIN);
        UserEntity savedUserEntity = new UserEntity(1L, "newUser", "test@gmail.com", "hashedPassword", Role.ADMIN);
        User savedUserModel = new User(1L, "newUser", "test@gmail.com", "hashedPassword", Role.ADMIN);
        UserDto expectedUserDto = new UserDto(1L, "newUser", "test@gmail.com", null, "hashedPassword", Role.ADMIN);


        when(userRepository.findByName(userDtoToCreate.name())).thenReturn(Collections.emptyList());
        when(passwordEncoderService.encode(userDtoToCreate.plainPassword())).thenReturn("hashedPassword");
        
        when(userMapperMock.fromUserDtoToUser(any(UserDto.class))).thenReturn(userModel);
        when(userMapperMock.fromUserToUserEntity(any(User.class))).thenReturn(userEntityToSave);
        when(userRepository.save(userEntityToSave)).thenReturn(savedUserEntity);
        when(userMapperMock.fromUserEntityToUser(any(UserEntity.class))).thenReturn(savedUserModel);
        when(userMapperMock.fromUserToUserDto(any(User.class))).thenReturn(expectedUserDto);

        UserDto createdUser = userService.create(userDtoToCreate);

        assertEquals(Role.ADMIN, createdUser.role());
    }

    @Test
    void create_whenUserNameAlreadyExists_shouldThrowException() {
        UserDto userDtoToCreate = new UserDto(null, "existingUser", "test@gmail.com", "plainPassword123", null, Role.NORMAL);

        when(userRepository.findByName(userDtoToCreate.name())).thenReturn(List.of(
            new UserEntity(1L, "existingUser", "test@gmail.com", "someHash", Role.NORMAL)
        ));
        when(userMapperMock.fromUserEntityToUser(any(UserEntity.class))).thenReturn(new User(1L, "existingUser", "test@gmail.com", "someHash", Role.NORMAL));
        when(userMapperMock.fromUserToUserDto(any(User.class))).thenReturn(new UserDto(1L, "existingUser", "test@gmail.com", null, "someHash", Role.NORMAL));


        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.create(userDtoToCreate));

        assertEquals("User with name existingUser already exists.", exception.getMessage());
        verify(userRepository, times(1)).findByName(userDtoToCreate.name());
        verify(passwordEncoderService, never()).encode(anyString());
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    void create_whenUserNameAlreadyExists_shouldNotSaveUser() {
        UserDto userDtoToCreate = new UserDto(null, "existingUser", "test@gmail.com", "plainPassword123", null, Role.NORMAL);

        when(userRepository.findByName(userDtoToCreate.name())).thenReturn(List.of(
            new UserEntity(1L, "existingUser", "test@gmail.com", "someHash", Role.NORMAL)
        ));
        when(userMapperMock.fromUserEntityToUser(any(UserEntity.class))).thenReturn(new User(1L, "existingUser", "test@gmail.com", "someHash", Role.NORMAL));
        when(userMapperMock.fromUserToUserDto(any(User.class))).thenReturn(new UserDto(1L, "existingUser", "test@gmail.com", null, "someHash", Role.NORMAL));

        assertThrows(IllegalArgumentException.class, () -> userService.create(userDtoToCreate));

        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    void create_whenPasswordEncoderFails_shouldPropagateException() {
        UserDto userDtoToCreate = new UserDto(null, "newUser", "test@gmail.com", "plainPassword123", null, Role.NORMAL);

        when(userRepository.findByName(userDtoToCreate.name())).thenReturn(Collections.emptyList());
        when(passwordEncoderService.encode(userDtoToCreate.plainPassword())).thenThrow(new RuntimeException("Encoding failed"));

        Exception exception = assertThrows(RuntimeException.class, () -> userService.create(userDtoToCreate));

        assertEquals("Encoding failed", exception.getMessage());
        verify(userRepository, times(1)).findByName(userDtoToCreate.name());
        verify(passwordEncoderService, times(1)).encode(userDtoToCreate.plainPassword());
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    // UPDATE
    @Test
    void update_whenUserExists_shouldUpdateUser() {
        Long userId = 1L;
        UserDto userDtoToUpdate = new UserDto(userId, "updatedName", "test@gmail.com", null, "newHashedPassword", Role.ADMIN);
        
        User userModel = new User(userId, "updatedName", "test@gmail.com", "newHashedPassword", Role.ADMIN);
        UserEntity userEntityToSave = new UserEntity(userId, "updatedName", "test@gmail.com", "newHashedPassword", Role.ADMIN);
        UserEntity savedUserEntity = new UserEntity(userId, "updatedName", "test@gmail.com", "newHashedPassword", Role.ADMIN);
        User savedUserModel = new User(userId, "updatedName", "test@gmail.com", "newHashedPassword", Role.ADMIN);
        UserDto expectedUserDto = new UserDto(userId, "updatedName", "test@gmail.com", null, "newHashedPassword", Role.ADMIN);

        when(userRepository.findById(userId)).thenReturn(Optional.of(new UserEntity(userId, "oldName", "test@gmail.com", "oldHashedPassword", Role.NORMAL)));
        
        when(userMapperMock.fromUserEntityToUser(any(UserEntity.class)))
                .thenReturn(new User(userId, "oldName", "test@gmail.com", "oldHashedPassword", Role.NORMAL));
        when(userMapperMock.fromUserToUserDto(any(User.class)))
                .thenReturn(new UserDto(userId, "oldName", "test@gmail.com", null, "oldHashedPassword", Role.NORMAL));

        when(userMapperMock.fromUserDtoToUser(userDtoToUpdate)).thenReturn(userModel);
        when(userMapperMock.fromUserToUserEntity(userModel)).thenReturn(userEntityToSave);
        when(userRepository.save(userEntityToSave)).thenReturn(savedUserEntity);
        when(userMapperMock.fromUserEntityToUser(savedUserEntity)).thenReturn(savedUserModel);
        when(userMapperMock.fromUserToUserDto(savedUserModel)).thenReturn(expectedUserDto);

        UserDto updatedUser = userService.update(userDtoToUpdate);

        assertNotNull(updatedUser);
        assertEquals(userId, updatedUser.id());
        assertEquals("updatedName", updatedUser.name());
        assertEquals("test@gmail.com", updatedUser.email());
        assertEquals("newHashedPassword", updatedUser.passwordHash());
        assertEquals(Role.ADMIN, updatedUser.role());

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(userEntityToSave);
    }

    @Test
    void update_shouldSaveUpdatedUser() {
        Long userId = 1L;
        UserDto userDtoToUpdate = new UserDto(userId, "updatedName", "test@gmail.com", null, "newHashedPassword", Role.ADMIN);
        
        User userModel = new User(userId, "updatedName", "test@gmail.com", "newHashedPassword", Role.ADMIN);
        UserEntity userEntityToSave = new UserEntity(userId, "updatedName", "test@gmail.com", "newHashedPassword", Role.ADMIN);
        UserEntity savedUserEntity = new UserEntity(userId, "updatedName", "test@gmail.com", "newHashedPassword", Role.ADMIN);
        User savedUserModel = new User(userId, "updatedName", "test@gmail.com", "newHashedPassword", Role.ADMIN);
        UserDto expectedUserDto = new UserDto(userId, "updatedName", "test@gmail.com", null, "newHashedPassword", Role.ADMIN);

        when(userRepository.findById(userId)).thenReturn(Optional.of(new UserEntity(userId, "oldName", "test@gmail.com", "oldHashedPassword", Role.NORMAL)));
        
        when(userMapperMock.fromUserEntityToUser(any(UserEntity.class)))
                .thenReturn(new User(userId, "oldName", "test@gmail.com", "oldHashedPassword", Role.NORMAL));
        when(userMapperMock.fromUserToUserDto(any(User.class)))
                .thenReturn(new UserDto(userId, "oldName", "test@gmail.com", null, "oldHashedPassword", Role.NORMAL));

        when(userMapperMock.fromUserDtoToUser(userDtoToUpdate)).thenReturn(userModel);
        when(userMapperMock.fromUserToUserEntity(userModel)).thenReturn(userEntityToSave);
        when(userRepository.save(userEntityToSave)).thenReturn(savedUserEntity);
        when(userMapperMock.fromUserEntityToUser(savedUserEntity)).thenReturn(savedUserModel);
        when(userMapperMock.fromUserToUserDto(savedUserModel)).thenReturn(expectedUserDto);

        userService.update(userDtoToUpdate);

        verify(userRepository, times(1)).save(userEntityToSave);
    }

    @Test
    void update_shouldReturnUpdatedUser() {
        Long userId = 1L;
        UserDto userDtoToUpdate = new UserDto(userId, "updatedName", "test@gmail.com", null, "newHashedPassword", Role.ADMIN);
        
        User userModel = new User(userId, "updatedName", "test@gmail.com", "newHashedPassword", Role.ADMIN);
        UserEntity userEntityToSave = new UserEntity(userId, "updatedName", "test@gmail.com", "newHashedPassword", Role.ADMIN);
        UserEntity savedUserEntity = new UserEntity(userId, "updatedName", "test@gmail.com", "newHashedPassword", Role.ADMIN);
        User savedUserModel = new User(userId, "updatedName", "test@gmail.com", "newHashedPassword", Role.ADMIN);
        UserDto expectedUserDto = new UserDto(userId, "updatedName", "test@gmail.com", null, "newHashedPassword", Role.ADMIN);

        when(userRepository.findById(userId)).thenReturn(Optional.of(new UserEntity(userId, "oldName", "test@gmail.com", "oldHashedPassword", Role.NORMAL)));
        
        when(userMapperMock.fromUserEntityToUser(any(UserEntity.class)))
                .thenReturn(new User(userId, "oldName", "test@gmail.com", "oldHashedPassword", Role.NORMAL));
        when(userMapperMock.fromUserToUserDto(any(User.class)))
                .thenReturn(new UserDto(userId, "oldName", "test@gmail.com", null, "oldHashedPassword", Role.NORMAL));

        when(userMapperMock.fromUserDtoToUser(userDtoToUpdate)).thenReturn(userModel);
        when(userMapperMock.fromUserToUserEntity(userModel)).thenReturn(userEntityToSave);
        when(userRepository.save(userEntityToSave)).thenReturn(savedUserEntity);
        when(userMapperMock.fromUserEntityToUser(savedUserEntity)).thenReturn(savedUserModel);
        when(userMapperMock.fromUserToUserDto(savedUserModel)).thenReturn(expectedUserDto);

        UserDto updatedUser = userService.update(userDtoToUpdate);

        assertNotNull(updatedUser);
        assertEquals(userId, updatedUser.id());
        assertEquals("updatedName", updatedUser.name());
        assertEquals("test@gmail.com", updatedUser.email());
        assertEquals("newHashedPassword", updatedUser.passwordHash());
        assertEquals(Role.ADMIN, updatedUser.role());
    }

    @Test
    void update_shouldPreserveUserId() {
        Long userId = 1L;
        UserDto userDtoToUpdate = new UserDto(userId, "updatedName", "test@gmail.com", null, "newHashedPassword", Role.ADMIN);
        
        User userModel = new User(userId, "updatedName", "test@gmail.com", "newHashedPassword", Role.ADMIN);
        UserEntity userEntityToSave = new UserEntity(userId, "updatedName", "test@gmail.com", "newHashedPassword", Role.ADMIN);
        UserEntity savedUserEntity = new UserEntity(userId, "updatedName", "test@gmail.com", "newHashedPassword", Role.ADMIN);
        User savedUserModel = new User(userId, "updatedName", "test@gmail.com", "newHashedPassword", Role.ADMIN);
        UserDto expectedUserDto = new UserDto(userId, "updatedName", "test@gmail.com", null, "newHashedPassword", Role.ADMIN);

        when(userRepository.findById(userId)).thenReturn(Optional.of(new UserEntity(userId, "oldName", "test@gmail.com", "oldHashedPassword", Role.NORMAL)));
        
        when(userMapperMock.fromUserEntityToUser(any(UserEntity.class)))
                .thenReturn(new User(userId, "oldName", "test@gmail.com", "oldHashedPassword", Role.NORMAL));
        when(userMapperMock.fromUserToUserDto(any(User.class)))
                .thenReturn(new UserDto(userId, "oldName", "test@gmail.com", null, "oldHashedPassword", Role.NORMAL));

        when(userMapperMock.fromUserDtoToUser(userDtoToUpdate)).thenReturn(userModel);
        when(userMapperMock.fromUserToUserEntity(userModel)).thenReturn(userEntityToSave);
        when(userRepository.save(userEntityToSave)).thenReturn(savedUserEntity);
        when(userMapperMock.fromUserEntityToUser(savedUserEntity)).thenReturn(savedUserModel);
        when(userMapperMock.fromUserToUserDto(savedUserModel)).thenReturn(expectedUserDto);

        UserDto updatedUser = userService.update(userDtoToUpdate);

        assertEquals(userId, updatedUser.id());
    }

    @Test
    void update_whenUserDoesNotExist_shouldThrowException() {
        Long userId = 99L;
        UserDto userDtoToUpdate = new UserDto(userId, "nonExistentUser", "test@gmail.com", null, "hashed", Role.NORMAL);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.update(userDtoToUpdate));

        assertEquals("User with id " + userId + " does not exist.", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    void update_whenUserDoesNotExist_shouldNotSaveUser() {
        Long userId = 99L;
        UserDto userDtoToUpdate = new UserDto(userId, "nonExistentUser", "test@gmail.com", null, "hashed", Role.NORMAL);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userService.update(userDtoToUpdate));

        verify(userRepository, never()).save(any(UserEntity.class));
    }

    // DELETE
    @Test
    void delete_whenUserExistsAndIsNotAdmin_shouldDeleteUser() {
        Long userId = 1L;
        UserEntity userEntity = new UserEntity(userId, "userToDelete", "test@gmail.com", "someHash", Role.NORMAL);
        User userModel = new User(userId, "userToDelete", "test@gmail.com", "someHash", Role.NORMAL);
        UserDto userDto = new UserDto(userId, "userToDelete", "test@gmail.com", null, "someHash", Role.NORMAL);

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(userMapperMock.fromUserEntityToUser(userEntity)).thenReturn(userModel);
        when(userMapperMock.fromUserToUserDto(userModel)).thenReturn(userDto);
        doNothing().when(userRepository).delete(userId);

        userService.delete(userId);

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).delete(userId);
    }

    @Test
    void delete_shouldCallRepositoryDelete() {
        Long userId = 1L;
        UserEntity userEntity = new UserEntity(userId, "userToDelete", "test@gmail.com", "someHash", Role.NORMAL);
        User userModel = new User(userId, "userToDelete", "test@gmail.com", "someHash", Role.NORMAL);
        UserDto userDto = new UserDto(userId, "userToDelete", "test@gmail.com", null, "someHash", Role.NORMAL);

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(userMapperMock.fromUserEntityToUser(userEntity)).thenReturn(userModel);
        when(userMapperMock.fromUserToUserDto(userModel)).thenReturn(userDto);
        doNothing().when(userRepository).delete(userId);

        userService.delete(userId);

        verify(userRepository, times(1)).delete(userId);
    }

    @Test
    void delete_whenUserDoesNotExist_shouldThrowException() {
        Long userId = 99L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.delete(userId));

        assertEquals("User with id " + userId + " does not exist.", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).delete(anyLong());
    }

    @Test
    void delete_whenUserIsAdmin_shouldThrowException() {
        Long userId = 1L;
        UserEntity adminUserEntity = new UserEntity(userId, "adminUser", "test@gmail.com", "someHash", Role.ADMIN);
        User adminUserModel = new User(userId, "adminUser", "test@gmail.com", "someHash", Role.ADMIN);
        UserDto adminUserDto = new UserDto(userId, "adminUser", "test@gmail.com", null, "someHash", Role.ADMIN);

        when(userRepository.findById(userId)).thenReturn(Optional.of(adminUserEntity));
        when(userMapperMock.fromUserEntityToUser(adminUserEntity)).thenReturn(adminUserModel);
        when(userMapperMock.fromUserToUserDto(adminUserModel)).thenReturn(adminUserDto);
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.delete(userId));

        assertEquals("Cannot delete an ADMIN user.", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).delete(anyLong());
    }

    @Test
    void delete_whenUserIsAdmin_shouldNotDeleteUser() {
        Long userId = 1L;
        UserEntity adminUserEntity = new UserEntity(userId, "adminUser", "test@gmail.com", "someHash", Role.ADMIN);
        User adminUserModel = new User(userId, "adminUser", "test@gmail.com", "someHash", Role.ADMIN);
        UserDto adminUserDto = new UserDto(userId, "adminUser", "test@gmail.com", null, "someHash", Role.ADMIN);

        when(userRepository.findById(userId)).thenReturn(Optional.of(adminUserEntity));
        when(userMapperMock.fromUserEntityToUser(adminUserEntity)).thenReturn(adminUserModel);
        when(userMapperMock.fromUserToUserDto(adminUserModel)).thenReturn(adminUserDto);

        assertThrows(IllegalArgumentException.class, () -> userService.delete(userId));

        verify(userRepository, never()).delete(anyLong());
    }

    // LOGIN
    @Test
    void logByEmail_whenCredentialsAreCorrect_shouldReturnToken() {
        String name = "testUser";
        String email = "test@gmail.com";
        String plainPassword = "plainPassword";
        String hashedPassword = "hashedPassword";
        String sessionToken = "randomSessionToken";

        UserEntity userEntity = new UserEntity(1L, name, email, hashedPassword, Role.NORMAL);
        User userModel = new User(1L, name, email,  hashedPassword, Role.NORMAL);
        UserDto userDto = new UserDto(1L, name, email, null, hashedPassword, Role.NORMAL);

        when(userRepository.findByEmail(email)).thenReturn(List.of(userEntity));
        when(userMapperMock.fromUserEntityToUser(userEntity)).thenReturn(userModel);
        when(userMapperMock.fromUserToUserDto(userModel)).thenReturn(userDto);

        when(passwordEncoderService.verify(plainPassword, hashedPassword)).thenReturn(true);
        when(userRepository.createSessionToken(userDto.id())).thenReturn(sessionToken);

        String resultToken = userService.logByEmail(email, plainPassword);

        assertEquals(sessionToken, resultToken);
        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordEncoderService, times(1)).verify(plainPassword, hashedPassword);
        verify(userRepository, times(1)).createSessionToken(userDto.id());
    }

    @Test
    void logByEmail_shouldCreateSessionToken() {
        String name = "testUser";
        String email = "test@gmail.com";
        String plainPassword = "plainPassword";
        String hashedPassword = "hashedPassword";
        String sessionToken = "randomSessionToken";

        UserEntity userEntity = new UserEntity(1L, name, email, hashedPassword, Role.NORMAL);
        User userModel = new User(1L, name, email, hashedPassword, Role.NORMAL);
        UserDto userDto = new UserDto(1L, name, email, null, hashedPassword, Role.NORMAL);

        when(userRepository.findByEmail(email)).thenReturn(List.of(userEntity));
        when(userMapperMock.fromUserEntityToUser(userEntity)).thenReturn(userModel);
        when(userMapperMock.fromUserToUserDto(userModel)).thenReturn(userDto);

        when(passwordEncoderService.verify(plainPassword, hashedPassword)).thenReturn(true);
        when(userRepository.createSessionToken(userDto.id())).thenReturn(sessionToken);

        userService.logByEmail(email, plainPassword);

        verify(userRepository, times(1)).createSessionToken(userDto.id());
    }

    @Test
    void logByEmail_shouldCallVerifyPassword() {
        String name = "testUser";
        String email = "test@gmail.com";
        String plainPassword = "plainPassword";
        String hashedPassword = "hashedPassword";
        String sessionToken = "randomSessionToken";

        UserEntity userEntity = new UserEntity(1L, name, email, hashedPassword, Role.NORMAL);
        User userModel = new User(1L, name, email, hashedPassword, Role.NORMAL);
        UserDto userDto = new UserDto(1L, name, email, null, hashedPassword, Role.NORMAL);

        when(userRepository.findByEmail(email)).thenReturn(List.of(userEntity));
        when(userMapperMock.fromUserEntityToUser(userEntity)).thenReturn(userModel);
        when(userMapperMock.fromUserToUserDto(userModel)).thenReturn(userDto);

        when(passwordEncoderService.verify(plainPassword, hashedPassword)).thenReturn(true);
        when(userRepository.createSessionToken(userDto.id())).thenReturn(sessionToken);

        userService.logByEmail(email, plainPassword);

        verify(passwordEncoderService, times(1)).verify(plainPassword, hashedPassword);
    }

    @Test
    void logByEmail_whenUserDoesNotExist_shouldThrowException() {
        String email = "nonExistentUser@example.com";
        String plainPassword = "anyPassword";

        when(userRepository.findByEmail(email)).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.logByEmail(email, plainPassword));

        assertEquals("User with email " + email + " does not exist.", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordEncoderService, never()).verify(anyString(), anyString());
        verify(userRepository, never()).createSessionToken(anyLong());
    }

    @Test
    void logByEmail_whenPasswordIsIncorrect_shouldThrowException() {
        String email = "testUser@example.com";
        String plainPassword = "wrongPassword";
        String hashedPassword = "hashedPassword";

        UserEntity userEntity = new UserEntity(1L, "testUser", email, hashedPassword, Role.NORMAL);
        User userModel = new User(1L, "testUser", email, hashedPassword, Role.NORMAL);
        UserDto userDto = new UserDto(1L, "testUser", email, null, hashedPassword, Role.NORMAL);

        when(userRepository.findByEmail(email)).thenReturn(List.of(userEntity));
        when(userMapperMock.fromUserEntityToUser(userEntity)).thenReturn(userModel);
        when(userMapperMock.fromUserToUserDto(userModel)).thenReturn(userDto);

        when(passwordEncoderService.verify(plainPassword, hashedPassword)).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.logByEmail(email, plainPassword));

        assertEquals("Incorrect password for user " + email + ".", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordEncoderService, times(1)).verify(plainPassword, hashedPassword);
        verify(userRepository, never()).createSessionToken(anyLong());
    }

    @Test
    void logByEmail_whenPasswordIsIncorrect_shouldNotCreateToken() {
        String email = "testUser@example.com";
        String plainPassword = "wrongPassword";
        String hashedPassword = "hashedPassword";

        UserEntity userEntity = new UserEntity(1L, "testUser", email, hashedPassword, Role.NORMAL);
        User userModel = new User(1L, "testUser", email, hashedPassword, Role.NORMAL);
        UserDto userDto = new UserDto(1L, "testUser", email, null, hashedPassword, Role.NORMAL);

        when(userRepository.findByEmail(email)).thenReturn(List.of(userEntity));
        when(userMapperMock.fromUserEntityToUser(userEntity)).thenReturn(userModel);
        when(userMapperMock.fromUserToUserDto(userModel)).thenReturn(userDto);

        when(passwordEncoderService.verify(plainPassword, hashedPassword)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> userService.logByEmail(email, plainPassword));

        verify(userRepository, never()).createSessionToken(anyLong());
    }

    // LOGOUT
    @Test
    void logout_whenTokenIsValid_shouldDeleteSession() {
        String token = "validToken";
        UserEntity userEntity = new UserEntity(1L, "user", "test@gmail.com", "hash", Role.NORMAL);

        when(userRepository.findByToken(token)).thenReturn(userEntity);
        doNothing().when(userRepository).deleteSessionToken(token);

        userService.logout(token);

        verify(userRepository, times(1)).findByToken(token);
        verify(userRepository, times(1)).deleteSessionToken(token);
    }

    @Test
    void logout_shouldCallDeleteSessionToken() {
        String token = "validToken";
        UserEntity userEntity = new UserEntity(1L, "user", "test@gmail.com", "hash", Role.NORMAL);

        when(userRepository.findByToken(token)).thenReturn(userEntity);
        doNothing().when(userRepository).deleteSessionToken(token);

        userService.logout(token);

        verify(userRepository, times(1)).deleteSessionToken(token);
    }

    @Test
    void logout_whenTokenIsNull_shouldThrowException() {
        String token = null;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.logout(token));

        assertEquals("Token cannot be null or empty", exception.getMessage());
        verify(userRepository, never()).findByToken(anyString());
        verify(userRepository, never()).deleteSessionToken(anyString());
    }

    @Test
    void logout_whenTokenIsEmpty_shouldThrowException() {
        String token = "";

        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.logout(token));

        assertEquals("Token cannot be null or empty", exception.getMessage());
        verify(userRepository, never()).findByToken(anyString());
        verify(userRepository, never()).deleteSessionToken(anyString());
    }

    @Test
    void logout_whenTokenIsInvalid_shouldThrowException() {
        String token = "invalidToken";

        when(userRepository.findByToken(token)).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.logout(token));

        assertEquals("Invalid token or session already expired", exception.getMessage());
        verify(userRepository, times(1)).findByToken(token);
        verify(userRepository, never()).deleteSessionToken(anyString());
    }

    @Test
    void logout_whenTokenIsInvalid_shouldNotDeleteSession() {
        String token = "invalidToken";

        when(userRepository.findByToken(token)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> userService.logout(token));

        verify(userRepository, never()).deleteSessionToken(anyString());
    }

    // FIND BY ID
    @Test
    void findById_whenUserExists_shouldReturnUserDto() {
        Long userId = 1L;
        UserEntity userEntity = new UserEntity(userId, "testUser", "test@gmail.com", "hashedPassword", Role.NORMAL);
        User userModel = new User(userId, "testUser", "test@gmail.com", "hashedPassword", Role.NORMAL);
        UserDto expectedUserDto = new UserDto(userId, "testUser", "test@gmail.com", null, "hashedPassword", Role.NORMAL);

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(userMapperMock.fromUserEntityToUser(userEntity)).thenReturn(userModel);
        when(userMapperMock.fromUserToUserDto(userModel)).thenReturn(expectedUserDto);

        Optional<UserDto> result = userService.findById(userId);

        assertTrue(result.isPresent());
        assertEquals(expectedUserDto, result.get());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void findById_shouldMapEntityToDtoCorrectly() {
        Long userId = 1L;
        UserEntity userEntity = new UserEntity(userId, "testUser", "test@gmail.com", "hashedPassword", Role.NORMAL);
        User userModel = new User(userId, "testUser", "test@gmail.com", "hashedPassword", Role.NORMAL);
        UserDto expectedUserDto = new UserDto(userId, "testUser", "test@gmail.com", null, "hashedPassword", Role.NORMAL);

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(userMapperMock.fromUserEntityToUser(userEntity)).thenReturn(userModel);
        when(userMapperMock.fromUserToUserDto(userModel)).thenReturn(expectedUserDto);

        Optional<UserDto> result = userService.findById(userId);

        assertTrue(result.isPresent());
        assertEquals(expectedUserDto.id(), result.get().id());
        assertEquals(expectedUserDto.name(), result.get().name());
        assertEquals(expectedUserDto.passwordHash(), result.get().passwordHash());
        assertEquals(expectedUserDto.role(), result.get().role());
    }

    @Test
    void findById_whenUserDoesNotExist_shouldReturnEmptyOptional() {
        Long userId = 99L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Optional<UserDto> result = userService.findById(userId);

        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findById(userId);
        verify(userMapperMock, never()).fromUserEntityToUser(any(UserEntity.class));
        verify(userMapperMock, never()).fromUserToUserDto(any(User.class));
    }

    // FIND BY NAME
    @Test
    void findByName_whenUsersExist_shouldReturnList() {
        String name = "testUser";
        UserEntity userEntity1 = new UserEntity(1L, name, "test@gmail.com", "hash1", Role.NORMAL);
        UserEntity userEntity2 = new UserEntity(2L, name, "test@gmail.com", "hash2", Role.ADMIN);
        List<UserEntity> userEntities = List.of(userEntity1, userEntity2);

        User userModel1 = new User(1L, name, "test@gmail.com", "hash1", Role.NORMAL);
        User userModel2 = new User(2L, name, "test@gmail.com", "hash2", Role.ADMIN);

        UserDto userDto1 = new UserDto(1L, name, "test@gmail.com", null, "hash1", Role.NORMAL);
        UserDto userDto2 = new UserDto(2L, name, "test@gmail.com", null, "hash2", Role.ADMIN);
        List<UserDto> expectedUserDtos = List.of(userDto1, userDto2);

        when(userRepository.findByName(name)).thenReturn(userEntities);
        when(userMapperMock.fromUserEntityToUser(userEntity1)).thenReturn(userModel1);
        when(userMapperMock.fromUserToUserDto(userModel1)).thenReturn(userDto1);
        when(userMapperMock.fromUserEntityToUser(userEntity2)).thenReturn(userModel2);
        when(userMapperMock.fromUserToUserDto(userModel2)).thenReturn(userDto2);

        List<UserDto> result = userService.findByName(name);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
        assertEquals(expectedUserDtos.get(0).id(), result.get(0).id());
        assertEquals(expectedUserDtos.get(1).name(), result.get(1).name());

        verify(userRepository, times(1)).findByName(name);
        verify(userMapperMock, times(1)).fromUserEntityToUser(userEntity1);
        verify(userMapperMock, times(1)).fromUserToUserDto(userModel1);
        verify(userMapperMock, times(1)).fromUserEntityToUser(userEntity2);
        verify(userMapperMock, times(1)).fromUserToUserDto(userModel2);
    }

    @Test
    void findByName_shouldMapAllUsersCorrectly() {
        String name = "testUser";
        UserEntity userEntity1 = new UserEntity(1L, name, "test@gmail.com", "hash1", Role.NORMAL);
        UserEntity userEntity2 = new UserEntity(2L, name, "test@gmail.com", "hash2", Role.ADMIN);
        List<UserEntity> userEntities = List.of(userEntity1, userEntity2);

        User userModel1 = new User(1L, name, "test@gmail.com", "hash1", Role.NORMAL);
        User userModel2 = new User(2L, name, "test@gmail.com", "hash2", Role.ADMIN);

        UserDto userDto1 = new UserDto(1L, name, "test@gmail.com", null, "hash1", Role.NORMAL);
        UserDto userDto2 = new UserDto(2L, name, "test@gmail.com", null, "hash2", Role.ADMIN);
        List<UserDto> expectedUserDtos = List.of(userDto1, userDto2);

        when(userRepository.findByName(name)).thenReturn(userEntities);
        when(userMapperMock.fromUserEntityToUser(userEntity1)).thenReturn(userModel1);
        when(userMapperMock.fromUserToUserDto(userModel1)).thenReturn(userDto1);
        when(userMapperMock.fromUserEntityToUser(userEntity2)).thenReturn(userModel2);
        when(userMapperMock.fromUserToUserDto(userModel2)).thenReturn(userDto2);

        List<UserDto> result = userService.findByName(name);

        assertEquals(expectedUserDtos, result);
    }

    @Test
    void findByName_whenMultipleUsersHaveSameName_shouldReturnAll() {
        String name = "testUser";
        UserEntity userEntity1 = new UserEntity(1L, name, "test@gmail.com", "hash1", Role.NORMAL);
        UserEntity userEntity2 = new UserEntity(2L, name, "test@gmail.com", "hash2", Role.ADMIN);
        List<UserEntity> userEntities = List.of(userEntity1, userEntity2);

        User userModel1 = new User(1L, name, "test@gmail.com", "hash1", Role.NORMAL);
        User userModel2 = new User(2L, name, "test@gmail.com", "hash2", Role.ADMIN);

        UserDto userDto1 = new UserDto(1L, name, "test@gmail.com", null, "hash1", Role.NORMAL);
        UserDto userDto2 = new UserDto(2L, name, "test@gmail.com", null, "hash2", Role.ADMIN);
        List<UserDto> expectedUserDtos = List.of(userDto1, userDto2);

        when(userRepository.findByName(name)).thenReturn(userEntities);
        when(userMapperMock.fromUserEntityToUser(userEntity1)).thenReturn(userModel1);
        when(userMapperMock.fromUserToUserDto(userModel1)).thenReturn(userDto1);
        when(userMapperMock.fromUserEntityToUser(userEntity2)).thenReturn(userModel2);
        when(userMapperMock.fromUserToUserDto(userModel2)).thenReturn(userDto2);

        List<UserDto> result = userService.findByName(name);

        assertEquals(2, result.size());
    }

    @Test
    void findByName_whenNoUsersExist_shouldReturnEmptyList() {
        String name = "nonExistent";

        when(userRepository.findByName(name)).thenReturn(Collections.emptyList());

        List<UserDto> result = userService.findByName(name);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findByName(name);
        verify(userMapperMock, never()).fromUserEntityToUser(any(UserEntity.class));
        verify(userMapperMock, never()).fromUserToUserDto(any(User.class));
    }

    @Test
    void findByName_whenNameIsNull_shouldHandleGracefully() {
        String name = null;

        when(userRepository.findByName(name)).thenReturn(Collections.emptyList());

        List<UserDto> result = userService.findByName(name);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findByName(name);
        verify(userMapperMock, never()).fromUserEntityToUser(any(UserEntity.class));
        verify(userMapperMock, never()).fromUserToUserDto(any(User.class));
    }

    // FIND ALL
    @Test
    void findAll_whenUsersExist_shouldReturnAllUsers() {
        UserEntity userEntity1 = new UserEntity(1L, "user1", "test@gmail.com", "hash1", Role.NORMAL);
        UserEntity userEntity2 = new UserEntity(2L, "user2", "test@gmail.com", "hash2", Role.ADMIN);
        List<UserEntity> userEntities = List.of(userEntity1, userEntity2);
        Page<UserEntity> page = new Page<UserEntity>(userEntities, 1, 2, 2);

        User userModel1 = new User(1L, "user1", "test@gmail.com", "hash1", Role.NORMAL);
        User userModel2 = new User(2L, "user2", "test@gmail.com", "hash2", Role.ADMIN);

        UserDto userDto1 = new UserDto(1L, "user1", "test@gmail.com", null, "hash1", Role.NORMAL);
        UserDto userDto2 = new UserDto(2L, "user2", "test@gmail.com", null, "hash2", Role.ADMIN);
        List<UserDto> expectedUserDtos = List.of(userDto1, userDto2);

        when(userRepository.findAll(1, 10)).thenReturn(page);
        when(userMapperMock.fromUserEntityToUser(userEntity1)).thenReturn(userModel1);
        when(userMapperMock.fromUserToUserDto(userModel1)).thenReturn(userDto1);
        when(userMapperMock.fromUserEntityToUser(userEntity2)).thenReturn(userModel2);
        when(userMapperMock.fromUserToUserDto(userModel2)).thenReturn(userDto2);

        Page<UserDto> result = userService.findAll(1, 10);

        assertNotNull(result);
        assertFalse(result.data().isEmpty());
        assertEquals(2, result.data().size());
        assertEquals(expectedUserDtos.get(0).id(), result.data().get(0).id());
        assertEquals(expectedUserDtos.get(1).name(), result.data().get(1).name());

        verify(userRepository, times(1)).findAll(1, 10);
        verify(userMapperMock, times(1)).fromUserEntityToUser(userEntity1);
        verify(userMapperMock, times(1)).fromUserToUserDto(userModel1);
        verify(userMapperMock, times(1)).fromUserEntityToUser(userEntity2);
        verify(userMapperMock, times(1)).fromUserToUserDto(userModel2);
    }

    @Test
    void findAll_shouldMapAllEntitiesToDtos() {
        UserEntity userEntity1 = new UserEntity(1L, "user1", "test@gmail.com", "hash1", Role.NORMAL);
        UserEntity userEntity2 = new UserEntity(2L, "user2", "test@gmail.com", "hash2", Role.ADMIN);
        List<UserEntity> userEntities = List.of(userEntity1, userEntity2);
        Page<UserEntity> page = new Page<UserEntity>(userEntities, 1, 2, 2);


        User userModel1 = new User(1L, "user1","test@gmail.com",  "hash1", Role.NORMAL);
        User userModel2 = new User(2L, "user2","test@gmail.com",  "hash2", Role.ADMIN);

        UserDto userDto1 = new UserDto(1L, "user1", "test@gmail.com", null, "hash1", Role.NORMAL);
        UserDto userDto2 = new UserDto(2L, "user2", "test@gmail.com", null, "hash2", Role.ADMIN);
        List<UserDto> expectedUserDtos = List.of(userDto1, userDto2);

        when(userRepository.findAll(1, 10)).thenReturn(page);
        when(userMapperMock.fromUserEntityToUser(userEntity1)).thenReturn(userModel1);
        when(userMapperMock.fromUserToUserDto(userModel1)).thenReturn(userDto1);
        when(userMapperMock.fromUserEntityToUser(userEntity2)).thenReturn(userModel2);
        when(userMapperMock.fromUserToUserDto(userModel2)).thenReturn(userDto2);

        List<UserDto> result = userService.findAll(1, 10).data();

        assertEquals(expectedUserDtos, result);
    }

    @Test
    void findAll_whenNoUsersExist_shouldReturnEmptyList() {
        when(userRepository.findAll(1, 10)).thenReturn(new Page<UserEntity>(Collections.emptyList(), 1, 0, 0));

        Page<UserDto> result = userService.findAll(1, 10);

        assertNotNull(result);
        assertTrue(result.data().isEmpty());
        verify(userRepository, times(1)).findAll(1, 10);
        verify(userMapperMock, never()).fromUserEntityToUser(any(UserEntity.class));
        verify(userMapperMock, never()).fromUserToUserDto(any(User.class));
    }

    // INTERACTION / VERIFY
    @Test
    void create_shouldCallRepositorySaveOnce() {
        UserDto userDtoToCreate = new UserDto(null, "newUser", "test@gmail.com", "plainPassword123", null, Role.NORMAL);
        User userModel = new User(null, "newUser", "test@gmail.com", "hashedPassword", Role.NORMAL);
        UserEntity userEntityToSave = new UserEntity(null, "newUser", "test@gmail.com", "hashedPassword", Role.NORMAL);
        UserEntity savedUserEntity = new UserEntity(1L, "newUser", "test@gmail.com", "hashedPassword", Role.NORMAL);
        User savedUserModel = new User(1L, "newUser", "test@gmail.com", "hashedPassword", Role.NORMAL);
        UserDto expectedUserDto = new UserDto(1L, "newUser", "test@gmail.com", null, "hashedPassword", Role.NORMAL);

        when(userRepository.findByName(userDtoToCreate.name())).thenReturn(Collections.emptyList());
        when(passwordEncoderService.encode(userDtoToCreate.plainPassword())).thenReturn("hashedPassword");
        
        when(userMapperMock.fromUserDtoToUser(any(UserDto.class))).thenReturn(userModel);
        when(userMapperMock.fromUserToUserEntity(any(User.class))).thenReturn(userEntityToSave);
        when(userRepository.save(userEntityToSave)).thenReturn(savedUserEntity);
        when(userMapperMock.fromUserEntityToUser(any(UserEntity.class))).thenReturn(savedUserModel);
        when(userMapperMock.fromUserToUserDto(any(User.class))).thenReturn(expectedUserDto);

        userService.create(userDtoToCreate);

        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void delete_shouldCallRepositoryDeleteOnce() {
        Long userId = 1L;
        UserEntity userEntity = new UserEntity(userId, "userToDelete", "test@gmail.com", "someHash", Role.NORMAL);
        User userModel = new User(userId, "userToDelete", "test@gmail.com", "someHash", Role.NORMAL);
        UserDto userDto = new UserDto(userId, "userToDelete", "test@gmail.com", null, "someHash", Role.NORMAL);

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(userMapperMock.fromUserEntityToUser(userEntity)).thenReturn(userModel);
        when(userMapperMock.fromUserToUserDto(userModel)).thenReturn(userDto);
        doNothing().when(userRepository).delete(userId);

        userService.delete(userId);

        verify(userRepository, times(1)).delete(userId);
    }

    @Test
    void logByEmail_shouldCallCreateSessionTokenOnce() {
        String name = "testUser";
        String email = "test@gmail.com";
        String plainPassword = "plainPassword";
        String hashedPassword = "hashedPassword";
        String sessionToken = "randomSessionToken";

        UserEntity userEntity = new UserEntity(1L, name, email, hashedPassword, Role.NORMAL);
        User userModel = new User(1L, name, email, hashedPassword, Role.NORMAL);
        UserDto userDto = new UserDto(1L, name, email, null, hashedPassword, Role.NORMAL);

        when(userRepository.findByEmail(name)).thenReturn(List.of(userEntity));
        when(userMapperMock.fromUserEntityToUser(userEntity)).thenReturn(userModel);
        when(userMapperMock.fromUserToUserDto(userModel)).thenReturn(userDto);
        when(passwordEncoderService.verify(plainPassword, hashedPassword)).thenReturn(true);
        when(userRepository.createSessionToken(userDto.id())).thenReturn(sessionToken);

        userService.logByEmail(name, plainPassword);

        verify(userRepository, times(1)).createSessionToken(userDto.id());
    }

    // SECURITY / BUSINESS RULES
    @Test
    void create_shouldNotStorePlainPasswordInHash() {
        String plainPassword = "plainPassword123";
        String hashedPassword = "hashedPassword";
        UserDto userDtoToCreate = new UserDto(null, "newUser", "test@gmail.com", plainPassword, null, Role.NORMAL);
        User userModel = new User(null, "newUser", "test@gmail.com", hashedPassword, Role.NORMAL);
        UserEntity userEntityToSave = new UserEntity(null, "newUser", "test@gmail.com", hashedPassword, Role.NORMAL);
        UserEntity savedUserEntity = new UserEntity(1L, "newUser", "test@gmail.com", hashedPassword, Role.NORMAL);
        User savedUserModel = new User(1L, "newUser", "test@gmail.com", hashedPassword, Role.NORMAL);
        UserDto expectedUserDto = new UserDto(1L, "newUser", "test@gmail.com", null, hashedPassword, Role.NORMAL);

        when(userRepository.findByName(userDtoToCreate.name())).thenReturn(Collections.emptyList());
        when(passwordEncoderService.encode(userDtoToCreate.plainPassword())).thenReturn(hashedPassword);
        
        when(userMapperMock.fromUserDtoToUser(any(UserDto.class))).thenReturn(userModel);
        when(userMapperMock.fromUserToUserEntity(any(User.class))).thenReturn(userEntityToSave);
        when(userRepository.save(userEntityToSave)).thenReturn(savedUserEntity);
        when(userMapperMock.fromUserEntityToUser(any(UserEntity.class))).thenReturn(savedUserModel);
        when(userMapperMock.fromUserToUserDto(any(User.class))).thenReturn(expectedUserDto);

        UserDto createdUser = userService.create(userDtoToCreate);

        assertNotNull(createdUser.passwordHash());
        assertNotEquals(plainPassword, createdUser.passwordHash());
    }

    @Test
    void delete_shouldPreventDeletingAdminUser() {
        Long userId = 1L;
        UserEntity adminUserEntity = new UserEntity(userId, "adminUser", "test@gmail.com", "someHash", Role.ADMIN);
        User adminUserModel = new User(userId, "adminUser", "test@gmail.com", "someHash", Role.ADMIN);
        UserDto adminUserDto = new UserDto(userId, "adminUser", "test@gmail.com", null, "someHash", Role.ADMIN);

        when(userRepository.findById(userId)).thenReturn(Optional.of(adminUserEntity));
        when(userMapperMock.fromUserEntityToUser(adminUserEntity)).thenReturn(adminUserModel);
        when(userMapperMock.fromUserToUserDto(adminUserModel)).thenReturn(adminUserDto);
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.delete(userId));

        assertEquals("Cannot delete an ADMIN user.", exception.getMessage());
        verify(userRepository, never()).delete(anyLong());
    }

    // TRANSACTIONAL
    @Test
    void create_whenExceptionOccurs_shouldRollbackTransaction() {
        UserDto userDtoToCreate = new UserDto(null, "newUser", "test@gmail.com", "plainPassword123", null, Role.NORMAL);

        when(userRepository.findByName(userDtoToCreate.name())).thenReturn(Collections.emptyList());
        when(passwordEncoderService.encode(userDtoToCreate.plainPassword())).thenThrow(new RuntimeException("Simulated DB error during encoding"));

        Exception exception = assertThrows(RuntimeException.class, () -> userService.create(userDtoToCreate));

        assertEquals("Simulated DB error during encoding", exception.getMessage());
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    void delete_whenExceptionOccurs_shouldRollbackTransaction() {
        Long userId = 1L;
        UserEntity userEntity = new UserEntity(userId, "userToDelete", "test@gmail.com", "someHash", Role.NORMAL);
        User userModel = new User(userId, "userToDelete", "test@gmail.com", "someHash", Role.NORMAL);
        UserDto userDto = new UserDto(userId, "userToDelete", "test@gmail.com", null, "someHash", Role.NORMAL);

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(userMapperMock.fromUserEntityToUser(userEntity)).thenReturn(userModel);
        when(userMapperMock.fromUserToUserDto(userModel)).thenReturn(userDto);
        doThrow(new RuntimeException("Simulated DB error during delete")).when(userRepository).delete(userId);

        Exception exception = assertThrows(RuntimeException.class, () -> userService.delete(userId));

        assertEquals("Simulated DB error during delete", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).delete(userId);
    }

    @Test
    void update_whenExceptionOccurs_shouldRollbackTransaction() {
        Long userId = 1L;
        UserDto userDtoToUpdate = new UserDto(userId, "updatedName", "test@gmail.com", null, "newHashedPassword", Role.ADMIN);
        
        User userModel = new User(userId, "updatedName", "test@gmail.com", "newHashedPassword", Role.ADMIN);
        UserEntity userEntityToSave = new UserEntity(userId, "updatedName", "test@gmail.com", "newHashedPassword", Role.ADMIN);

        when(userRepository.findById(userId)).thenReturn(Optional.of(new UserEntity(userId, "oldName", "test@gmail.com", "oldHashedPassword", Role.NORMAL)));
        
        when(userMapperMock.fromUserEntityToUser(any(UserEntity.class)))
                .thenReturn(new User(userId, "oldName", "test@gmail.com", "oldHashedPassword", Role.NORMAL));
        when(userMapperMock.fromUserToUserDto(any(User.class)))
                .thenReturn(new UserDto(userId, "oldName", "test@gmail.com", null, "oldHashedPassword", Role.NORMAL));
        
        when(userMapperMock.fromUserDtoToUser(userDtoToUpdate)).thenReturn(userModel);
        when(userMapperMock.fromUserToUserEntity(userModel)).thenReturn(userEntityToSave);
        doThrow(new RuntimeException("Simulated DB error during update")).when(userRepository).save(userEntityToSave);

        Exception exception = assertThrows(RuntimeException.class, () -> userService.update(userDtoToUpdate));

        assertEquals("Simulated DB error during update", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(userEntityToSave);
    }
}
