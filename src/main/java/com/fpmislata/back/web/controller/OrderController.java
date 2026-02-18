package com.fpmislata.back.web.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fpmislata.back.web.mapper.OrderMapper;
import com.fpmislata.back.web.webModel.response.OrderResponse;
import com.fpmislata.back.domain.service.OrderService;
import com.fpmislata.back.domain.service.dto.OrderDto;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

  private final OrderService orderService;

  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @GetMapping("/user")
  public ResponseEntity<List<OrderResponse>> getOrdersByUser(@RequestAttribute Long authenticatedUserId) {
    List<OrderDto> orders = orderService.getOrdersByUserId(authenticatedUserId);
    List<OrderResponse> response = orders.stream()
        .map(OrderMapper.getInstance()::fromOrderDtoToOrderResponse)
        .toList();
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
    OrderDto order = orderService.getOrderById(id);
    return ResponseEntity.ok(OrderMapper.getInstance().fromOrderDtoToOrderResponse(order));
  }

  @PutMapping("/{id}/ship")
  public ResponseEntity<OrderResponse> markAsShipped(@PathVariable Long id) {
    OrderDto order = orderService.markAsShipped(id);
    return ResponseEntity.ok(OrderMapper.getInstance().fromOrderDtoToOrderResponse(order));
  }

  @PutMapping("/{id}/cancel")
  public ResponseEntity<OrderResponse> cancelOrder(@PathVariable Long id) {
    OrderDto order = orderService.cancelOrder(id);
    return ResponseEntity.ok(OrderMapper.getInstance().fromOrderDtoToOrderResponse(order));
  }
}
