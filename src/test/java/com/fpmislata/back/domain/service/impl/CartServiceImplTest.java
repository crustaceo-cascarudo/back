package com.fpmislata.back.domain.service.impl;

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
import com.fpmislata.back.infrastructure.payment.request.CardPaymentRequest;
import com.fpmislata.back.infrastructure.payment.service.BankPaymentService;

class CartServiceImplTest {

    private OrderRepository orderRepository;
    private ProductRepository productRepository;
    private BankPaymentService bankPaymentService;
    private CartServiceImpl cartService;

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

    // ── Helper builders ────────────────────────────────────────────

    private OrderEntity buildCartEntity(Long id, List<OrderItemEntity> items, BigDecimal total) {
        return new OrderEntity(id, items, Estado.CARRITO.name(), null, new Date(), total, USER_ID);
    }

    private ProductEntity buildProductEntity(Long id, String name, double basePrice) {
        return new ProductEntity(id, name, List.of(), basePrice, null, "img.png", List.of());
    }

    private Product buildProduct(Long id, String name, double basePrice) {
        return new Product(id, name, List.of(), basePrice, null, "img.png", List.of());
    }

    private CartDto buildCartDto(Long id, List<OrderItemDto> items, BigDecimal total) {
        return new CartDto(id, USER_ID, items, Estado.CARRITO.name(), total);
    }

    // ── getActiveCartByUserId ──────────────────────────────────────

    @Test
    void getActiveCartByUserId_exists_returnsCartDto() {
        OrderEntity cartEntity = buildCartEntity(1L, List.of(), BigDecimal.ZERO);
        CartDto expectedDto = buildCartDto(1L, List.of(), BigDecimal.ZERO);

        when(orderRepository.findByUserIdAndEstado(USER_ID, Estado.CARRITO.name()))
                .thenReturn(Optional.of(cartEntity));
        when(cartMapper.fromOrderEntityToCartDto(cartEntity)).thenReturn(expectedDto);

        CartDto result = cartService.getActiveCartByUserId(USER_ID);

        assertEquals(expectedDto, result);
    }

    @Test
    void getActiveCartByUserId_notFound_throwsResourceNotFoundException() {
        when(orderRepository.findByUserIdAndEstado(USER_ID, Estado.CARRITO.name()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> cartService.getActiveCartByUserId(USER_ID));
    }

    // ── createCartForUser ──────────────────────────────────────────

    @Test
    void createCartForUser_noExistingCart_createsNew() {
        when(orderRepository.findByUserIdAndEstado(USER_ID, Estado.CARRITO.name()))
                .thenReturn(Optional.empty());

        OrderEntity savedEntity = buildCartEntity(10L, new ArrayList<>(), BigDecimal.ZERO);
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(savedEntity);

        CartDto expectedDto = buildCartDto(10L, List.of(), BigDecimal.ZERO);
        when(cartMapper.fromOrderEntityToCartDto(savedEntity)).thenReturn(expectedDto);

        CartDto result = cartService.createCartForUser(USER_ID);

        assertEquals(expectedDto, result);
        verify(orderRepository).save(any(OrderEntity.class));
    }

    @Test
    void createCartForUser_alreadyExists_throwsBusinessException() {
        OrderEntity existing = buildCartEntity(5L, List.of(), BigDecimal.ZERO);
        when(orderRepository.findByUserIdAndEstado(USER_ID, Estado.CARRITO.name()))
                .thenReturn(Optional.of(existing));

        BusinessException ex = assertThrows(BusinessException.class,
                () -> cartService.createCartForUser(USER_ID));
        assertTrue(ex.getMessage().contains("carrito activo"));
    }

    // ── addItemToCart ──────────────────────────────────────────────

    @Test
    void addItemToCart_newItem_addsToCart() {
        OrderEntity cartEntity = buildCartEntity(1L, new ArrayList<>(), BigDecimal.ZERO);
        ProductEntity productEntity = buildProductEntity(PRODUCT_ID, "Burger", 10.0);
        Product product = buildProduct(PRODUCT_ID, "Burger", 10.0);

        when(orderRepository.findByUserIdAndEstado(USER_ID, Estado.CARRITO.name()))
                .thenReturn(Optional.of(cartEntity));
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(productEntity));
        when(productMapper.fromProductEntityToProduct(productEntity)).thenReturn(product);

        OrderEntity savedCart = buildCartEntity(1L, List.of(
                new OrderItemEntity(null, productEntity, 2, BigDecimal.valueOf(10.0))
        ), BigDecimal.valueOf(20.0));
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(savedCart);

        CartDto expectedDto = buildCartDto(1L, List.of(), BigDecimal.valueOf(20.0));
        when(cartMapper.fromOrderEntityToCartDto(savedCart)).thenReturn(expectedDto);

        CartDto result = cartService.addItemToCart(USER_ID, PRODUCT_ID, 2);

        assertEquals(expectedDto, result);
        verify(orderRepository).save(any(OrderEntity.class));
    }

    @Test
    void addItemToCart_existingItem_mergesQuantity() {
        ProductEntity productEntity = buildProductEntity(PRODUCT_ID, "Burger", 10.0);
        OrderItemEntity existingItem = new OrderItemEntity(1L, productEntity, 3, BigDecimal.valueOf(10.0));
        OrderEntity cartEntity = buildCartEntity(1L, new ArrayList<>(List.of(existingItem)), BigDecimal.valueOf(30.0));
        Product product = buildProduct(PRODUCT_ID, "Burger", 10.0);

        when(orderRepository.findByUserIdAndEstado(USER_ID, Estado.CARRITO.name()))
                .thenReturn(Optional.of(cartEntity));
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(productEntity));
        when(productMapper.fromProductEntityToProduct(productEntity)).thenReturn(product);

        OrderEntity savedCart = buildCartEntity(1L, List.of(
                new OrderItemEntity(1L, productEntity, 5, BigDecimal.valueOf(10.0))
        ), BigDecimal.valueOf(50.0));
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(savedCart);

        CartDto expectedDto = buildCartDto(1L, List.of(), BigDecimal.valueOf(50.0));
        when(cartMapper.fromOrderEntityToCartDto(savedCart)).thenReturn(expectedDto);

        CartDto result = cartService.addItemToCart(USER_ID, PRODUCT_ID, 2);

        assertEquals(expectedDto, result);
        verify(orderRepository).save(argThat(e -> {
            OrderItemEntity item = e.products().get(0);
            return item.quantity() == 5;
        }));
    }

    @Test
    void addItemToCart_noCartExists_autoCreatesCart() {
        OrderEntity newCart = buildCartEntity(20L, new ArrayList<>(), BigDecimal.ZERO);
        ProductEntity productEntity = buildProductEntity(PRODUCT_ID, "Burger", 10.0);
        Product product = buildProduct(PRODUCT_ID, "Burger", 10.0);

        when(orderRepository.findByUserIdAndEstado(USER_ID, Estado.CARRITO.name()))
                .thenReturn(Optional.empty());
        when(orderRepository.save(any(OrderEntity.class)))
                .thenReturn(newCart)
                .thenReturn(newCart);
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(productEntity));
        when(productMapper.fromProductEntityToProduct(productEntity)).thenReturn(product);

        CartDto expectedDto = buildCartDto(20L, List.of(), BigDecimal.valueOf(10.0));
        when(cartMapper.fromOrderEntityToCartDto(any(OrderEntity.class))).thenReturn(expectedDto);

        CartDto result = cartService.addItemToCart(USER_ID, PRODUCT_ID, 1);

        assertNotNull(result);
        verify(orderRepository, atLeast(2)).save(any(OrderEntity.class));
    }

    @Test
    void addItemToCart_quantityZeroOrNegative_throwsBusinessException() {
        BusinessException ex = assertThrows(BusinessException.class,
                () -> cartService.addItemToCart(USER_ID, PRODUCT_ID, 0));
        assertTrue(ex.getMessage().contains("mayor que cero"));

        assertThrows(BusinessException.class,
                () -> cartService.addItemToCart(USER_ID, PRODUCT_ID, -1));
    }

    @Test
    void addItemToCart_productNotFound_throwsResourceNotFoundException() {
        OrderEntity cartEntity = buildCartEntity(1L, new ArrayList<>(), BigDecimal.ZERO);
        when(orderRepository.findByUserIdAndEstado(USER_ID, Estado.CARRITO.name()))
                .thenReturn(Optional.of(cartEntity));
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> cartService.addItemToCart(USER_ID, PRODUCT_ID, 1));
    }

    // ── removeItemFromCart ─────────────────────────────────────────

    @Test
    void removeItemFromCart_itemExists_removesAndRecalculatesTotal() {
        ProductEntity productEntity = buildProductEntity(PRODUCT_ID, "Burger", 10.0);
        OrderItemEntity item = new OrderItemEntity(1L, productEntity, 2, BigDecimal.valueOf(10.0));
        OrderEntity cartEntity = buildCartEntity(1L, new ArrayList<>(List.of(item)), BigDecimal.valueOf(20.0));

        when(orderRepository.findByUserIdAndEstado(USER_ID, Estado.CARRITO.name()))
                .thenReturn(Optional.of(cartEntity));

        OrderEntity savedCart = buildCartEntity(1L, new ArrayList<>(), BigDecimal.ZERO);
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(savedCart);

        CartDto expectedDto = buildCartDto(1L, List.of(), BigDecimal.ZERO);
        when(cartMapper.fromOrderEntityToCartDto(savedCart)).thenReturn(expectedDto);

        CartDto result = cartService.removeItemFromCart(USER_ID, PRODUCT_ID);

        assertEquals(BigDecimal.ZERO, result.totalPrice());
        verify(orderRepository).save(argThat(e -> e.products().isEmpty()));
    }

    @Test
    void removeItemFromCart_productNotInCart_throwsResourceNotFoundException() {
        OrderEntity cartEntity = buildCartEntity(1L, new ArrayList<>(), BigDecimal.ZERO);
        when(orderRepository.findByUserIdAndEstado(USER_ID, Estado.CARRITO.name()))
                .thenReturn(Optional.of(cartEntity));

        assertThrows(ResourceNotFoundException.class,
                () -> cartService.removeItemFromCart(USER_ID, 999L));
    }

    @Test
    void removeItemFromCart_noCart_throwsResourceNotFoundException() {
        when(orderRepository.findByUserIdAndEstado(USER_ID, Estado.CARRITO.name()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> cartService.removeItemFromCart(USER_ID, PRODUCT_ID));
    }

    // ── updateItemQuantity ─────────────────────────────────────────

    @Test
    void updateItemQuantity_positiveQuantity_updatesItem() {
        ProductEntity productEntity = buildProductEntity(PRODUCT_ID, "Burger", 10.0);
        OrderItemEntity item = new OrderItemEntity(1L, productEntity, 2, BigDecimal.valueOf(10.0));
        OrderEntity cartEntity = buildCartEntity(1L, new ArrayList<>(List.of(item)), BigDecimal.valueOf(20.0));

        when(orderRepository.findByUserIdAndEstado(USER_ID, Estado.CARRITO.name()))
                .thenReturn(Optional.of(cartEntity));

        OrderEntity savedCart = buildCartEntity(1L, List.of(
                new OrderItemEntity(1L, productEntity, 5, BigDecimal.valueOf(10.0))
        ), BigDecimal.valueOf(50.0));
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(savedCart);

        CartDto expectedDto = buildCartDto(1L, List.of(), BigDecimal.valueOf(50.0));
        when(cartMapper.fromOrderEntityToCartDto(savedCart)).thenReturn(expectedDto);

        CartDto result = cartService.updateItemQuantity(USER_ID, PRODUCT_ID, 5);

        assertEquals(BigDecimal.valueOf(50.0), result.totalPrice());
        verify(orderRepository).save(argThat(e -> e.products().get(0).quantity() == 5));
    }

    @Test
    void updateItemQuantity_zeroQuantity_removesItem() {
        ProductEntity productEntity = buildProductEntity(PRODUCT_ID, "Burger", 10.0);
        OrderItemEntity item = new OrderItemEntity(1L, productEntity, 2, BigDecimal.valueOf(10.0));
        OrderEntity cartEntity = buildCartEntity(1L, new ArrayList<>(List.of(item)), BigDecimal.valueOf(20.0));

        when(orderRepository.findByUserIdAndEstado(USER_ID, Estado.CARRITO.name()))
                .thenReturn(Optional.of(cartEntity));

        OrderEntity savedCart = buildCartEntity(1L, new ArrayList<>(), BigDecimal.ZERO);
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(savedCart);

        CartDto expectedDto = buildCartDto(1L, List.of(), BigDecimal.ZERO);
        when(cartMapper.fromOrderEntityToCartDto(savedCart)).thenReturn(expectedDto);

        CartDto result = cartService.updateItemQuantity(USER_ID, PRODUCT_ID, 0);

        assertEquals(BigDecimal.ZERO, result.totalPrice());
        verify(orderRepository).save(argThat(e -> e.products().isEmpty()));
    }

    @Test
    void updateItemQuantity_negativeQuantity_throwsBusinessException() {
        BusinessException ex = assertThrows(BusinessException.class,
                () -> cartService.updateItemQuantity(USER_ID, PRODUCT_ID, -1));
        assertTrue(ex.getMessage().contains("negativa"));
    }

    // ── clearCart ──────────────────────────────────────────────────

    @Test
    void clearCart_cartExists_deletesItemsAndSavesEmpty() {
        OrderEntity cartEntity = buildCartEntity(1L, List.of(
                new OrderItemEntity(1L, buildProductEntity(PRODUCT_ID, "Burger", 10.0),
                        2, BigDecimal.valueOf(10.0))
        ), BigDecimal.valueOf(20.0));

        when(orderRepository.findByUserIdAndEstado(USER_ID, Estado.CARRITO.name()))
                .thenReturn(Optional.of(cartEntity));
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(cartEntity);

        cartService.clearCart(USER_ID);

        verify(orderRepository).deleteItemsByOrderId(1L);
        verify(orderRepository).save(argThat(e ->
                e.products().isEmpty() && BigDecimal.ZERO.equals(e.totalPrice())));
    }

    @Test
    void clearCart_noCart_throwsResourceNotFoundException() {
        when(orderRepository.findByUserIdAndEstado(USER_ID, Estado.CARRITO.name()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> cartService.clearCart(USER_ID));
    }

    // ── deleteCartById ─────────────────────────────────────────────

    @Test
    void deleteCartById_isCarrito_deletesSuccessfully() {
        OrderEntity cartEntity = buildCartEntity(1L, List.of(), BigDecimal.ZERO);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(cartEntity));

        cartService.deleteCartById(1L);

        verify(orderRepository).deleteById(1L);
    }

    @Test
    void deleteCartById_notCarrito_throwsBusinessException() {
        OrderEntity orderEntity = new OrderEntity(1L, List.of(), Estado.PENDIENTE.name(),
                "Calle 1", new Date(), BigDecimal.TEN, USER_ID);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(orderEntity));

        BusinessException ex = assertThrows(BusinessException.class,
                () -> cartService.deleteCartById(1L));
        assertTrue(ex.getMessage().contains("carritos activos"));
    }

    @Test
    void deleteCartById_notFound_throwsResourceNotFoundException() {
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> cartService.deleteCartById(999L));
    }

    // ── checkout ───────────────────────────────────────────────────

    @Test
    void checkout_validCartWithAddress_transitionsToPendiente() {
        ProductEntity pe = buildProductEntity(PRODUCT_ID, "Burger", 10.0);
        OrderItemEntity item = new OrderItemEntity(1L, pe, 2, BigDecimal.valueOf(10.0));
        OrderEntity cartEntity = buildCartEntity(1L, List.of(item), BigDecimal.valueOf(20.0));

        when(orderRepository.findByUserIdAndEstado(USER_ID, Estado.CARRITO.name()))
                .thenReturn(Optional.of(cartEntity));

        OrderEntity savedOrder = new OrderEntity(1L, List.of(item), Estado.PENDIENTE.name(),
                "Calle 1", new Date(), BigDecimal.valueOf(20.0), USER_ID);
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(savedOrder);

        Order order = new Order(1L, List.of(), Estado.PENDIENTE, "Calle 1",
                new Date(), BigDecimal.valueOf(20.0), USER_ID);
        OrderDto dto = new OrderDto(1L, List.of(), "PENDIENTE", "Calle 1",
                new Date(), BigDecimal.valueOf(20.0), USER_ID);

        when(orderMapper.fromOrderEntityToOrder(savedOrder)).thenReturn(order);
        when(orderMapper.fromOrderToOrderDto(order)).thenReturn(dto);

        OrderDto result = cartService.checkout(USER_ID, "Calle 1");

        assertEquals("PENDIENTE", result.estado());
        verify(orderRepository).save(argThat(e -> Estado.PENDIENTE.name().equals(e.estado())));
    }

    @Test
    void checkout_emptyCart_throwsBusinessException() {
        OrderEntity cartEntity = buildCartEntity(1L, List.of(), BigDecimal.ZERO);
        when(orderRepository.findByUserIdAndEstado(USER_ID, Estado.CARRITO.name()))
                .thenReturn(Optional.of(cartEntity));

        BusinessException ex = assertThrows(BusinessException.class,
                () -> cartService.checkout(USER_ID, "Calle 1"));
        assertTrue(ex.getMessage().contains("vacío"));
    }

    @Test
    void checkout_nullAddress_throwsBusinessException() {
        ProductEntity pe = buildProductEntity(PRODUCT_ID, "Burger", 10.0);
        OrderItemEntity item = new OrderItemEntity(1L, pe, 1, BigDecimal.valueOf(10.0));
        OrderEntity cartEntity = buildCartEntity(1L, List.of(item), BigDecimal.valueOf(10.0));

        when(orderRepository.findByUserIdAndEstado(USER_ID, Estado.CARRITO.name()))
                .thenReturn(Optional.of(cartEntity));

        BusinessException ex = assertThrows(BusinessException.class,
                () -> cartService.checkout(USER_ID, null));
        assertTrue(ex.getMessage().contains("dirección"));
    }

    @Test
    void checkout_blankAddress_throwsBusinessException() {
        ProductEntity pe = buildProductEntity(PRODUCT_ID, "Burger", 10.0);
        OrderItemEntity item = new OrderItemEntity(1L, pe, 1, BigDecimal.valueOf(10.0));
        OrderEntity cartEntity = buildCartEntity(1L, List.of(item), BigDecimal.valueOf(10.0));

        when(orderRepository.findByUserIdAndEstado(USER_ID, Estado.CARRITO.name()))
                .thenReturn(Optional.of(cartEntity));

        assertThrows(BusinessException.class,
                () -> cartService.checkout(USER_ID, "   "));
    }

    @Test
    void checkout_noActiveCart_throwsResourceNotFoundException() {
        when(orderRepository.findByUserIdAndEstado(USER_ID, Estado.CARRITO.name()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> cartService.checkout(USER_ID, "Calle 1"));
    }

    // ── payWithCard ────────────────────────────────────────────────

    @Test
    void payWithCard_success_transitionsToPagado() {
        ProductEntity pe = buildProductEntity(PRODUCT_ID, "Burger", 10.0);
        OrderItemEntity item = new OrderItemEntity(1L, pe, 2, BigDecimal.valueOf(10.0));
        OrderEntity cartEntity = buildCartEntity(1L, List.of(item), BigDecimal.valueOf(20.0));

        when(orderRepository.findByUserIdAndEstado(USER_ID, Estado.CARRITO.name()))
                .thenReturn(Optional.of(cartEntity));
        doNothing().when(bankPaymentService).processCardPayment(any(CardPaymentRequest.class));

        OrderEntity savedOrder = new OrderEntity(1L, List.of(item), Estado.PAGADO.name(),
                "Calle 1", new Date(), BigDecimal.valueOf(20.0), USER_ID);
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(savedOrder);

        Order order = new Order(1L, List.of(), Estado.PAGADO, "Calle 1",
                new Date(), BigDecimal.valueOf(20.0), USER_ID);
        OrderDto dto = new OrderDto(1L, List.of(), "PAGADO", "Calle 1",
                new Date(), BigDecimal.valueOf(20.0), USER_ID);

        when(orderMapper.fromOrderEntityToOrder(savedOrder)).thenReturn(order);
        when(orderMapper.fromOrderToOrderDto(order)).thenReturn(dto);

        OrderDto result = cartService.payWithCard(USER_ID, "Calle 1",
                "4111111111111111", "12", "2030", "123", "Test User", "ES1234567890");

        assertEquals("PAGADO", result.estado());
        verify(bankPaymentService).processCardPayment(any(CardPaymentRequest.class));
        verify(orderRepository).save(argThat(e -> Estado.PAGADO.name().equals(e.estado())));
    }

    @Test
    void payWithCard_bankServiceThrows_propagatesException() {
        ProductEntity pe = buildProductEntity(PRODUCT_ID, "Burger", 10.0);
        OrderItemEntity item = new OrderItemEntity(1L, pe, 1, BigDecimal.valueOf(10.0));
        OrderEntity cartEntity = buildCartEntity(1L, List.of(item), BigDecimal.valueOf(10.0));

        when(orderRepository.findByUserIdAndEstado(USER_ID, Estado.CARRITO.name()))
                .thenReturn(Optional.of(cartEntity));
        doThrow(new RuntimeException("Bank error"))
                .when(bankPaymentService).processCardPayment(any(CardPaymentRequest.class));

        assertThrows(RuntimeException.class,
                () -> cartService.payWithCard(USER_ID, "Calle 1",
                        "4111111111111111", "12", "2030", "123", "Test User", "ES1234567890"));

        verify(orderRepository, never()).save(argThat(e -> Estado.PAGADO.name().equals(e.estado())));
    }

    @Test
    void payWithCard_emptyCart_throwsBusinessException() {
        OrderEntity cartEntity = buildCartEntity(1L, List.of(), BigDecimal.ZERO);
        when(orderRepository.findByUserIdAndEstado(USER_ID, Estado.CARRITO.name()))
                .thenReturn(Optional.of(cartEntity));

        BusinessException ex = assertThrows(BusinessException.class,
                () -> cartService.payWithCard(USER_ID, "Calle 1",
                        "4111111111111111", "12", "2030", "123", "Test User", "ES1234567890"));
        assertTrue(ex.getMessage().contains("vacío"));
    }

    @Test
    void payWithCard_noAddress_throwsBusinessException() {
        ProductEntity pe = buildProductEntity(PRODUCT_ID, "Burger", 10.0);
        OrderItemEntity item = new OrderItemEntity(1L, pe, 1, BigDecimal.valueOf(10.0));
        OrderEntity cartEntity = buildCartEntity(1L, List.of(item), BigDecimal.valueOf(10.0));

        when(orderRepository.findByUserIdAndEstado(USER_ID, Estado.CARRITO.name()))
                .thenReturn(Optional.of(cartEntity));

        assertThrows(BusinessException.class,
                () -> cartService.payWithCard(USER_ID, null,
                        "4111111111111111", "12", "2030", "123", "Test User", "ES1234567890"));
    }

    @Test
    void payWithCard_noActiveCart_throwsResourceNotFoundException() {
        when(orderRepository.findByUserIdAndEstado(USER_ID, Estado.CARRITO.name()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> cartService.payWithCard(USER_ID, "Calle 1",
                        "4111111111111111", "12", "2030", "123", "Test User", "ES1234567890"));
    }
}
