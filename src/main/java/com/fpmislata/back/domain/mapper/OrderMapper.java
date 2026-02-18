package com.fpmislata.back.domain.mapper;

import com.fpmislata.back.domain.enumerado.Estado;
import com.fpmislata.back.domain.model.Order;
import com.fpmislata.back.domain.repository.entity.OrderEntity;
import com.fpmislata.back.domain.service.dto.OrderDto;

public class OrderMapper {

    private static OrderMapper instance;

    private OrderMapper() {}

    public static OrderMapper getInstance() {
        if (instance == null) {
            instance = new OrderMapper();
        }
        return instance;
    }

    public OrderDto fromOrderToOrderDto(Order order) {
        if (order == null) return null;

        return new OrderDto(
                order.getId(),
                order.getProducts().stream()
                        .map(OrderItemMapper.getInstance()::fromModelToDto)
                        .toList(),
                order.getEstado() != null ? order.getEstado().name() : null,
                order.getAddress(),
                order.getOrderDate(),
                order.getTotalPrice(),
                order.getUserId()
        );
    }

    public Order fromOrderDtoToOrder(OrderDto orderDto) {
        if (orderDto == null) return null;

        return new Order(
                orderDto.id(),
                orderDto.products().stream()
                        .map(OrderItemMapper.getInstance()::fromDtoToModel)
                        .toList(),
                orderDto.estado() != null ? Estado.valueOf(orderDto.estado()) : null,
                orderDto.address(),
                orderDto.orderDate(),
                orderDto.totalPrice(),
                orderDto.userId()
        );
    }

    public OrderEntity fromOrderToOrderEntity(Order order) {
        if (order == null) return null;

        return new OrderEntity(
                order.getId(),
                order.getProducts().stream()
                        .map(OrderItemMapper.getInstance()::fromModelToEntity)
                        .toList(),
                order.getEstado() != null ? order.getEstado().name() : null,
                order.getAddress(),
                order.getOrderDate(),
                order.getTotalPrice(),
                order.getUserId()
        );
    }

    public Order fromOrderEntityToOrder(OrderEntity orderEntity) {
        if (orderEntity == null) return null;

        return new Order(
                orderEntity.id(),
                orderEntity.products().stream()
                        .map(OrderItemMapper.getInstance()::fromEntityToModel)
                        .toList(),
                orderEntity.estado() != null ? Estado.valueOf(orderEntity.estado()) : null,
                orderEntity.address(),
                orderEntity.orderDate(),
                orderEntity.totalPrice(),
                orderEntity.userId()
        );
    }
}