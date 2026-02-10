package com.fpmislata.back.controller.webModel.response;

import java.math.BigDecimal;

public record CartItemResponse(
    Long id,
    Long productId,
    String productName,
    String productImage,
    Integer quantity,
    BigDecimal unitPrice,
    BigDecimal subtotal
) {
}
