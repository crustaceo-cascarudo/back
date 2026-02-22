package com.fpmislata.back.persistence.repository.impl;

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

import com.fpmislata.back.domain.repository.entity.OrderEntity;
import com.fpmislata.back.persistence.dao.OrderDao;
import com.fpmislata.back.persistence.dao.impl.entity.OrderJpaEntity;
import com.fpmislata.back.persistence.repository.mapper.OrderMapper;

class OrderRepositoryImplTest {

    private OrderDao orderDao;
    private OrderRepositoryImpl orderRepository;
    private MockedStatic<OrderMapper> orderMapperStatic;
    private OrderMapper orderMapper;

    @BeforeEach
    void setUp() {
        orderDao = mock(OrderDao.class);
        orderRepository = new OrderRepositoryImpl(orderDao);

        orderMapper = mock(OrderMapper.class);
        orderMapperStatic = mockStatic(OrderMapper.class);
        orderMapperStatic.when(OrderMapper::getInstance).thenReturn(orderMapper);
    }

    @AfterEach
    void tearDown() {
        orderMapperStatic.close();
    }

    @Test
    void findByUserIdAndEstado_exists_returnsMappedEntity() {
        OrderJpaEntity jpa = new OrderJpaEntity(1L, "CARRITO", null, new Date(),
                BigDecimal.ZERO, 1L, List.of());
        OrderEntity entity = new OrderEntity(1L, List.of(), "CARRITO", null,
                new Date(), BigDecimal.ZERO, 1L);

        when(orderDao.findByUserIdAndEstado(1L, "CARRITO")).thenReturn(Optional.of(jpa));
        when(orderMapper.fromOrderJpaEntityToOrderEntity(jpa)).thenReturn(entity);

        Optional<OrderEntity> result = orderRepository.findByUserIdAndEstado(1L, "CARRITO");

        assertTrue(result.isPresent());
        assertEquals("CARRITO", result.get().estado());
    }

    @Test
    void findByUserIdAndEstado_notFound_returnsEmpty() {
        when(orderDao.findByUserIdAndEstado(99L, "CARRITO")).thenReturn(Optional.empty());

        Optional<OrderEntity> result = orderRepository.findByUserIdAndEstado(99L, "CARRITO");

        assertTrue(result.isEmpty());
    }

    @Test
    void findByUserId_returnsAllOrdersForUser() {
        OrderJpaEntity jpa = new OrderJpaEntity(1L, "PENDIENTE", "Calle 1", new Date(),
                BigDecimal.TEN, 1L, List.of());
        OrderEntity entity = new OrderEntity(1L, List.of(), "PENDIENTE", "Calle 1",
                new Date(), BigDecimal.TEN, 1L);

        when(orderDao.findByUserId(1L)).thenReturn(List.of(jpa));
        when(orderMapper.fromOrderJpaEntityToOrderEntity(jpa)).thenReturn(entity);

        List<OrderEntity> result = orderRepository.findByUserId(1L);

        assertEquals(1, result.size());
    }

    @Test
    void findByUserIdExcludingEstado_filtersOutSpecifiedEstado() {
        OrderJpaEntity jpa = new OrderJpaEntity(1L, "PENDIENTE", "Calle 1", new Date(),
                BigDecimal.TEN, 1L, List.of());
        OrderEntity entity = new OrderEntity(1L, List.of(), "PENDIENTE", "Calle 1",
                new Date(), BigDecimal.TEN, 1L);

        when(orderDao.findByUserIdExcludingEstado(1L, "CARRITO")).thenReturn(List.of(jpa));
        when(orderMapper.fromOrderJpaEntityToOrderEntity(jpa)).thenReturn(entity);

        List<OrderEntity> result = orderRepository.findByUserIdExcludingEstado(1L, "CARRITO");

        assertEquals(1, result.size());
        assertEquals("PENDIENTE", result.get(0).estado());
    }

    @Test
    void findById_exists_returnsEntity() {
        OrderJpaEntity jpa = new OrderJpaEntity(5L, "PAGADO", "Calle 2", new Date(),
                BigDecimal.valueOf(50), 2L, List.of());
        OrderEntity entity = new OrderEntity(5L, List.of(), "PAGADO", "Calle 2",
                new Date(), BigDecimal.valueOf(50), 2L);

        when(orderDao.findById(5L)).thenReturn(Optional.of(jpa));
        when(orderMapper.fromOrderJpaEntityToOrderEntity(jpa)).thenReturn(entity);

        Optional<OrderEntity> result = orderRepository.findById(5L);

        assertTrue(result.isPresent());
        assertEquals(5L, result.get().id());
    }

    @Test
    void save_newOrder_callsInsert() {
        OrderEntity entity = new OrderEntity(null, List.of(), "CARRITO", null,
                new Date(), BigDecimal.ZERO, 1L);
        OrderJpaEntity jpa = new OrderJpaEntity(null, "CARRITO", null, new Date(),
                BigDecimal.ZERO, 1L, List.of());
        OrderJpaEntity savedJpa = new OrderJpaEntity(10L, "CARRITO", null, new Date(),
                BigDecimal.ZERO, 1L, List.of());
        OrderEntity savedEntity = new OrderEntity(10L, List.of(), "CARRITO", null,
                new Date(), BigDecimal.ZERO, 1L);

        when(orderMapper.fromOrderEntityToOrderJpaEntity(entity)).thenReturn(jpa);
        when(orderDao.insert(jpa)).thenReturn(savedJpa);
        when(orderMapper.fromOrderJpaEntityToOrderEntity(savedJpa)).thenReturn(savedEntity);

        OrderEntity result = orderRepository.save(entity);

        assertEquals(10L, result.id());
        verify(orderDao).insert(jpa);
    }

    @Test
    void save_existingOrder_callsUpdate() {
        OrderEntity entity = new OrderEntity(5L, List.of(), "PENDIENTE", "Calle 1",
                new Date(), BigDecimal.TEN, 1L);
        OrderJpaEntity jpa = new OrderJpaEntity(5L, "PENDIENTE", "Calle 1", new Date(),
                BigDecimal.TEN, 1L, List.of());
        OrderJpaEntity updatedJpa = new OrderJpaEntity(5L, "PENDIENTE", "Calle 1", new Date(),
                BigDecimal.TEN, 1L, List.of());
        OrderEntity updatedEntity = new OrderEntity(5L, List.of(), "PENDIENTE", "Calle 1",
                new Date(), BigDecimal.TEN, 1L);

        when(orderMapper.fromOrderEntityToOrderJpaEntity(entity)).thenReturn(jpa);
        when(orderDao.update(jpa)).thenReturn(updatedJpa);
        when(orderMapper.fromOrderJpaEntityToOrderEntity(updatedJpa)).thenReturn(updatedEntity);

        OrderEntity result = orderRepository.save(entity);

        verify(orderDao).update(jpa);
    }

    @Test
    void deleteById_delegatesToDao() {
        orderRepository.deleteById(1L);

        verify(orderDao).deleteById(1L);
    }

    @Test
    void deleteItemsByOrderId_delegatesToDao() {
        orderRepository.deleteItemsByOrderId(5L);

        verify(orderDao).deleteItemsByOrderId(5L);
    }
}
