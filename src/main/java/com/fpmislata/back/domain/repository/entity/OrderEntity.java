package com.fpmislata.back.domain.repository.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


public record OrderEntity(
    Long id,
    List<OrderItemEntity> products,
    String estado,
    String address,
    Date orderDate,
    BigDecimal totalPrice,
    Long userId
) {

}
