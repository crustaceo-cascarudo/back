package com.fpmislata.back.domain.service.dto;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.NotNull;

public record CartDto(
    Long id,
    @NotNull(message = "userId cannot be null") Long userId,
    @NotNull(message = "products cannot be null") List<OrderItemDto> products,
    String estado,
    @NotNull(message = "totalPrice cannot be null") BigDecimal totalPrice) {

}
