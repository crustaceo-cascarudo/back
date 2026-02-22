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

import com.fpmislata.back.domain.service.CartService;
import com.fpmislata.back.domain.service.dto.CartDto;
import com.fpmislata.back.domain.service.dto.OrderDto;
import com.fpmislata.back.web.mapper.CartMapper;
import com.fpmislata.back.web.mapper.OrderMapper;
import com.fpmislata.back.web.webModel.request.AddCartItemRequest;
import com.fpmislata.back.web.webModel.request.CheckoutRequest;
import com.fpmislata.back.web.webModel.request.PayWithCardRequest;
import com.fpmislata.back.web.webModel.response.CartResponse;
import com.fpmislata.back.web.webModel.response.OrderResponse;
import com.fpmislata.back.web.webModel.response.PaymentResponse;

class CartControllerTest {

    private CartService cartService;
    private CartController cartController;
    private MockedStatic<CartMapper> cartMapperStatic;
    private MockedStatic<OrderMapper> orderMapperStatic;
    private CartMapper cartMapper;
    private OrderMapper orderMapper;

    private static final Long USER_ID = 1L;

    @BeforeEach
    void setUp() {
        cartService = mock(CartService.class);
        cartController = new CartController(cartService);

        cartMapper = mock(CartMapper.class);
        orderMapper = mock(OrderMapper.class);

        cartMapperStatic = mockStatic(CartMapper.class);
        orderMapperStatic = mockStatic(OrderMapper.class);

        cartMapperStatic.when(CartMapper::getInstance).thenReturn(cartMapper);
        orderMapperStatic.when(OrderMapper::getInstance).thenReturn(orderMapper);
    }

    @AfterEach
    void tearDown() {
        cartMapperStatic.close();
        orderMapperStatic.close();
    }

    @Test
    void getActiveCart_returnsOk() {
        CartDto cartDto = new CartDto(1L, USER_ID, List.of(), "CARRITO", BigDecimal.ZERO);
        CartResponse cartResponse = new CartResponse(1L, USER_ID, List.of(), 0, BigDecimal.ZERO);

        when(cartService.getActiveCartByUserId(USER_ID)).thenReturn(cartDto);
        when(cartMapper.fromCartDtoToCartResponse(cartDto)).thenReturn(cartResponse);

        ResponseEntity<CartResponse> response = cartController.getActiveCart(USER_ID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().totalItems());
    }

    @Test
    void createCart_returnsCreated() {
        CartDto cartDto = new CartDto(1L, USER_ID, List.of(), "CARRITO", BigDecimal.ZERO);
        CartResponse cartResponse = new CartResponse(1L, USER_ID, List.of(), 0, BigDecimal.ZERO);

        when(cartService.createCartForUser(USER_ID)).thenReturn(cartDto);
        when(cartMapper.fromCartDtoToCartResponse(cartDto)).thenReturn(cartResponse);

        ResponseEntity<CartResponse> response = cartController.createCart(USER_ID);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void addItem_returnsOk() {
        AddCartItemRequest request = new AddCartItemRequest(100L, 2);
        CartDto cartDto = new CartDto(1L, USER_ID, List.of(), "CARRITO", BigDecimal.valueOf(20));
        CartResponse cartResponse = new CartResponse(1L, USER_ID, List.of(), 2, BigDecimal.valueOf(20));

        when(cartService.addItemToCart(USER_ID, 100L, 2)).thenReturn(cartDto);
        when(cartMapper.fromCartDtoToCartResponse(cartDto)).thenReturn(cartResponse);

        ResponseEntity<CartResponse> response = cartController.addItem(USER_ID, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().totalItems());
    }

    @Test
    void clearCart_returnsNoContent() {
        doNothing().when(cartService).clearCart(USER_ID);

        ResponseEntity<Void> response = cartController.clearCart(USER_ID);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(cartService).clearCart(USER_ID);
    }

    @Test
    void checkout_returnsOkWithOrderResponse() {
        CheckoutRequest request = new CheckoutRequest("Calle 1");
        OrderDto orderDto = new OrderDto(1L, List.of(), "PENDIENTE", "Calle 1",
                new Date(), BigDecimal.TEN, USER_ID);
        OrderResponse orderResponse = new OrderResponse(1L, USER_ID, "PENDIENTE", "Calle 1",
                new Date(), List.of(), 0, BigDecimal.TEN);

        when(cartService.checkout(USER_ID, "Calle 1")).thenReturn(orderDto);
        when(orderMapper.fromOrderDtoToOrderResponse(orderDto)).thenReturn(orderResponse);

        ResponseEntity<OrderResponse> response = cartController.checkout(USER_ID, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("PENDIENTE", response.getBody().estado());
    }

    @Test
    void payWithCard_returnsOkWithPaymentResponse() {
        PayWithCardRequest request = new PayWithCardRequest(
                "4111111111111111", "12", "2030", "123", "Test User", "ES12345", "Calle 1");
        OrderDto orderDto = new OrderDto(1L, List.of(), "PAGADO", "Calle 1",
                new Date(), BigDecimal.TEN, USER_ID);
        OrderResponse orderResponse = new OrderResponse(1L, USER_ID, "PAGADO", "Calle 1",
                new Date(), List.of(), 0, BigDecimal.TEN);

        when(cartService.payWithCard(eq(USER_ID), eq("Calle 1"), eq("4111111111111111"),
                eq("12"), eq("2030"), eq("123"), eq("Test User"), eq("ES12345")))
                .thenReturn(orderDto);
        when(orderMapper.fromOrderDtoToOrderResponse(orderDto)).thenReturn(orderResponse);

        ResponseEntity<PaymentResponse> response = cartController.payWithCard(USER_ID, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("PAGADO", response.getBody().estado());
        assertEquals("Pago realizado con éxito", response.getBody().message());
    }
}
