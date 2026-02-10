package com.fpmislata.back.domain.repository.entity;

import java.math.BigDecimal;
import java.util.List;

public record CartEntity(
    Long id,
    Long userId,
    List<OrderItemEntity> products,
    String estado,
    BigDecimal totalPrice) {
}
