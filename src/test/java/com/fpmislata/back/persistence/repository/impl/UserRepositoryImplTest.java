package com.fpmislata.back.persistence.repository.impl;

import com.fpmislata.back.domain.enumerado.Role;
import com.fpmislata.back.domain.repository.entity.UserEntity;
import com.fpmislata.back.persistence.dao.UserDao;
import com.fpmislata.back.persistence.dao.impl.entity.UserJpaEntity;
import com.fpmislata.back.persistence.repository.mapper.UserMapper;
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
class UserRepositoryImplTest {

    @Mock
    private UserDao userDao;
    @Mock
    private UserMapper userMapperMock;

    @InjectMocks
    private UserRepositoryImpl userRepository;

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

    // DELETE
    @Test
    void delete_shouldCallUserDaoDelete() {
        Long userId = 1L;

        doNothing().when(userDao).delete(userId);

        userRepository.delete(userId);

        verify(userDao, times(1)).delete(userId);
    }

    // FIND ALL
    @Test
    void findAll_shouldReturnMappedUserEntities() {
        List<UserJpaEntity> jpaEntities = List.of(new UserJpaEntity(1L, "user1", "pass1", Role.NORMAL));
        when(userDao.findAll(0, 5)).thenReturn(jpaEntities);
        when(userMapperMock.fromUserJpaEntitytoUserEntity(jpaEntities.get(0)))
                .thenReturn(new UserEntity(1L, "user1", "pass1", Role.NORMAL));

        List<UserEntity> result = userRepository.findAll();

        assertEquals(1, result.size());
        assertEquals("user1", result.get(0).name());
        verify(userDao).findAll(0, 5);
        verify(userMapperMock).fromUserJpaEntitytoUserEntity(jpaEntities.get(0));
    }

    @Test
    void findAll_shouldCallUserDaoFindAllWithPagination() {
        when(userDao.findAll(0, 5)).thenReturn(Collections.emptyList());
        userRepository.findAll();
        verify(userDao).findAll(0, 5);
    }

    // FIND BY ID
    @Test
    void findById_whenUserExists_shouldReturnUserEntity() {
        Long userId = 1L;
        UserJpaEntity jpaEntity = new UserJpaEntity(userId, "user1", "pass1", Role.NORMAL);
        when(userDao.findById(userId)).thenReturn(Optional.of(jpaEntity));
        when(userMapperMock.fromUserJpaEntitytoUserEntity(jpaEntity))
                .thenReturn(new UserEntity(userId, "user1", "pass1", Role.NORMAL));

        Optional<UserEntity> result = userRepository.findById(userId);

        assertTrue(result.isPresent());
        assertEquals(userId, result.get().id());
    }

    @Test
    void findById_whenUserDoesNotExist_shouldReturnEmptyOptional() {
        Long userId = 1L;
        when(userDao.findById(userId)).thenReturn(Optional.empty());

        Optional<UserEntity> result = userRepository.findById(userId);

        assertTrue(result.isEmpty());
    }

    @Test
    void findById_shouldMapJpaEntityToDomainEntity() {
        Long userId = 1L;
        UserJpaEntity jpaEntity = new UserJpaEntity(userId, "user1", "pass1", Role.NORMAL);
        when(userDao.findById(userId)).thenReturn(Optional.of(jpaEntity));
        when(userMapperMock.fromUserJpaEntitytoUserEntity(jpaEntity))
                .thenReturn(new UserEntity(userId, "user1", "pass1", Role.NORMAL));

        userRepository.findById(userId);

        verify(userMapperMock).fromUserJpaEntitytoUserEntity(jpaEntity);
    }

    // FIND BY NAME
    @Test
    void findByName_whenUsersExist_shouldReturnMappedList() {
        String name = "test";
        List<UserJpaEntity> jpaEntities = List.of(new UserJpaEntity(1L, name, "pass1", Role.NORMAL));
        when(userDao.findByName(name)).thenReturn(jpaEntities);
        when(userMapperMock.fromUserJpaEntitytoUserEntity(jpaEntities.get(0)))
                .thenReturn(new UserEntity(1L, name, "pass1", Role.NORMAL));

        List<UserEntity> result = userRepository.findByName(name);

        assertEquals(1, result.size());
        assertEquals(name, result.get(0).name());
    }

    @Test
    void findByName_whenNoUsersExist_shouldReturnEmptyList() {
        String name = "test";
        when(userDao.findByName(name)).thenReturn(Collections.emptyList());

        List<UserEntity> result = userRepository.findByName(name);

        assertTrue(result.isEmpty());
    }

    @Test
    void findByName_shouldCallUserDaoFindByName() {
        String name = "test";
        when(userDao.findByName(name)).thenReturn(Collections.emptyList());

        userRepository.findByName(name);

        verify(userDao).findByName(name);
    }

    // LOG BY NAME
    @Test
    void logByName_whenUserExists_shouldReturnUserEntity() {
        String name = "test";
        List<UserJpaEntity> jpaEntities = List.of(new UserJpaEntity(1L, name, "pass1", Role.NORMAL));
        when(userDao.findByName(name)).thenReturn(jpaEntities);
        when(userMapperMock.fromUserJpaEntitytoUserEntity(jpaEntities.get(0)))
                .thenReturn(new UserEntity(1L, name, "pass1", Role.NORMAL));

        UserEntity result = userRepository.logByName(name);

        assertNotNull(result);
        assertEquals(name, result.name());
    }

    @Test
    void logByName_whenUserDoesNotExist_shouldReturnNull() {
        String name = "test";
        when(userDao.findByName(name)).thenReturn(Collections.emptyList());

        UserEntity result = userRepository.logByName(name);

        assertNull(result);
    }

    // SAVE
    @Test
    void save_whenIdIsNull_shouldInsertUser() {
        UserEntity userToSave = new UserEntity(null, "new", "pass", Role.NORMAL);
        UserJpaEntity jpaEntity = new UserJpaEntity(null, "new", "pass", Role.NORMAL);
        UserJpaEntity savedJpaEntity = new UserJpaEntity(1L, "new", "pass", Role.NORMAL);

        when(userMapperMock.fromUserEntitytoJpaEntity(userToSave)).thenReturn(jpaEntity);
        when(userDao.insert(jpaEntity)).thenReturn(savedJpaEntity);
        when(userMapperMock.fromUserJpaEntitytoUserEntity(savedJpaEntity))
                .thenReturn(new UserEntity(1L, "new", "pass", Role.NORMAL));

        UserEntity result = userRepository.save(userToSave);

        assertEquals(1L, result.id());
        verify(userDao).insert(jpaEntity);
        verify(userDao, never()).update(any());
    }

    @Test
    void save_whenIdExistsAndUserExists_shouldUpdateUser() {
        Long userId = 1L;
        UserEntity userToSave = new UserEntity(userId, "update", "pass", Role.NORMAL);
        UserJpaEntity jpaEntity = new UserJpaEntity(userId, "update", "pass", Role.NORMAL);

        when(userDao.findById(userId)).thenReturn(Optional.of(new UserJpaEntity()));
        when(userMapperMock.fromUserEntitytoJpaEntity(userToSave)).thenReturn(jpaEntity);

        UserEntity result = userRepository.save(userToSave);

        assertEquals(userToSave, result);
        verify(userDao).update(jpaEntity);
        verify(userDao, never()).insert(any());
    }
    
    @Test
    void save_whenIdExistsAndUserDoesNotExist_shouldInsertUser() {
        Long userId = 1L;
        UserEntity userToSave = new UserEntity(userId, "new", "pass", Role.NORMAL);
        UserJpaEntity jpaEntity = new UserJpaEntity(userId, "new", "pass", Role.NORMAL);
        UserJpaEntity savedJpaEntity = new UserJpaEntity(userId, "new", "pass", Role.NORMAL);
    
        when(userDao.findById(userId)).thenReturn(Optional.empty());
        when(userMapperMock.fromUserEntitytoJpaEntity(userToSave)).thenReturn(jpaEntity);
        when(userDao.insert(jpaEntity)).thenReturn(savedJpaEntity);
        when(userMapperMock.fromUserJpaEntitytoUserEntity(savedJpaEntity))
                .thenReturn(new UserEntity(userId, "new", "pass", Role.NORMAL));
    
        UserEntity result = userRepository.save(userToSave);
    
        assertEquals(userId, result.id());
        verify(userDao).insert(jpaEntity);
        verify(userDao, never()).update(any());
    }

    @Test
    void save_shouldMapDomainEntityToJpaEntity() {
        UserEntity userToSave = new UserEntity(null, "new", "pass", Role.NORMAL);
        UserJpaEntity jpaEntity = new UserJpaEntity(null, "new", "pass", Role.NORMAL);

        when(userMapperMock.fromUserEntitytoJpaEntity(userToSave)).thenReturn(jpaEntity);
        when(userDao.insert(jpaEntity)).thenReturn(new UserJpaEntity());

        userRepository.save(userToSave);

        verify(userMapperMock).fromUserEntitytoJpaEntity(userToSave);
    }

    // SESSION TOKEN
    @Test
    void createSessionToken_shouldCallUserDaoCreateSessionToken() {
        Long userId = 1L;
        String expectedToken = "token";
        when(userDao.createSessionToken(userId)).thenReturn(expectedToken);

        String token = userRepository.createSessionToken(userId);

        assertEquals(expectedToken, token);
        verify(userDao).createSessionToken(userId);
    }

    @Test
    void findByToken_whenTokenExists_shouldReturnUserEntity() {
        String token = "token";
        UserJpaEntity jpaEntity = new UserJpaEntity(1L, "user1", "pass1", Role.NORMAL);
        when(userDao.findByToken(token)).thenReturn(jpaEntity);
        when(userMapperMock.fromUserJpaEntitytoUserEntity(jpaEntity))
                .thenReturn(new UserEntity(1L, "user1", "pass1", Role.NORMAL));

        UserEntity result = userRepository.findByToken(token);

        assertNotNull(result);
        assertEquals(1L, result.id());
    }

    @Test
    void deleteSessionToken_shouldCallUserDaoDeleteToken() {
        String token = "token";
        doNothing().when(userDao).deleteToken(token);

        userRepository.deleteSessionToken(token);

        verify(userDao).deleteToken(token);
    }
}