package com.fpmislata.back.domain.repository;

import java.util.List;
import java.util.Optional;

import com.fpmislata.back.domain.repository.entity.OrderEntity;

public interface OrderRepository {
    Optional<OrderEntity> findByUserIdAndEstado(Long userId, String estado);
    List<OrderEntity> findByUserId(Long userId);
    List<OrderEntity> findByUserIdExcludingEstado(Long userId, String estado);
    Optional<OrderEntity> findById(Long id);
    OrderEntity save(OrderEntity orderEntity);
    void deleteById(Long id);
    void deleteItemsByOrderId(Long orderId);
}
