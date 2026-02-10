package com.fpmislata.back.persistence.repository.impl;

import java.util.List;
import java.util.Optional;

import com.fpmislata.back.domain.repository.OrderRepository;
import com.fpmislata.back.domain.repository.entity.OrderEntity;
import com.fpmislata.back.persistence.dao.OrderDao;
import com.fpmislata.back.persistence.dao.impl.entity.OrderJpaEntity;
import com.fpmislata.back.persistence.repository.mapper.OrderMapper;

public class OrderRepositoryImpl implements OrderRepository {

    private final OrderDao orderDao;

    public OrderRepositoryImpl(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    public Optional<OrderEntity> findByUserIdAndEstado(Long userId, String estado) {
        return orderDao.findByUserIdAndEstado(userId, estado)
                .map(OrderMapper.getInstance()::fromOrderJpaEntityToOrderEntity);
    }

    @Override
    public List<OrderEntity> findByUserId(Long userId) {
        return orderDao.findByUserId(userId).stream()
                .map(OrderMapper.getInstance()::fromOrderJpaEntityToOrderEntity)
                .toList();
    }

    @Override
    public List<OrderEntity> findByUserIdExcludingEstado(Long userId, String estado) {
        return orderDao.findByUserIdExcludingEstado(userId, estado).stream()
                .map(OrderMapper.getInstance()::fromOrderJpaEntityToOrderEntity)
                .toList();
    }

    @Override
    public Optional<OrderEntity> findById(Long id) {
        return orderDao.findById(id)
                .map(OrderMapper.getInstance()::fromOrderJpaEntityToOrderEntity);
    }

    @Override
    public OrderEntity save(OrderEntity orderEntity) {
        OrderJpaEntity jpaEntity = OrderMapper.getInstance().fromOrderEntityToOrderJpaEntity(orderEntity);
        OrderJpaEntity saved;
        if (orderEntity.id() == null) {
            saved = orderDao.insert(jpaEntity);
        } else {
            saved = orderDao.update(jpaEntity);
        }
        return OrderMapper.getInstance().fromOrderJpaEntityToOrderEntity(saved);
    }

    @Override
    public void deleteById(Long id) {
        orderDao.deleteById(id);
    }

    @Override
    public void deleteItemsByOrderId(Long orderId) {
        orderDao.deleteItemsByOrderId(orderId);
    }
}
