package com.fpmislata.back.domain.service.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import jakarta.validation.constraints.NotNull;

public record OrderDto(
    Long id,
    @NotNull(message = "products cannot be null") List<OrderItemDto> products,
    String estado,
    @NotNull(message = "address cannot be null") String address,
    @NotNull(message = "orderDate cannot be null") Date orderDate,
    @NotNull(message = "totalPrice cannot be null") BigDecimal totalPrice,
    Long userId) {
}
