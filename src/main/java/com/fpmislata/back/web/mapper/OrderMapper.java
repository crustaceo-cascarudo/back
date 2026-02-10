package com.fpmislata.back.web.mapper;

import com.fpmislata.back.web.webModel.response.OrderItemResponse;
import com.fpmislata.back.web.webModel.response.OrderResponse;
import com.fpmislata.back.domain.service.dto.OrderDto;
import com.fpmislata.back.domain.service.dto.OrderItemDto;

import java.math.BigDecimal;
import java.util.List;

public class OrderMapper {

  private static OrderMapper instance;

  private OrderMapper() {
  }

  public static OrderMapper getInstance() {
    if (instance == null) {
      instance = new OrderMapper();
    }
    return instance;
  }

  public OrderResponse fromOrderDtoToOrderResponse(OrderDto orderDto) {
    if (orderDto == null)
      return null;

    List<OrderItemResponse> items = orderDto.products().stream()
        .map(this::fromOrderItemDtoToOrderItemResponse)
        .toList();

    int totalItems = orderDto.products().stream()
        .mapToInt(OrderItemDto::quantity)
        .sum();

    return new OrderResponse(
        orderDto.id(),
        orderDto.userId(),
        orderDto.estado(),
        orderDto.address(),
        orderDto.orderDate(),
        items,
        totalItems,
        orderDto.totalPrice());
  }

  public OrderItemResponse fromOrderItemDtoToOrderItemResponse(OrderItemDto itemDto) {
    if (itemDto == null)
      return null;

    BigDecimal subtotal = itemDto.itemPrice().multiply(BigDecimal.valueOf(itemDto.quantity()));

    return new OrderItemResponse(
        itemDto.id(),
        itemDto.productDto().id(),
        itemDto.productDto().name(),
        itemDto.productDto().image(),
        itemDto.quantity(),
        itemDto.itemPrice(),
        subtotal);
  }
}
