package com.fpmislata.back.spring;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fpmislata.back.domain.repository.UserRepository;
import com.fpmislata.back.domain.service.UserService;
import com.fpmislata.back.domain.service.impl.UserServiceImpl;
import com.fpmislata.back.infrastructure.PasswordEncoderImpl;
import com.fpmislata.back.persitence.dao.UserDao;
import com.fpmislata.back.persitence.dao.impl.UserDaoJpa;
import com.fpmislata.back.persitence.repository.impl.UserRepositoryImpl;

@Configuration
@EntityScan(basePackages = { "com.fpmislata.back.persitence.dao.impl.entity" })
public class SpringConfig {
    @Bean
    public UserDao userDao() {
        return new UserDaoJpa();
    }

    @Bean
    public UserRepository userRepository(UserDao userDao) {
        return new UserRepositoryImpl(userDao);
    }

    @Bean
    public UserService userService(UserRepository userRepository, PasswordEncoderImpl passwordEncoderImpl) {
        return new UserServiceImpl(userRepository, passwordEncoderImpl);
    }

}
