package com.fpmislata.back.web.webModel.response;

import java.math.BigDecimal;
import java.util.List;

public record CartResponse(
    Long id,
    Long userId,
    List<CartItemResponse> items,
    int totalItems,
    BigDecimal totalPrice) {
}
