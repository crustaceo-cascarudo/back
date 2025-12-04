package com.fpmislata.back.persistence.dao;

public interface GenericDao<T>{
    Long insert(T jpaEntity);
    T getById(Long id);
    void update(T jpaEntity);
    void delete(Long id);
    
}
