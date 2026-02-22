package com.fpmislata.back.web.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fpmislata.back.domain.service.OrderService;
import com.fpmislata.back.domain.service.dto.OrderDto;
import com.fpmislata.back.web.mapper.OrderMapper;
import com.fpmislata.back.web.webModel.response.OrderResponse;

class OrderControllerTest {

    private OrderService orderService;
    private OrderController orderController;
    private MockedStatic<OrderMapper> orderMapperStatic;
    private OrderMapper orderMapper;

    @BeforeEach
    void setUp() {
        orderService = mock(OrderService.class);
        orderController = new OrderController(orderService);

        orderMapper = mock(OrderMapper.class);
        orderMapperStatic = mockStatic(OrderMapper.class);
        orderMapperStatic.when(OrderMapper::getInstance).thenReturn(orderMapper);
    }

    @AfterEach
    void tearDown() {
        orderMapperStatic.close();
    }

    @Test
    void getOrdersByUser_returnsOkWithList() {
        Long userId = 1L;
        OrderDto dto = new OrderDto(1L, List.of(), "PENDIENTE", "Calle 1",
                new Date(), BigDecimal.TEN, userId);
        OrderResponse orderResponse = new OrderResponse(1L, userId, "PENDIENTE", "Calle 1",
                new Date(), List.of(), 0, BigDecimal.TEN);

        when(orderService.getOrdersByUserId(userId)).thenReturn(List.of(dto));
        when(orderMapper.fromOrderDtoToOrderResponse(dto)).thenReturn(orderResponse);

        ResponseEntity<List<OrderResponse>> response = orderController.getOrdersByUser(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getOrderById_returnsOkWithOrder() {
        OrderDto dto = new OrderDto(5L, List.of(), "PAGADO", "Calle 1",
                new Date(), BigDecimal.TEN, 1L);
        OrderResponse orderResponse = new OrderResponse(5L, 1L, "PAGADO", "Calle 1",
                new Date(), List.of(), 0, BigDecimal.TEN);

        when(orderService.getOrderById(5L)).thenReturn(dto);
        when(orderMapper.fromOrderDtoToOrderResponse(dto)).thenReturn(orderResponse);

        ResponseEntity<OrderResponse> response = orderController.getOrderById(5L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("PAGADO", response.getBody().estado());
    }

    @Test
    void markAsShipped_returnsOkWithUpdatedOrder() {
        OrderDto dto = new OrderDto(5L, List.of(), "ENVIADO", "Calle 1",
                new Date(), BigDecimal.TEN, 1L);
        OrderResponse orderResponse = new OrderResponse(5L, 1L, "ENVIADO", "Calle 1",
                new Date(), List.of(), 0, BigDecimal.TEN);

        when(orderService.markAsShipped(5L)).thenReturn(dto);
        when(orderMapper.fromOrderDtoToOrderResponse(dto)).thenReturn(orderResponse);

        ResponseEntity<OrderResponse> response = orderController.markAsShipped(5L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("ENVIADO", response.getBody().estado());
    }

    @Test
    void cancelOrder_returnsOkWithCancelledOrder() {
        OrderDto dto = new OrderDto(5L, List.of(), "CANCELADO", "Calle 1",
                new Date(), BigDecimal.TEN, 1L);
        OrderResponse orderResponse = new OrderResponse(5L, 1L, "CANCELADO", "Calle 1",
                new Date(), List.of(), 0, BigDecimal.TEN);

        when(orderService.cancelOrder(5L)).thenReturn(dto);
        when(orderMapper.fromOrderDtoToOrderResponse(dto)).thenReturn(orderResponse);

        ResponseEntity<OrderResponse> response = orderController.cancelOrder(5L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("CANCELADO", response.getBody().estado());
    }
}
