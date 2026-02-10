package com.fpmislata.back.domain.service;

import com.fpmislata.back.domain.service.dto.CartDto;
import com.fpmislata.back.domain.service.dto.OrderDto;

public interface CartService {
  CartDto getActiveCartByUserId(Long userId);
  CartDto createCartForUser(Long userId);
  CartDto addItemToCart(Long userId, Long productId, int quantity);
  CartDto removeItemFromCart(Long userId, Long productId, int quantity);
  CartDto updateItemQuantity(Long userId, Long productId, int quantity);
  void clearCart(Long userId);
  void deleteCartById(Long id);
  OrderDto checkout(Long userId, String address);
}
