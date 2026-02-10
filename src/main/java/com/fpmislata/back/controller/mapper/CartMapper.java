package com.fpmislata.back.controller.mapper;

import com.fpmislata.back.controller.webModel.response.CartItemResponse;
import com.fpmislata.back.controller.webModel.response.CartResponse;
import com.fpmislata.back.domain.service.dto.CartDto;
import com.fpmislata.back.domain.service.dto.OrderItemDto;

import java.math.BigDecimal;
import java.util.List;

public class CartMapper {

    private static CartMapper instance;

    private CartMapper() {}

    public static CartMapper getInstance() {
        if (instance == null) {
            instance = new CartMapper();
        }
        return instance;
    }

    public CartResponse fromCartDtoToCartResponse(CartDto cartDto) {
        if (cartDto == null) return null;

        List<CartItemResponse> items = cartDto.products().stream()
                .map(this::fromOrderItemDtoToCartItemResponse)
                .toList();

        int totalItems = cartDto.products().stream()
                .mapToInt(OrderItemDto::quantity)
                .sum();

        return new CartResponse(
                cartDto.id(),
                cartDto.userId(),
                items,
                totalItems,
                cartDto.totalPrice()
        );
    }

    public CartItemResponse fromOrderItemDtoToCartItemResponse(OrderItemDto itemDto) {
        if (itemDto == null) return null;

        BigDecimal subtotal = itemDto.itemPrice().multiply(BigDecimal.valueOf(itemDto.quantity()));

        return new CartItemResponse(
                itemDto.id(),
                itemDto.productDto().id(),
                itemDto.productDto().name(),
                itemDto.productDto().image(),
                itemDto.quantity(),
                itemDto.itemPrice(),
                subtotal
        );
    }
}
