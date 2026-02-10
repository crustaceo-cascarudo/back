package com.fpmislata.back.domain.mapper;

import com.fpmislata.back.domain.enumerado.Estado;
import com.fpmislata.back.domain.model.Cart;
import com.fpmislata.back.domain.repository.entity.CartEntity;
import com.fpmislata.back.domain.repository.entity.OrderEntity;
import com.fpmislata.back.domain.service.dto.CartDto;

public class CartMapper {

    private static CartMapper instance;

    private CartMapper() {}

    public static CartMapper getInstance() {
        if (instance == null) {
            instance = new CartMapper();
        }
        return instance;
    }

    public CartDto fromCartToCartDto(Cart cart) {
        if (cart == null) return null;

        return new CartDto(
                cart.getId(),
                cart.getUserId(),
                cart.getProducts().stream()
                        .map(OrderItemMapper.getInstance()::fromModelToDto)
                        .toList(),
                cart.getEstado() != null ? cart.getEstado().name() : null,
                cart.getTotalPrice()
        );
    }

    public Cart fromCartDtoToCart(CartDto cartDto) {
        if (cartDto == null) return null;

        return new Cart(
                cartDto.id(),
                cartDto.userId(),
                cartDto.products().stream()
                        .map(OrderItemMapper.getInstance()::fromDtoToModel)
                        .toList(),
                cartDto.estado() != null ? Estado.valueOf(cartDto.estado()) : null,
                cartDto.totalPrice()
        );
    }

    public CartEntity fromCartToCartEntity(Cart cart) {
        if (cart == null) return null;

        return new CartEntity(
                cart.getId(),
                cart.getUserId(),
                cart.getProducts().stream()
                        .map(OrderItemMapper.getInstance()::fromModelToEntity)
                        .toList(),
                cart.getEstado() != null ? cart.getEstado().name() : null,
                cart.getTotalPrice()
        );
    }

    public Cart fromCartEntityToCart(CartEntity cartEntity) {
        if (cartEntity == null) return null;

        return new Cart(
                cartEntity.id(),
                cartEntity.userId(),
                cartEntity.products().stream()
                        .map(OrderItemMapper.getInstance()::fromEntityToModel)
                        .toList(),
                cartEntity.estado() != null ? Estado.valueOf(cartEntity.estado()) : null,
                cartEntity.totalPrice()
        );
    }

    public CartDto fromOrderEntityToCartDto(OrderEntity orderEntity) {
        if (orderEntity == null) return null;
        CartEntity cartEntity = new CartEntity(
                orderEntity.id(), orderEntity.userId(),
                orderEntity.products(), orderEntity.estado(),
                orderEntity.totalPrice()
        );
        return fromCartToCartDto(fromCartEntityToCart(cartEntity));
    }
}