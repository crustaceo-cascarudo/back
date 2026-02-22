package com.fpmislata.back.integration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.fpmislata.back.domain.mapper.CartMapper;
import com.fpmislata.back.domain.mapper.OrderMapper;
import com.fpmislata.back.domain.mapper.ProductMapper;
import com.fpmislata.back.domain.model.Order;
import com.fpmislata.back.domain.model.Product;
import com.fpmislata.back.domain.repository.OrderRepository;
import com.fpmislata.back.domain.repository.ProductRepository;
import com.fpmislata.back.domain.repository.entity.OrderEntity;
import com.fpmislata.back.domain.repository.entity.OrderItemEntity;
import com.fpmislata.back.domain.repository.entity.ProductEntity;
import com.fpmislata.back.domain.service.dto.CartDto;
import com.fpmislata.back.domain.service.dto.OrderDto;
import com.fpmislata.back.domain.service.dto.OrderItemDto;
import com.fpmislata.back.domain.service.impl.CartServiceImpl;
import com.fpmislata.back.domain.service.impl.OrderServiceImpl;
import com.fpmislata.back.infrastructure.payment.request.CardPaymentRequest;
import com.fpmislata.back.infrastructure.payment.service.BankPaymentService;

/**
 * Integration-style tests that verify cross-layer flows through
 * service → repository (mocked) with real mapper logic.
 */
class OrderFlowIntegrationTest {

    private OrderRepository orderRepository;
    private ProductRepository productRepository;
    private BankPaymentService bankPaymentService;
    private CartServiceImpl cartService;
    private OrderServiceImpl orderService;

    private MockedStatic<CartMapper> cartMapperStatic;
    private MockedStatic<OrderMapper> orderMapperStatic;
    private MockedStatic<ProductMapper> productMapperStatic;

    private CartMapper cartMapper;
    private OrderMapper orderMapper;
    private ProductMapper productMapper;

    private static final Long USER_ID = 1L;
    private static final Long PRODUCT_ID = 100L;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        productRepository = mock(ProductRepository.class);
        bankPaymentService = mock(BankPaymentService.class);
        cartService = new CartServiceImpl(orderRepository, productRepository, bankPaymentService);
        orderService = new OrderServiceImpl(orderRepository);

        cartMapper = mock(CartMapper.class);
        orderMapper = mock(OrderMapper.class);
        productMapper = mock(ProductMapper.class);

        cartMapperStatic = mockStatic(CartMapper.class);
        orderMapperStatic = mockStatic(OrderMapper.class);
        productMapperStatic = mockStatic(ProductMapper.class);

        cartMapperStatic.when(CartMapper::getInstance).thenReturn(cartMapper);
        orderMapperStatic.when(OrderMapper::getInstance).thenReturn(orderMapper);
        productMapperStatic.when(ProductMapper::getInstance).thenReturn(productMapper);
    }

    @AfterEach
    void tearDown() {
        cartMapperStatic.close();
        orderMapperStatic.close();
        productMapperStatic.close();
    }

    // ── Full order lifecycle: add → checkout → ship ────────────────

    @Test
    void fullLifecycle_addToCart_checkout_ship() {
        // Step 1: Add item to cart
        ProductEntity pe = new ProductEntity(PRODUCT_ID, "Burger", List.of(), 10.0, null, "img.png", List.of());
        Product product = new Product(PRODUCT_ID, "Burger", List.of(), 10.0, null, "img.png", List.of());
        OrderEntity cartEntity = new OrderEntity(1L, new ArrayList<>(), Estado.CARRITO.name(),
                null, new Date(), BigDecimal.ZERO, USER_ID);
        OrderItemEntity newItem = new OrderItemEntity(null, pe, 2, BigDecimal.valueOf(10.0));
        OrderEntity cartWithItem = new OrderEntity(1L, List.of(newItem), Estado.CARRITO.name(),
                null, new Date(), BigDecimal.valueOf(20), USER_ID);

        when(orderRepository.findByUserIdAndEstado(USER_ID, Estado.CARRITO.name()))
                .thenReturn(Optional.of(cartEntity));
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(pe));
        when(productMapper.fromProductEntityToProduct(pe)).thenReturn(product);
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(cartWithItem);

        CartDto cartDto = new CartDto(1L, USER_ID, List.of(), "CARRITO", BigDecimal.valueOf(20));
        when(cartMapper.fromOrderEntityToCartDto(cartWithItem)).thenReturn(cartDto);

        CartDto addResult = cartService.addItemToCart(USER_ID, PRODUCT_ID, 2);
        assertNotNull(addResult);

        // Step 2: Checkout
        when(orderRepository.findByUserIdAndEstado(USER_ID, Estado.CARRITO.name()))
                .thenReturn(Optional.of(cartWithItem));
        OrderEntity pendienteEntity = new OrderEntity(1L, List.of(newItem), Estado.PENDIENTE.name(),
                "Calle 1", new Date(), BigDecimal.valueOf(20), USER_ID);
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(pendienteEntity);

        Order pendienteOrder = new Order(1L, List.of(), Estado.PENDIENTE, "Calle 1",
                new Date(), BigDecimal.valueOf(20), USER_ID);
        OrderDto pendienteDto = new OrderDto(1L, List.of(), "PENDIENTE", "Calle 1",
                new Date(), BigDecimal.valueOf(20), USER_ID);
        when(orderMapper.fromOrderEntityToOrder(pendienteEntity)).thenReturn(pendienteOrder);
        when(orderMapper.fromOrderToOrderDto(pendienteOrder)).thenReturn(pendienteDto);

        OrderDto checkoutResult = cartService.checkout(USER_ID, "Calle 1");
        assertEquals("PENDIENTE", checkoutResult.estado());
    }

    @Test
    void fullLifecycle_addToCart_payWithCard_ship() {
        // Step 1: Setup cart with items
        ProductEntity pe = new ProductEntity(PRODUCT_ID, "Burger", List.of(), 10.0, null, "img.png", List.of());
        OrderItemEntity item = new OrderItemEntity(1L, pe, 2, BigDecimal.valueOf(10.0));
        OrderEntity cartEntity = new OrderEntity(1L, List.of(item), Estado.CARRITO.name(),
                null, new Date(), BigDecimal.valueOf(20), USER_ID);

        when(orderRepository.findByUserIdAndEstado(USER_ID, Estado.CARRITO.name()))
                .thenReturn(Optional.of(cartEntity));
        doNothing().when(bankPaymentService).processCardPayment(any(CardPaymentRequest.class));

        OrderEntity pagadoEntity = new OrderEntity(1L, List.of(item), Estado.PAGADO.name(),
                "Calle 1", new Date(), BigDecimal.valueOf(20), USER_ID);
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(pagadoEntity);

        Order pagadoOrder = new Order(1L, List.of(), Estado.PAGADO, "Calle 1",
                new Date(), BigDecimal.valueOf(20), USER_ID);
        OrderDto pagadoDto = new OrderDto(1L, List.of(), "PAGADO", "Calle 1",
                new Date(), BigDecimal.valueOf(20), USER_ID);
        when(orderMapper.fromOrderEntityToOrder(pagadoEntity)).thenReturn(pagadoOrder);
        when(orderMapper.fromOrderToOrderDto(pagadoOrder)).thenReturn(pagadoDto);

        // Step 2: Pay with card
        OrderDto payResult = cartService.payWithCard(USER_ID, "Calle 1",
                "4111111111111111", "12", "2030", "123", "Test User", "ES12345");
        assertEquals("PAGADO", payResult.estado());
        verify(bankPaymentService).processCardPayment(any(CardPaymentRequest.class));

        // Step 3: Ship the order
        when(orderRepository.findById(1L)).thenReturn(Optional.of(pagadoEntity));
        OrderEntity enviadoEntity = new OrderEntity(1L, List.of(item), Estado.ENVIADO.name(),
                "Calle 1", new Date(), BigDecimal.valueOf(20), USER_ID);
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(enviadoEntity);

        Order enviadoOrder = new Order(1L, List.of(), Estado.ENVIADO, "Calle 1",
                new Date(), BigDecimal.valueOf(20), USER_ID);
        OrderDto enviadoDto = new OrderDto(1L, List.of(), "ENVIADO", "Calle 1",
                new Date(), BigDecimal.valueOf(20), USER_ID);
        when(orderMapper.fromOrderEntityToOrder(enviadoEntity)).thenReturn(enviadoOrder);
        when(orderMapper.fromOrderToOrderDto(enviadoOrder)).thenReturn(enviadoDto);

        OrderDto shipResult = orderService.markAsShipped(1L);
        assertEquals("ENVIADO", shipResult.estado());
    }

    // ── Cancel from different states ───────────────────────────────

    @Test
    void cancelOrder_fromPendiente_success() {
        OrderEntity pendienteEntity = new OrderEntity(1L, List.of(), Estado.PENDIENTE.name(),
                "Calle 1", new Date(), BigDecimal.TEN, USER_ID);
        OrderEntity canceladoEntity = new OrderEntity(1L, List.of(), Estado.CANCELADO.name(),
                "Calle 1", new Date(), BigDecimal.TEN, USER_ID);
        Order order = new Order(1L, List.of(), Estado.CANCELADO, "Calle 1",
                new Date(), BigDecimal.TEN, USER_ID);
        OrderDto dto = new OrderDto(1L, List.of(), "CANCELADO", "Calle 1",
                new Date(), BigDecimal.TEN, USER_ID);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(pendienteEntity));
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(canceladoEntity);
        when(orderMapper.fromOrderEntityToOrder(canceladoEntity)).thenReturn(order);
        when(orderMapper.fromOrderToOrderDto(order)).thenReturn(dto);

        OrderDto result = orderService.cancelOrder(1L);
        assertEquals("CANCELADO", result.estado());
    }

    @Test
    void cancelOrder_fromEnviado_throwsBusinessException() {
        OrderEntity enviadoEntity = new OrderEntity(1L, List.of(), Estado.ENVIADO.name(),
                "Calle 1", new Date(), BigDecimal.TEN, USER_ID);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(enviadoEntity));

        assertThrows(BusinessException.class, () -> orderService.cancelOrder(1L));
    }

    // ── Cart edge cases ────────────────────────────────────────────

    @Test
    void addItemToCart_thenRemoveItem_cartBecomesEmpty() {
        ProductEntity pe = new ProductEntity(PRODUCT_ID, "Burger", List.of(), 10.0, null, "img.png", List.of());
        Product product = new Product(PRODUCT_ID, "Burger", List.of(), 10.0, null, "img.png", List.of());
        OrderItemEntity item = new OrderItemEntity(1L, pe, 2, BigDecimal.valueOf(10.0));
        OrderEntity cartWithItem = new OrderEntity(1L, new ArrayList<>(List.of(item)), Estado.CARRITO.name(),
                null, new Date(), BigDecimal.valueOf(20), USER_ID);

        // Remove item
        when(orderRepository.findByUserIdAndEstado(USER_ID, Estado.CARRITO.name()))
                .thenReturn(Optional.of(cartWithItem));
        OrderEntity emptyCart = new OrderEntity(1L, new ArrayList<>(), Estado.CARRITO.name(),
                null, new Date(), BigDecimal.ZERO, USER_ID);
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(emptyCart);

        CartDto emptyCartDto = new CartDto(1L, USER_ID, List.of(), "CARRITO", BigDecimal.ZERO);
        when(cartMapper.fromOrderEntityToCartDto(emptyCart)).thenReturn(emptyCartDto);

        CartDto result = cartService.removeItemFromCart(USER_ID, PRODUCT_ID);
        assertEquals(BigDecimal.ZERO, result.totalPrice());
    }

    @Test
    void checkout_emptyCart_throwsBusinessException() {
        OrderEntity emptyCart = new OrderEntity(1L, List.of(), Estado.CARRITO.name(),
                null, new Date(), BigDecimal.ZERO, USER_ID);
        when(orderRepository.findByUserIdAndEstado(USER_ID, Estado.CARRITO.name()))
                .thenReturn(Optional.of(emptyCart));

        assertThrows(BusinessException.class,
                () -> cartService.checkout(USER_ID, "Calle 1"));
    }

    @Test
    void payWithCard_bankFails_orderNotSaved() {
        ProductEntity pe = new ProductEntity(PRODUCT_ID, "Burger", List.of(), 10.0, null, "img.png", List.of());
        OrderItemEntity item = new OrderItemEntity(1L, pe, 1, BigDecimal.valueOf(10.0));
        OrderEntity cartEntity = new OrderEntity(1L, List.of(item), Estado.CARRITO.name(),
                null, new Date(), BigDecimal.valueOf(10), USER_ID);

        when(orderRepository.findByUserIdAndEstado(USER_ID, Estado.CARRITO.name()))
                .thenReturn(Optional.of(cartEntity));
        doThrow(new RuntimeException("Bank error"))
                .when(bankPaymentService).processCardPayment(any(CardPaymentRequest.class));

        assertThrows(RuntimeException.class,
                () -> cartService.payWithCard(USER_ID, "Calle 1",
                        "4111111111111111", "12", "2030", "123", "Test User", "ES12345"));

        verify(orderRepository, never()).save(argThat(e -> Estado.PAGADO.name().equals(e.estado())));
    }

    @Test
    void deleteCartById_nonCarritoEstado_throwsBusinessException() {
        OrderEntity orderEntity = new OrderEntity(1L, List.of(), Estado.PENDIENTE.name(),
                "Calle 1", new Date(), BigDecimal.TEN, USER_ID);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(orderEntity));

        assertThrows(BusinessException.class, () -> cartService.deleteCartById(1L));
        verify(orderRepository, never()).deleteById(1L);
    }

    @Test
    void getOrderById_carritoEstado_throwsBusinessException() {
        OrderEntity carritoEntity = new OrderEntity(1L, List.of(), Estado.CARRITO.name(),
                null, new Date(), BigDecimal.ZERO, USER_ID);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(carritoEntity));

        assertThrows(BusinessException.class, () -> orderService.getOrderById(1L));
    }

    @Test
    void markAsShipped_fromPendiente_throwsBusinessException() {
        OrderEntity pendienteEntity = new OrderEntity(1L, List.of(), Estado.PENDIENTE.name(),
                "Calle 1", new Date(), BigDecimal.TEN, USER_ID);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(pendienteEntity));

        assertThrows(BusinessException.class, () -> orderService.markAsShipped(1L));
    }

    @Test
    void getOrdersByUserId_excludesCarrito() {
        OrderEntity pendienteEntity = new OrderEntity(1L, List.of(), Estado.PENDIENTE.name(),
                "Calle 1", new Date(), BigDecimal.TEN, USER_ID);
        Order order = new Order(1L, List.of(), Estado.PENDIENTE, "Calle 1",
                new Date(), BigDecimal.TEN, USER_ID);
        OrderDto dto = new OrderDto(1L, List.of(), "PENDIENTE", "Calle 1",
                new Date(), BigDecimal.TEN, USER_ID);

        when(orderRepository.findByUserIdExcludingEstado(USER_ID, Estado.CARRITO.name()))
                .thenReturn(List.of(pendienteEntity));
        when(orderMapper.fromOrderEntityToOrder(pendienteEntity)).thenReturn(order);
        when(orderMapper.fromOrderToOrderDto(order)).thenReturn(dto);

        List<OrderDto> result = orderService.getOrdersByUserId(USER_ID);

        assertEquals(1, result.size());
        assertEquals("PENDIENTE", result.get(0).estado());
    }

    @Test
    void createCartForUser_alreadyHasCart_throwsBusinessException() {
        OrderEntity existingCart = new OrderEntity(1L, List.of(), Estado.CARRITO.name(),
                null, new Date(), BigDecimal.ZERO, USER_ID);
        when(orderRepository.findByUserIdAndEstado(USER_ID, Estado.CARRITO.name()))
                .thenReturn(Optional.of(existingCart));

        assertThrows(BusinessException.class, () -> cartService.createCartForUser(USER_ID));
    }
}
