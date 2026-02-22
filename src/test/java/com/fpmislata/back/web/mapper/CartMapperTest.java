package com.fpmislata.back.web.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.fpmislata.back.domain.service.dto.CartDto;
import com.fpmislata.back.domain.service.dto.OrderItemDto;
import com.fpmislata.back.domain.service.dto.ProductDto;
import com.fpmislata.back.web.webModel.response.CartItemResponse;
import com.fpmislata.back.web.webModel.response.CartResponse;

class CartMapperTest {

    private final CartMapper mapper = CartMapper.getInstance();

    @Test
    void fromCartDtoToCartResponse_validInput_mapsAllFields() {
        ProductDto productDto = new ProductDto(1L, "Burger", List.of(), 10.0, null, 10.0, "img.png", List.of());
        OrderItemDto itemDto = new OrderItemDto(1L, productDto, 3, BigDecimal.TEN);
        CartDto cartDto = new CartDto(1L, 1L, List.of(itemDto), "CARRITO", BigDecimal.valueOf(30));

        CartResponse response = mapper.fromCartDtoToCartResponse(cartDto);

        assertEquals(1L, response.id());
        assertEquals(1L, response.userId());
        assertEquals(BigDecimal.valueOf(30), response.totalPrice());
        assertEquals(3, response.totalItems());
        assertEquals(1, response.items().size());
    }

    @Test
    void fromCartDtoToCartResponse_null_returnsNull() {
        assertNull(mapper.fromCartDtoToCartResponse(null));
    }

    @Test
    void fromCartDtoToCartResponse_emptyProducts_zeroTotalItems() {
        CartDto cartDto = new CartDto(1L, 1L, List.of(), "CARRITO", BigDecimal.ZERO);

        CartResponse response = mapper.fromCartDtoToCartResponse(cartDto);

        assertEquals(0, response.totalItems());
        assertTrue(response.items().isEmpty());
    }

    @Test
    void fromOrderItemDtoToCartItemResponse_validInput_calculatesSubtotal() {
        ProductDto productDto = new ProductDto(1L, "Burger", List.of(), 10.0, null, 10.0, "img.png", List.of());
        OrderItemDto itemDto = new OrderItemDto(1L, productDto, 2, BigDecimal.valueOf(10));

        CartItemResponse response = mapper.fromOrderItemDtoToCartItemResponse(itemDto);

        assertEquals(1L, response.id());
        assertEquals(1L, response.productId());
        assertEquals("Burger", response.productName());
        assertEquals("img.png", response.productImage());
        assertEquals(2, response.quantity());
        assertEquals(BigDecimal.valueOf(10), response.unitPrice());
        assertEquals(BigDecimal.valueOf(20), response.subtotal());
    }

    @Test
    void fromOrderItemDtoToCartItemResponse_null_returnsNull() {
        assertNull(mapper.fromOrderItemDtoToCartItemResponse(null));
    }
}
