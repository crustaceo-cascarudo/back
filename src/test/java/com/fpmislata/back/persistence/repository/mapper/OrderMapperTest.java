package com.fpmislata.back.persistence.repository.mapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import com.fpmislata.back.domain.repository.entity.OrderEntity;
import com.fpmislata.back.domain.repository.entity.OrderItemEntity;
import com.fpmislata.back.domain.repository.entity.ProductEntity;
import com.fpmislata.back.persistence.dao.impl.entity.OrderItemJpaEntity;
import com.fpmislata.back.persistence.dao.impl.entity.OrderJpaEntity;
import com.fpmislata.back.persistence.dao.impl.entity.ProductJpaEntity;

class OrderMapperTest {

    private MockedStatic<ProductMapper> productMapperStatic;
    private ProductMapper productMapper;
    private OrderMapper orderMapper;

    @BeforeEach
    void setUp() {
        productMapper = mock(ProductMapper.class);
        productMapperStatic = mockStatic(ProductMapper.class);
        productMapperStatic.when(ProductMapper::getInstance).thenReturn(productMapper);
        orderMapper = OrderMapper.getInstance();
    }

    @AfterEach
    void tearDown() {
        productMapperStatic.close();
    }

    @Test
    void fromOrderJpaEntityToOrderEntity_validInput_mapsAllFields() {
        ProductJpaEntity productJpa = new ProductJpaEntity(1L, "Burger",
                new java.util.ArrayList<>(), 10.0, null, "img.png", new java.util.ArrayList<>());
        OrderItemJpaEntity itemJpa = new OrderItemJpaEntity(1L, null, productJpa, 2, BigDecimal.TEN);

        OrderJpaEntity jpa = new OrderJpaEntity(5L, "PENDIENTE", "Calle 1",
                new Date(), BigDecimal.valueOf(20), 1L, List.of(itemJpa));

        ProductEntity pe = new ProductEntity(1L, "Burger", List.of(), 10.0, null, "img.png", List.of());
        when(productMapper.fromProductJpaEntityToProductEntity(productJpa)).thenReturn(pe);

        OrderEntity result = orderMapper.fromOrderJpaEntityToOrderEntity(jpa);

        assertNotNull(result);
        assertEquals(5L, result.id());
        assertEquals("PENDIENTE", result.estado());
        assertEquals("Calle 1", result.address());
        assertEquals(1L, result.userId());
        assertEquals(1, result.products().size());
        assertEquals(2, result.products().get(0).quantity());
    }

    @Test
    void fromOrderJpaEntityToOrderEntity_null_returnsNull() {
        assertNull(orderMapper.fromOrderJpaEntityToOrderEntity(null));
    }

    @Test
    void fromOrderEntityToOrderJpaEntity_validInput_mapsAllFields() {
        ProductEntity pe = new ProductEntity(1L, "Burger", List.of(), 10.0, null, "img.png", List.of());
        OrderItemEntity itemEntity = new OrderItemEntity(1L, pe, 3, BigDecimal.TEN);
        OrderEntity entity = new OrderEntity(7L, List.of(itemEntity), "PAGADO",
                "Calle 2", new Date(), BigDecimal.valueOf(30), 2L);

        ProductJpaEntity productJpa = new ProductJpaEntity(1L, "Burger",
                new java.util.ArrayList<>(), 10.0, null, "img.png", new java.util.ArrayList<>());
        when(productMapper.fromProductEntityToProductJpaEntity(pe)).thenReturn(productJpa);

        OrderJpaEntity result = orderMapper.fromOrderEntityToOrderJpaEntity(entity);

        assertNotNull(result);
        assertEquals(7L, result.getId());
        assertEquals("PAGADO", result.getEstado());
        assertEquals("Calle 2", result.getAddress());
        assertEquals(2L, result.getUserId());
        assertEquals(1, result.getItems().size());
    }

    @Test
    void fromOrderEntityToOrderJpaEntity_null_returnsNull() {
        assertNull(orderMapper.fromOrderEntityToOrderJpaEntity(null));
    }

    @Test
    void fromOrderItemJpaEntityToOrderItemEntity_null_returnsNull() {
        assertNull(orderMapper.fromOrderItemJpaEntityToOrderItemEntity(null));
    }

    @Test
    void fromOrderItemEntityToOrderItemJpaEntity_null_returnsNull() {
        assertNull(orderMapper.fromOrderItemEntityToOrderItemJpaEntity(null));
    }

    @Test
    void fromOrderJpaEntityToOrderEntity_emptyItems_returnsEmptyList() {
        OrderJpaEntity jpa = new OrderJpaEntity(1L, "CARRITO", null,
                new Date(), BigDecimal.ZERO, 1L, List.of());

        OrderEntity result = orderMapper.fromOrderJpaEntityToOrderEntity(jpa);

        assertNotNull(result);
        assertTrue(result.products().isEmpty());
    }
}
