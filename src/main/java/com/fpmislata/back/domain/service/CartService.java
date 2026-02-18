package com.fpmislata.back.domain.service;

import com.fpmislata.back.domain.service.dto.CartDto;
import com.fpmislata.back.domain.service.dto.OrderDto;

public interface CartService {
  CartDto getActiveCartByUserId(Long userId);
  CartDto createCartForUser(Long userId);
  CartDto addItemToCart(Long userId, Long productId, int quantity);
  CartDto removeItemFromCart(Long userId, Long productId);
  CartDto updateItemQuantity(Long userId, Long productId, int quantity);
  void clearCart(Long userId);
  void deleteCartById(Long id);
  OrderDto checkout(Long userId, String address);
  OrderDto payWithCard(Long userId, String address, String cardNumber, String expiryMonth, String expiryYear, String cvc, String fullName, String accountIban);
}
