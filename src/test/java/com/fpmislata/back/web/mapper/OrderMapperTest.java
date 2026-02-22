package com.fpmislata.back.web.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.fpmislata.back.domain.service.dto.OrderDto;
import com.fpmislata.back.domain.service.dto.OrderItemDto;
import com.fpmislata.back.domain.service.dto.ProductDto;
import com.fpmislata.back.web.webModel.response.OrderItemResponse;
import com.fpmislata.back.web.webModel.response.OrderResponse;

class OrderMapperTest {

    private final OrderMapper mapper = OrderMapper.getInstance();

    @Test
    void fromOrderDtoToOrderResponse_validInput_mapsAllFields() {
        ProductDto productDto = new ProductDto(1L, "Burger", List.of(), 10.0, null, 10.0, "img.png", List.of());
        OrderItemDto itemDto = new OrderItemDto(1L, productDto, 2, BigDecimal.TEN);
        Date now = new Date();
        OrderDto orderDto = new OrderDto(5L, List.of(itemDto), "PENDIENTE", "Calle 1",
                now, BigDecimal.valueOf(20), 1L);

        OrderResponse response = mapper.fromOrderDtoToOrderResponse(orderDto);

        assertEquals(5L, response.id());
        assertEquals(1L, response.userId());
        assertEquals("PENDIENTE", response.estado());
        assertEquals("Calle 1", response.address());
        assertEquals(now, response.orderDate());
        assertEquals(BigDecimal.valueOf(20), response.totalPrice());
        assertEquals(2, response.totalItems());
        assertEquals(1, response.items().size());
    }

    @Test
    void fromOrderDtoToOrderResponse_null_returnsNull() {
        assertNull(mapper.fromOrderDtoToOrderResponse(null));
    }

    @Test
    void fromOrderItemDtoToOrderItemResponse_validInput_calculatesSubtotal() {
        ProductDto productDto = new ProductDto(1L, "Burger", List.of(), 10.0, null, 10.0, "img.png", List.of());
        OrderItemDto itemDto = new OrderItemDto(1L, productDto, 3, BigDecimal.valueOf(10));

        OrderItemResponse response = mapper.fromOrderItemDtoToOrderItemResponse(itemDto);

        assertEquals(1L, response.id());
        assertEquals(1L, response.productId());
        assertEquals("Burger", response.productName());
        assertEquals("img.png", response.productImage());
        assertEquals(3, response.quantity());
        assertEquals(BigDecimal.valueOf(10), response.unitPrice());
        assertEquals(BigDecimal.valueOf(30), response.subtotal());
    }

    @Test
    void fromOrderItemDtoToOrderItemResponse_null_returnsNull() {
        assertNull(mapper.fromOrderItemDtoToOrderItemResponse(null));
    }

    @Test
    void fromOrderDtoToOrderResponse_emptyProducts_zeroTotalItems() {
        OrderDto orderDto = new OrderDto(1L, List.of(), "CARRITO", null,
                new Date(), BigDecimal.ZERO, 1L);

        OrderResponse response = mapper.fromOrderDtoToOrderResponse(orderDto);

        assertEquals(0, response.totalItems());
        assertTrue(response.items().isEmpty());
    }
}
