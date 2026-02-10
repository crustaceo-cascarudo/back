package com.fpmislata.back.domain.service.impl;

import java.util.List;

import com.fpmislata.back.domain.Exception.BusinessException;
import com.fpmislata.back.domain.Exception.ResourceNotFoundException;
import com.fpmislata.back.domain.enumerado.Estado;
import com.fpmislata.back.domain.mapper.OrderMapper;
import com.fpmislata.back.domain.model.Order;
import com.fpmislata.back.domain.repository.OrderRepository;
import com.fpmislata.back.domain.repository.entity.OrderEntity;
import com.fpmislata.back.domain.service.OrderService;
import com.fpmislata.back.domain.service.dto.OrderDto;

import jakarta.transaction.Transactional;

public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<OrderDto> getOrdersByUserId(Long userId) {
        List<OrderEntity> orderEntities = orderRepository.findByUserIdExcludingEstado(userId, Estado.CARRITO.name());
        return orderEntities.stream()
                .map(OrderMapper.getInstance()::fromOrderEntityToOrder)
                .map(OrderMapper.getInstance()::fromOrderToOrderDto)
                .toList();
    }

    @Override
    public OrderDto getOrderById(Long id) {
        OrderEntity orderEntity = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con id: " + id));
        if (Estado.CARRITO.name().equals(orderEntity.estado())) {
            throw new BusinessException("El registro con id " + id + " es un carrito, no un pedido");
        }
        Order order = OrderMapper.getInstance().fromOrderEntityToOrder(orderEntity);
        return OrderMapper.getInstance().fromOrderToOrderDto(order);
    }

    @Override
    @Transactional
    public OrderDto markAsShipped(Long orderId) {
        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con id: " + orderId));

        if (!Estado.PAGADO.name().equals(orderEntity.estado())) {
            throw new BusinessException("Solo se puede marcar como enviado un pedido en estado PAGADO. Estado actual: " + orderEntity.estado());
        }

        OrderEntity updatedOrder = new OrderEntity(
                orderEntity.id(), orderEntity.products(), Estado.ENVIADO.name(),
                orderEntity.address(), orderEntity.orderDate(),
                orderEntity.totalPrice(), orderEntity.userId()
        );
        OrderEntity saved = orderRepository.save(updatedOrder);
        return OrderMapper.getInstance().fromOrderToOrderDto(
                OrderMapper.getInstance().fromOrderEntityToOrder(saved)
        );
    }

    @Override
    @Transactional
    public OrderDto cancelOrder(Long orderId) {
        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con id: " + orderId));

        if (Estado.ENVIADO.name().equals(orderEntity.estado()) || Estado.CANCELADO.name().equals(orderEntity.estado())) {
            throw new BusinessException("No se puede cancelar un pedido en estado " + orderEntity.estado());
        }

        if (Estado.CARRITO.name().equals(orderEntity.estado())) {
            throw new BusinessException("No se puede cancelar un carrito. Use la operación de eliminar carrito");
        }

        OrderEntity updatedOrder = new OrderEntity(
                orderEntity.id(), orderEntity.products(), Estado.CANCELADO.name(),
                orderEntity.address(), orderEntity.orderDate(),
                orderEntity.totalPrice(), orderEntity.userId()
        );
        OrderEntity saved = orderRepository.save(updatedOrder);
        return OrderMapper.getInstance().fromOrderToOrderDto(
                OrderMapper.getInstance().fromOrderEntityToOrder(saved)
        );
    }
}
