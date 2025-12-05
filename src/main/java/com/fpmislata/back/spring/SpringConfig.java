package com.fpmislata.back.spring;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fpmislata.back.domain.repository.CategoryRepository;
import com.fpmislata.back.domain.repository.UserRepository;
import com.fpmislata.back.domain.service.CategoryService;
import com.fpmislata.back.domain.service.UserService;
import com.fpmislata.back.domain.service.impl.CategoryServiceImpl;
import com.fpmislata.back.domain.service.impl.UserServiceImpl;
import com.fpmislata.back.infrastructure.PasswordEncoderImpl;
import com.fpmislata.back.persistence.dao.CategoryDao;
import com.fpmislata.back.persistence.dao.UserDao;
import com.fpmislata.back.persistence.dao.impl.CategoryDaoJpa;
import com.fpmislata.back.persistence.dao.impl.UserDaoJpa;
import com.fpmislata.back.persistence.repository.impl.CategoryRepositoryImpl;
import com.fpmislata.back.persistence.repository.impl.UserRepositoryImpl;

@Configuration
@EntityScan(basePackages = "com.fpmislata.back.persistence.dao.impl.entity")
public class SpringConfig {

    // User DAO Bean

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

    @Bean
    public PasswordEncoderImpl passwordEncoderImpl() {
        return new PasswordEncoderImpl();
    }

    // ----------------------------------------------

    // Category DAO Bean

    @Bean
    CategoryDao categoryDao() {
        return new CategoryDaoJpa();
    }

    @Bean
    CategoryRepository categoryRepository(CategoryDao categoryDao) {
        return new CategoryRepositoryImpl(categoryDao);
    }

    @Bean
    CategoryService categoryService(CategoryRepository categoryRepository) {
        return new CategoryServiceImpl(categoryRepository);
    }

    // ----------------------------------------------

    

}
