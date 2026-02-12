package com.fpmislata.back.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fpmislata.back.web.mapper.CartMapper;
import com.fpmislata.back.web.mapper.OrderMapper;
import com.fpmislata.back.web.webModel.request.AddCartItemRequest;
import com.fpmislata.back.web.webModel.request.CheckoutRequest;
import com.fpmislata.back.web.webModel.request.PayWithCardRequest;
import com.fpmislata.back.web.webModel.request.RemoveCartItemRequest;
import com.fpmislata.back.web.webModel.request.UpdateCartItemRequest;
import com.fpmislata.back.web.webModel.response.CartResponse;
import com.fpmislata.back.web.webModel.response.OrderResponse;
import com.fpmislata.back.web.webModel.response.PaymentResponse;
import com.fpmislata.back.domain.service.CartService;
import com.fpmislata.back.domain.service.dto.CartDto;
import com.fpmislata.back.domain.service.dto.OrderDto;

@RestController
@RequestMapping("/api/cart")
public class CartController {

  private final CartService cartService;

  public CartController(CartService cartService) {
    this.cartService = cartService;
  }

  @GetMapping
  public ResponseEntity<CartResponse> getActiveCart(@RequestAttribute Long authenticatedUserId) {
    CartDto cart = cartService.getActiveCartByUserId(authenticatedUserId);
    return ResponseEntity.ok(CartMapper.getInstance().fromCartDtoToCartResponse(cart));
  }

  @PostMapping
  public ResponseEntity<CartResponse> createCart(@RequestAttribute Long authenticatedUserId) {
    CartDto cart = cartService.createCartForUser(authenticatedUserId);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(CartMapper.getInstance().fromCartDtoToCartResponse(cart));
  }

  @PostMapping("/items")
  public ResponseEntity<CartResponse> addItem(
      @RequestAttribute Long authenticatedUserId,
      @RequestBody AddCartItemRequest request) {
    CartDto cart = cartService.addItemToCart(authenticatedUserId, request.productId(), request.quantity());
    return ResponseEntity.ok(CartMapper.getInstance().fromCartDtoToCartResponse(cart));
  }

  @DeleteMapping("/items")
  public ResponseEntity<CartResponse> removeItem(
      @RequestAttribute Long authenticatedUserId,
      @RequestBody RemoveCartItemRequest request) {
    CartDto cart = cartService.removeItemFromCart(authenticatedUserId, request.productId(), request.quantity());
    return ResponseEntity.ok(CartMapper.getInstance().fromCartDtoToCartResponse(cart));
  }

  @PutMapping("/items")
  public ResponseEntity<CartResponse> updateItemQuantity(
      @RequestAttribute Long authenticatedUserId,
      @RequestBody UpdateCartItemRequest request) {
    CartDto cart = cartService.updateItemQuantity(authenticatedUserId, request.productId(), request.quantity());
    return ResponseEntity.ok(CartMapper.getInstance().fromCartDtoToCartResponse(cart));
  }

  @DeleteMapping("/clear")
  public ResponseEntity<Void> clearCart(@RequestAttribute Long authenticatedUserId) {
    cartService.clearCart(authenticatedUserId);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{cartId}")
  public ResponseEntity<Void> deleteCart(@PathVariable Long cartId) {
    cartService.deleteCartById(cartId);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/checkout")
  public ResponseEntity<OrderResponse> checkout(
      @RequestAttribute Long authenticatedUserId,
      @RequestBody CheckoutRequest request) {
    OrderDto order = cartService.checkout(authenticatedUserId, request.address());
    return ResponseEntity.ok(OrderMapper.getInstance().fromOrderDtoToOrderResponse(order));
  }

  @PostMapping("/pay")
  public ResponseEntity<PaymentResponse> payWithCard(
      @RequestAttribute Long authenticatedUserId,
      @RequestBody PayWithCardRequest request) {

    OrderDto order = cartService.payWithCard(
        authenticatedUserId,
        request.address(),
        request.cardNumber(),
        request.expiryMonth(),
        request.expiryYear(),
        request.cvc(),
        request.fullName(),
        request.accountIban());

    OrderResponse orderResponse = OrderMapper.getInstance().fromOrderDtoToOrderResponse(order);
    PaymentResponse paymentResponse = new PaymentResponse(
        orderResponse.id(),
        orderResponse.estado(),
        orderResponse.address(),
        orderResponse.orderDate(),
        orderResponse.items(),
        orderResponse.totalItems(),
        orderResponse.totalPrice(),
        "Pago realizado con éxito");

    return ResponseEntity.ok(paymentResponse);
  }
}
