package com.fpmislata.back.domain.mapper;

import com.fpmislata.back.domain.enumerado.Estado;
import com.fpmislata.back.domain.model.*;
import com.fpmislata.back.domain.repository.entity.*;
import com.fpmislata.back.domain.service.dto.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderMapperTest {

    private final OrderMapper mapper = OrderMapper.getInstance();

    private Product createTestProduct() {
        return new Product(1L, "Pizza", List.of(), 10.0, 0, "pizza.jpg", List.of());
    }

    private ProductEntity createTestProductEntity() {
        return new ProductEntity(1L, "Pizza", List.of(), 10.0, 0, "pizza.jpg", List.of());
    }

    @Test
    void fromOrderToOrderDto_shouldMapAllFields() {
        Product product = createTestProduct();
        OrderItem item = new OrderItem(1L, product, 2, BigDecimal.TEN);
        Date now = new Date();
        Order order = new Order(1L, List.of(item), Estado.PENDIENTE, "Calle 1", now, BigDecimal.valueOf(20), 1L);

        OrderDto dto = mapper.fromOrderToOrderDto(order);

        assertEquals(1L, dto.id());
        assertEquals("PENDIENTE", dto.estado());
        assertEquals("Calle 1", dto.address());
        assertEquals(now, dto.orderDate());
        assertEquals(BigDecimal.valueOf(20), dto.totalPrice());
        assertEquals(1L, dto.userId());
        assertEquals(1, dto.products().size());
    }

    @Test
    void fromOrderToOrderDto_null_shouldReturnNull() {
        assertNull(mapper.fromOrderToOrderDto(null));
    }

    @Test
    void fromOrderDtoToOrder_shouldMapAllFields() {
        ProductDto productDto = new ProductDto(1L, "Pizza", List.of(), 10.0, 0, 10.0, "pizza.jpg", List.of());
        OrderItemDto itemDto = new OrderItemDto(1L, productDto, 2, BigDecimal.TEN);
        Date now = new Date();
        OrderDto dto = new OrderDto(1L, List.of(itemDto), "PAGADO", "Calle 1", now, BigDecimal.valueOf(20), 1L);

        Order order = mapper.fromOrderDtoToOrder(dto);

        assertEquals(1L, order.getId());
        assertEquals(Estado.PAGADO, order.getEstado());
        assertEquals("Calle 1", order.getAddress());
        assertEquals(1, order.getProducts().size());
    }

    @Test
    void fromOrderDtoToOrder_null_shouldReturnNull() {
        assertNull(mapper.fromOrderDtoToOrder(null));
    }

    @Test
    void fromOrderToOrderEntity_shouldMapAllFields() {
        Product product = createTestProduct();
        OrderItem item = new OrderItem(1L, product, 2, BigDecimal.TEN);
        Date now = new Date();
        Order order = new Order(1L, List.of(item), Estado.ENVIADO, "Calle 1", now, BigDecimal.valueOf(20), 1L);

        OrderEntity entity = mapper.fromOrderToOrderEntity(order);

        assertEquals(1L, entity.id());
        assertEquals("ENVIADO", entity.estado());
        assertEquals("Calle 1", entity.address());
        assertEquals(1, entity.products().size());
    }

    @Test
    void fromOrderEntityToOrder_shouldMapAllFields() {
        ProductEntity pe = createTestProductEntity();
        OrderItemEntity itemEntity = new OrderItemEntity(1L, pe, 2, BigDecimal.TEN);
        Date now = new Date();
        OrderEntity entity = new OrderEntity(1L, List.of(itemEntity), "CANCELADO", "Calle 1", now, BigDecimal.valueOf(20), 1L);

        Order order = mapper.fromOrderEntityToOrder(entity);

        assertEquals(1L, order.getId());
        assertEquals(Estado.CANCELADO, order.getEstado());
        assertEquals("Calle 1", order.getAddress());
        assertEquals(1, order.getProducts().size());
    }

    @Test
    void fromOrderEntityToOrder_null_shouldReturnNull() {
        assertNull(mapper.fromOrderEntityToOrder(null));
    }

    @Test
    void fromOrderToOrderDto_withEmptyItems_shouldWork() {
        Order order = new Order(1L, List.of(), Estado.CARRITO, null, new Date(), BigDecimal.ZERO, 1L);
        OrderDto dto = mapper.fromOrderToOrderDto(order);
        assertTrue(dto.products().isEmpty());
    }
}
