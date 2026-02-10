package com.fpmislata.back.domain.repository.entity;

import java.math.BigDecimal;

public record OrderItemEntity(
    Long id,
    ProductEntity productEntity,
    Integer quantity,
    BigDecimal itemPrice
) {

}
