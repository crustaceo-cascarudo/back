package com.fpmislata.back.domain.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import com.fpmislata.back.domain.Exception.BusinessException;
import com.fpmislata.back.domain.Exception.ResourceNotFoundException;
import com.fpmislata.back.domain.enumerado.Estado;
import com.fpmislata.back.domain.mapper.OrderMapper;
import com.fpmislata.back.domain.model.Order;
import com.fpmislata.back.domain.model.OrderItem;
import com.fpmislata.back.domain.repository.OrderRepository;
import com.fpmislata.back.domain.repository.entity.OrderEntity;
import com.fpmislata.back.domain.repository.entity.OrderItemEntity;
import com.fpmislata.back.domain.service.dto.OrderDto;
import com.fpmislata.back.domain.service.dto.OrderItemDto;

class OrderServiceImplTest {

    private OrderRepository orderRepository;
    private OrderServiceImpl orderService;
    private MockedStatic<OrderMapper> orderMapperStatic;
    private OrderMapper orderMapper;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        orderService = new OrderServiceImpl(orderRepository);

        orderMapper = mock(OrderMapper.class);
        orderMapperStatic = mockStatic(OrderMapper.class);
        orderMapperStatic.when(OrderMapper::getInstance).thenReturn(orderMapper);
    }

    @AfterEach
    void tearDown() {
        orderMapperStatic.close();
    }

    // ── getOrdersByUserId ──────────────────────────────────────────

    @Test
    void getOrdersByUserId_returnsListExcludingCarrito() {
        Long userId = 1L;
        OrderEntity entity = new OrderEntity(10L, List.of(), Estado.PENDIENTE.name(),
                "Calle 1", new Date(), BigDecimal.TEN, userId);
        Order order = new Order(10L, List.of(), Estado.PENDIENTE, "Calle 1",
                new Date(), BigDecimal.TEN, userId);
        OrderDto dto = new OrderDto(10L, List.of(), "PENDIENTE", "Calle 1",
                new Date(), BigDecimal.TEN, userId);

        when(orderRepository.findByUserIdExcludingEstado(userId, Estado.CARRITO.name()))
                .thenReturn(List.of(entity));
        when(orderMapper.fromOrderEntityToOrder(entity)).thenReturn(order);
        when(orderMapper.fromOrderToOrderDto(order)).thenReturn(dto);

        List<OrderDto> result = orderService.getOrdersByUserId(userId);

        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
        verify(orderRepository).findByUserIdExcludingEstado(userId, Estado.CARRITO.name());
    }

    @Test
    void getOrdersByUserId_returnsEmptyWhenNoOrders() {
        Long userId = 99L;
        when(orderRepository.findByUserIdExcludingEstado(userId, Estado.CARRITO.name()))
                .thenReturn(List.of());

        List<OrderDto> result = orderService.getOrdersByUserId(userId);

        assertTrue(result.isEmpty());
    }

    // ── getOrderById ───────────────────────────────────────────────

    @Test
    void getOrderById_existsAndIsNotCarrito_returnsDto() {
        Long id = 5L;
        OrderEntity entity = new OrderEntity(id, List.of(), Estado.PENDIENTE.name(),
                "Calle 1", new Date(), BigDecimal.TEN, 1L);
        Order order = new Order(id, List.of(), Estado.PENDIENTE, "Calle 1",
                new Date(), BigDecimal.TEN, 1L);
        OrderDto dto = new OrderDto(id, List.of(), "PENDIENTE", "Calle 1",
                new Date(), BigDecimal.TEN, 1L);

        when(orderRepository.findById(id)).thenReturn(Optional.of(entity));
        when(orderMapper.fromOrderEntityToOrder(entity)).thenReturn(order);
        when(orderMapper.fromOrderToOrderDto(order)).thenReturn(dto);

        OrderDto result = orderService.getOrderById(id);

        assertEquals(dto, result);
    }

    @Test
    void getOrderById_notFound_throwsResourceNotFoundException() {
        Long id = 999L;
        when(orderRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> orderService.getOrderById(id));
        assertTrue(ex.getMessage().contains("" + id));
    }

    @Test
    void getOrderById_isCarrito_throwsBusinessException() {
        Long id = 3L;
        OrderEntity cartEntity = new OrderEntity(id, List.of(), Estado.CARRITO.name(),
                null, new Date(), BigDecimal.ZERO, 1L);
        when(orderRepository.findById(id)).thenReturn(Optional.of(cartEntity));

        BusinessException ex = assertThrows(BusinessException.class,
                () -> orderService.getOrderById(id));
        assertTrue(ex.getMessage().contains("carrito"));
    }

    // ── markAsShipped ──────────────────────────────────────────────

    @Test
    void markAsShipped_fromPagado_success() {
        Long orderId = 7L;
        OrderEntity pagadoEntity = new OrderEntity(orderId, List.of(), Estado.PAGADO.name(),
                "Calle 1", new Date(), BigDecimal.TEN, 1L);
        OrderEntity enviadoEntity = new OrderEntity(orderId, List.of(), Estado.ENVIADO.name(),
                "Calle 1", pagadoEntity.orderDate(), BigDecimal.TEN, 1L);
        Order order = new Order(orderId, List.of(), Estado.ENVIADO, "Calle 1",
                pagadoEntity.orderDate(), BigDecimal.TEN, 1L);
        OrderDto dto = new OrderDto(orderId, List.of(), "ENVIADO", "Calle 1",
                pagadoEntity.orderDate(), BigDecimal.TEN, 1L);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(pagadoEntity));
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(enviadoEntity);
        when(orderMapper.fromOrderEntityToOrder(enviadoEntity)).thenReturn(order);
        when(orderMapper.fromOrderToOrderDto(order)).thenReturn(dto);

        OrderDto result = orderService.markAsShipped(orderId);

        assertEquals("ENVIADO", result.estado());
        verify(orderRepository).save(argThat(e -> Estado.ENVIADO.name().equals(e.estado())));
    }

    @Test
    void markAsShipped_fromPendiente_throwsBusinessException() {
        Long orderId = 8L;
        OrderEntity pendienteEntity = new OrderEntity(orderId, List.of(), Estado.PENDIENTE.name(),
                "Calle 1", new Date(), BigDecimal.TEN, 1L);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(pendienteEntity));

        BusinessException ex = assertThrows(BusinessException.class,
                () -> orderService.markAsShipped(orderId));
        assertTrue(ex.getMessage().contains("PAGADO"));
    }

    @Test
    void markAsShipped_fromCarrito_throwsBusinessException() {
        Long orderId = 9L;
        OrderEntity carritoEntity = new OrderEntity(orderId, List.of(), Estado.CARRITO.name(),
                null, new Date(), BigDecimal.ZERO, 1L);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(carritoEntity));

        BusinessException ex = assertThrows(BusinessException.class,
                () -> orderService.markAsShipped(orderId));
        assertTrue(ex.getMessage().contains("PAGADO"));
    }

    @Test
    void markAsShipped_notFound_throwsResourceNotFoundException() {
        Long orderId = 100L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> orderService.markAsShipped(orderId));
    }

    // ── cancelOrder ────────────────────────────────────────────────

    @Test
    void cancelOrder_fromPendiente_success() {
        Long orderId = 11L;
        OrderEntity pendienteEntity = new OrderEntity(orderId, List.of(), Estado.PENDIENTE.name(),
                "Calle 1", new Date(), BigDecimal.TEN, 1L);
        OrderEntity canceladoEntity = new OrderEntity(orderId, List.of(), Estado.CANCELADO.name(),
                "Calle 1", pendienteEntity.orderDate(), BigDecimal.TEN, 1L);
        Order order = new Order(orderId, List.of(), Estado.CANCELADO, "Calle 1",
                pendienteEntity.orderDate(), BigDecimal.TEN, 1L);
        OrderDto dto = new OrderDto(orderId, List.of(), "CANCELADO", "Calle 1",
                pendienteEntity.orderDate(), BigDecimal.TEN, 1L);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(pendienteEntity));
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(canceladoEntity);
        when(orderMapper.fromOrderEntityToOrder(canceladoEntity)).thenReturn(order);
        when(orderMapper.fromOrderToOrderDto(order)).thenReturn(dto);

        OrderDto result = orderService.cancelOrder(orderId);

        assertEquals("CANCELADO", result.estado());
        verify(orderRepository).save(argThat(e -> Estado.CANCELADO.name().equals(e.estado())));
    }

    @Test
    void cancelOrder_fromPagado_success() {
        Long orderId = 12L;
        OrderEntity pagadoEntity = new OrderEntity(orderId, List.of(), Estado.PAGADO.name(),
                "Calle 1", new Date(), BigDecimal.TEN, 1L);
        OrderEntity canceladoEntity = new OrderEntity(orderId, List.of(), Estado.CANCELADO.name(),
                "Calle 1", pagadoEntity.orderDate(), BigDecimal.TEN, 1L);
        Order order = new Order(orderId, List.of(), Estado.CANCELADO, "Calle 1",
                pagadoEntity.orderDate(), BigDecimal.TEN, 1L);
        OrderDto dto = new OrderDto(orderId, List.of(), "CANCELADO", "Calle 1",
                pagadoEntity.orderDate(), BigDecimal.TEN, 1L);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(pagadoEntity));
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(canceladoEntity);
        when(orderMapper.fromOrderEntityToOrder(canceladoEntity)).thenReturn(order);
        when(orderMapper.fromOrderToOrderDto(order)).thenReturn(dto);

        OrderDto result = orderService.cancelOrder(orderId);

        assertEquals("CANCELADO", result.estado());
    }

    @Test
    void cancelOrder_fromEnviado_throwsBusinessException() {
        Long orderId = 13L;
        OrderEntity enviadoEntity = new OrderEntity(orderId, List.of(), Estado.ENVIADO.name(),
                "Calle 1", new Date(), BigDecimal.TEN, 1L);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(enviadoEntity));

        BusinessException ex = assertThrows(BusinessException.class,
                () -> orderService.cancelOrder(orderId));
        assertTrue(ex.getMessage().contains("ENVIADO"));
    }

    @Test
    void cancelOrder_fromCancelado_throwsBusinessException() {
        Long orderId = 14L;
        OrderEntity canceladoEntity = new OrderEntity(orderId, List.of(), Estado.CANCELADO.name(),
                "Calle 1", new Date(), BigDecimal.TEN, 1L);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(canceladoEntity));

        BusinessException ex = assertThrows(BusinessException.class,
                () -> orderService.cancelOrder(orderId));
        assertTrue(ex.getMessage().contains("CANCELADO"));
    }

    @Test
    void cancelOrder_fromCarrito_throwsBusinessException() {
        Long orderId = 15L;
        OrderEntity carritoEntity = new OrderEntity(orderId, List.of(), Estado.CARRITO.name(),
                null, new Date(), BigDecimal.ZERO, 1L);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(carritoEntity));

        BusinessException ex = assertThrows(BusinessException.class,
                () -> orderService.cancelOrder(orderId));
        assertTrue(ex.getMessage().contains("carrito"));
    }

    @Test
    void cancelOrder_notFound_throwsResourceNotFoundException() {
        Long orderId = 200L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> orderService.cancelOrder(orderId));
    }
}
