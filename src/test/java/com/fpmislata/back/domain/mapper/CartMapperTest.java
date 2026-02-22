package com.fpmislata.back.domain.mapper;

import com.fpmislata.back.domain.enumerado.Estado;
import com.fpmislata.back.domain.model.Cart;
import com.fpmislata.back.domain.model.OrderItem;
import com.fpmislata.back.domain.model.Product;
import com.fpmislata.back.domain.repository.entity.*;
import com.fpmislata.back.domain.service.dto.CartDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CartMapperTest {

    private final CartMapper mapper = CartMapper.getInstance();

    @Test
    void fromCartToCartDto_shouldMapAllFields() {
        Product product = new Product(1L, "Pizza", List.of(), 10.0, 0, "pizza.jpg", List.of());
        OrderItem item = new OrderItem(1L, product, 2, BigDecimal.TEN);
        Cart cart = new Cart(1L, 1L, List.of(item), Estado.CARRITO, BigDecimal.valueOf(20));

        CartDto dto = mapper.fromCartToCartDto(cart);

        assertEquals(1L, dto.id());
        assertEquals(1L, dto.userId());
        assertEquals("CARRITO", dto.estado());
        assertEquals(BigDecimal.valueOf(20), dto.totalPrice());
        assertEquals(1, dto.products().size());
    }

    @Test
    void fromCartToCartDto_null_shouldReturnNull() {
        assertNull(mapper.fromCartToCartDto(null));
    }

    @Test
    void fromCartDtoToCart_shouldMapAllFields() {
        com.fpmislata.back.domain.service.dto.ProductDto productDto =
                new com.fpmislata.back.domain.service.dto.ProductDto(1L, "Pizza", List.of(), 10.0, 0, 10.0, "pizza.jpg", List.of());
        com.fpmislata.back.domain.service.dto.OrderItemDto itemDto =
                new com.fpmislata.back.domain.service.dto.OrderItemDto(1L, productDto, 2, BigDecimal.TEN);
        CartDto dto = new CartDto(1L, 1L, List.of(itemDto), "CARRITO", BigDecimal.valueOf(20));

        Cart cart = mapper.fromCartDtoToCart(dto);

        assertEquals(1L, cart.getId());
        assertEquals(1L, cart.getUserId());
        assertEquals(Estado.CARRITO, cart.getEstado());
        assertEquals(1, cart.getProducts().size());
    }

    @Test
    void fromCartDtoToCart_null_shouldReturnNull() {
        assertNull(mapper.fromCartDtoToCart(null));
    }

    @Test
    void fromCartToCartEntity_shouldMapAllFields() {
        Product product = new Product(1L, "Pizza", List.of(), 10.0, 0, "pizza.jpg", List.of());
        OrderItem item = new OrderItem(1L, product, 2, BigDecimal.TEN);
        Cart cart = new Cart(1L, 1L, List.of(item), Estado.CARRITO, BigDecimal.valueOf(20));

        CartEntity entity = mapper.fromCartToCartEntity(cart);

        assertEquals(1L, entity.id());
        assertEquals(1L, entity.userId());
        assertEquals("CARRITO", entity.estado());
        assertEquals(1, entity.products().size());
    }

    @Test
    void fromCartEntityToCart_shouldMapAllFields() {
        ProductEntity pe = new ProductEntity(1L, "Pizza", List.of(), 10.0, 0, "pizza.jpg", List.of());
        OrderItemEntity itemEntity = new OrderItemEntity(1L, pe, 2, BigDecimal.TEN);
        CartEntity entity = new CartEntity(1L, 1L, List.of(itemEntity), "CARRITO", BigDecimal.valueOf(20));

        Cart cart = mapper.fromCartEntityToCart(entity);

        assertEquals(1L, cart.getId());
        assertEquals(1L, cart.getUserId());
        assertEquals(Estado.CARRITO, cart.getEstado());
        assertEquals(1, cart.getProducts().size());
    }

    @Test
    void fromOrderEntityToCartDto_shouldConvertOrderToCart() {
        ProductEntity pe = new ProductEntity(1L, "Pizza", List.of(), 10.0, 0, "pizza.jpg", List.of());
        OrderItemEntity itemEntity = new OrderItemEntity(1L, pe, 2, BigDecimal.TEN);
        OrderEntity orderEntity = new OrderEntity(1L, List.of(itemEntity), "CARRITO", null, new Date(), BigDecimal.valueOf(20), 1L);

        CartDto dto = mapper.fromOrderEntityToCartDto(orderEntity);

        assertEquals(1L, dto.id());
        assertEquals(1L, dto.userId());
        assertEquals("CARRITO", dto.estado());
        assertEquals(1, dto.products().size());
    }

    @Test
    void fromOrderEntityToCartDto_null_shouldReturnNull() {
        assertNull(mapper.fromOrderEntityToCartDto(null));
    }
}
