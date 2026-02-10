package com.fpmislata.back.controller.webModel.response;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public record OrderResponse(
    Long id,
    Long userId,
    String estado,
    String address,
    Date orderDate,
    List<OrderItemResponse> items,
    int totalItems,
    BigDecimal totalPrice
) {
}
