package com.fpmislata.back.persistence.dao;

import com.fpmislata.back.persistence.dao.impl.entity.OrderJpaEntity;

import java.util.List;
import java.util.Optional;

public interface OrderDao {
    Optional<OrderJpaEntity> findById(Long id);
    Optional<OrderJpaEntity> findByUserIdAndEstado(Long userId, String estado);
    List<OrderJpaEntity> findByUserId(Long userId);
    List<OrderJpaEntity> findByUserIdExcludingEstado(Long userId, String estado);
    OrderJpaEntity insert(OrderJpaEntity entity);
    OrderJpaEntity update(OrderJpaEntity entity);
    void deleteById(Long id);
    void deleteItemsByOrderId(Long orderId);
}
