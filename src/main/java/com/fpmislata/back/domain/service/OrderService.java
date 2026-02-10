package com.fpmislata.back.domain.service;

import java.util.List;

import com.fpmislata.back.domain.service.dto.OrderDto;

public interface OrderService {
    List<OrderDto> getOrdersByUserId(Long userId);
    OrderDto getOrderById(Long id);
    OrderDto markAsShipped(Long orderId);
    OrderDto cancelOrder(Long orderId);
}