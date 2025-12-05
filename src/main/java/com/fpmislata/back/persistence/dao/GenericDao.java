package com.fpmislata.back.persistence.dao;

import java.util.List;
import java.util.Optional;

public interface GenericDao<T>{
    List<T> findAll(int pageNumber, int pageSize);
    T insert(T jpaEntity);
    Optional<T> findById(Long id);
    Optional<T> findByName(String name);
     T update(T jpaEntity);
    void delete(Long id);
    long count();
    
}
