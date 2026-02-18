package com.fpmislata.back.web.webModel.response;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public record PaymentResponse(
    Long orderId,
    String estado,
    String address,
    Date orderDate,
    List<OrderItemResponse> items,
    int totalItems,
    BigDecimal totalPrice,
    String message
) {
}
