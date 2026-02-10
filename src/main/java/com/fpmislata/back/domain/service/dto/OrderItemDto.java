package com.fpmislata.back.domain.service.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

public record OrderItemDto(
    Long id,
    @NotNull(message = "product cannot be null")
    ProductDto productDto,
    @NotNull(message = "quantity cannot be null")
    Integer quantity,
    @NotNull(message = "itemPrice cannot be null")
    BigDecimal itemPrice
) {

}
