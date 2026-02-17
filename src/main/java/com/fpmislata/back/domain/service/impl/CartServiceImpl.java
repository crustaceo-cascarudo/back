package com.fpmislata.back.domain.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.fpmislata.back.domain.Exception.BusinessException;
import com.fpmislata.back.domain.Exception.ResourceNotFoundException;
import com.fpmislata.back.domain.mapper.CartMapper;
import com.fpmislata.back.domain.mapper.OrderMapper;
import com.fpmislata.back.domain.mapper.ProductMapper;
import com.fpmislata.back.domain.model.Product;
import com.fpmislata.back.domain.enumerado.Estado;
import com.fpmislata.back.domain.repository.OrderRepository;
import com.fpmislata.back.domain.repository.ProductRepository;
import com.fpmislata.back.domain.repository.entity.OrderEntity;
import com.fpmislata.back.domain.repository.entity.OrderItemEntity;
import com.fpmislata.back.domain.repository.entity.ProductEntity;
import com.fpmislata.back.domain.service.CartService;
import com.fpmislata.back.domain.service.dto.CartDto;
import com.fpmislata.back.domain.service.dto.OrderDto;
import com.fpmislata.back.infrastructure.payment.service.BankPaymentService;
import com.fpmislata.back.infrastructure.payment.request.CardPaymentRequest;

import jakarta.transaction.Transactional;

public class CartServiceImpl implements CartService {

  private final OrderRepository orderRepository;
  private final ProductRepository productRepository;
  private final BankPaymentService bankPaymentService;

  public CartServiceImpl(OrderRepository orderRepository, ProductRepository productRepository,
      BankPaymentService bankPaymentService) {
    this.orderRepository = orderRepository;
    this.productRepository = productRepository;
    this.bankPaymentService = bankPaymentService;
  }

  @Override
  public CartDto getActiveCartByUserId(Long userId) {
    OrderEntity cartEntity = orderRepository.findByUserIdAndEstado(userId, Estado.CARRITO.name())
        .orElseThrow(
            () -> new ResourceNotFoundException("No se encontró carrito activo para el usuario con id: " + userId));
    return CartMapper.getInstance().fromOrderEntityToCartDto(cartEntity);
  }

  @Override
  @Transactional
  public CartDto createCartForUser(Long userId) {
    Optional<OrderEntity> existing = orderRepository.findByUserIdAndEstado(userId, Estado.CARRITO.name());
    if (existing.isPresent()) {
      throw new BusinessException("El usuario ya tiene un carrito activo");
    }
    OrderEntity newCart = new OrderEntity(
        null, new ArrayList<>(), Estado.CARRITO.name(),
        null, new Date(), BigDecimal.ZERO, userId);
    OrderEntity saved = orderRepository.save(newCart);
    return CartMapper.getInstance().fromOrderEntityToCartDto(saved);
  }

  @Override
  @Transactional
  public CartDto addItemToCart(Long userId, Long productId, int quantity) {
    if (quantity <= 0) {
      throw new BusinessException("La cantidad debe ser mayor que cero");
    }

    OrderEntity cartEntity = orderRepository.findByUserIdAndEstado(userId, Estado.CARRITO.name())
        .orElseGet(() -> {
          OrderEntity newCart = new OrderEntity(
              null, new ArrayList<>(), Estado.CARRITO.name(),
              null, new Date(), BigDecimal.ZERO, userId);
          return orderRepository.save(newCart);
        });

    ProductEntity productEntity = productRepository.findById(productId)
        .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + productId));

    Product product = ProductMapper.getInstance().fromProductEntityToProduct(productEntity);

    List<OrderItemEntity> updatedItems = new ArrayList<>(cartEntity.products());

    Optional<OrderItemEntity> existingItemOpt = updatedItems.stream()
        .filter(item -> item.productEntity().id().equals(productId))
        .findFirst();

    if (existingItemOpt.isPresent()) {
      OrderItemEntity existingItem = existingItemOpt.get();
      int newQuantity = existingItem.quantity() + quantity;
      OrderItemEntity updatedItem = new OrderItemEntity(
          existingItem.id(), existingItem.productEntity(),
          newQuantity, existingItem.itemPrice());
      int index = updatedItems.indexOf(existingItem);
      updatedItems.set(index, updatedItem);
    } else {
      OrderItemEntity newItem = new OrderItemEntity(
          null, productEntity, quantity,
          BigDecimal.valueOf(product.getFinalPrice()));
      updatedItems.add(newItem);
    }

    BigDecimal totalPrice = calculateTotal(updatedItems);
    OrderEntity updatedCart = new OrderEntity(
        cartEntity.id(), updatedItems, Estado.CARRITO.name(),
        cartEntity.address(), cartEntity.orderDate(), totalPrice, userId);
    OrderEntity savedCart = orderRepository.save(updatedCart);
    return CartMapper.getInstance().fromOrderEntityToCartDto(savedCart);
  }

  @Override
  @Transactional
  public CartDto removeItemFromCart(Long userId, Long productId) {
    OrderEntity cartEntity = orderRepository.findByUserIdAndEstado(userId, Estado.CARRITO.name())
        .orElseThrow(
            () -> new ResourceNotFoundException("No se encontró carrito activo para el usuario con id: " + userId));

    List<OrderItemEntity> updatedItems = new ArrayList<>(cartEntity.products());

    OrderItemEntity existingItem = updatedItems.stream()
        .filter(item -> item.productEntity().id().equals(productId))
        .findFirst()
        .orElseThrow(
            () -> new ResourceNotFoundException("Producto con id " + productId + " no encontrado en el carrito"));

    updatedItems.remove(existingItem);

    BigDecimal totalPrice = calculateTotal(updatedItems);
    OrderEntity updatedCart = new OrderEntity(
        cartEntity.id(), updatedItems, Estado.CARRITO.name(),
        cartEntity.address(), cartEntity.orderDate(), totalPrice, userId);
    OrderEntity savedCart = orderRepository.save(updatedCart);
    return CartMapper.getInstance().fromOrderEntityToCartDto(savedCart);
  }

  @Override
  @Transactional
  public CartDto updateItemQuantity(Long userId, Long productId, int quantity) {
    if (quantity < 0) {
      throw new BusinessException("La cantidad no puede ser negativa");
    }

    OrderEntity cartEntity = orderRepository.findByUserIdAndEstado(userId, Estado.CARRITO.name())
        .orElseThrow(
            () -> new ResourceNotFoundException("No se encontró carrito activo para el usuario con id: " + userId));

    List<OrderItemEntity> updatedItems = new ArrayList<>(cartEntity.products());

    OrderItemEntity existingItem = updatedItems.stream()
        .filter(item -> item.productEntity().id().equals(productId))
        .findFirst()
        .orElseThrow(
            () -> new ResourceNotFoundException("Producto con id " + productId + " no encontrado en el carrito"));

    if (quantity == 0) {
      updatedItems.remove(existingItem);
    } else {
      OrderItemEntity updatedItem = new OrderItemEntity(
          existingItem.id(), existingItem.productEntity(),
          quantity, existingItem.itemPrice());
      int index = updatedItems.indexOf(existingItem);
      updatedItems.set(index, updatedItem);
    }

    BigDecimal totalPrice = calculateTotal(updatedItems);
    OrderEntity updatedCart = new OrderEntity(
        cartEntity.id(), updatedItems, Estado.CARRITO.name(),
        cartEntity.address(), cartEntity.orderDate(), totalPrice, userId);
    OrderEntity savedCart = orderRepository.save(updatedCart);
    return CartMapper.getInstance().fromOrderEntityToCartDto(savedCart);
  }

  @Override
  @Transactional
  public void clearCart(Long userId) {
    OrderEntity cartEntity = orderRepository.findByUserIdAndEstado(userId, Estado.CARRITO.name())
        .orElseThrow(
            () -> new ResourceNotFoundException("No se encontró carrito activo para el usuario con id: " + userId));
    orderRepository.deleteItemsByOrderId(cartEntity.id());
    OrderEntity emptyCart = new OrderEntity(
        cartEntity.id(), new ArrayList<>(), Estado.CARRITO.name(),
        null, cartEntity.orderDate(), BigDecimal.ZERO, userId);
    orderRepository.save(emptyCart);
  }

  @Override
  @Transactional
  public void deleteCartById(Long id) {
    OrderEntity cartEntity = orderRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Carrito no encontrado con id: " + id));
    if (!Estado.CARRITO.name().equals(cartEntity.estado())) {
      throw new BusinessException("Solo se pueden eliminar carritos activos");
    }
    orderRepository.deleteById(id);
  }

  @Override
  @Transactional
  public OrderDto checkout(Long userId, String address) {
    OrderEntity cart = orderRepository.findByUserIdAndEstado(userId, Estado.CARRITO.name())
        .orElseThrow(
            () -> new ResourceNotFoundException("No se encontró carrito activo para el usuario con id: " + userId));

    if (cart.products() == null || cart.products().isEmpty()) {
      throw new BusinessException("No se puede realizar checkout de un carrito vacío");
    }

    if (address == null || address.isBlank()) {
      throw new BusinessException("La dirección es obligatoria para realizar el checkout");
    }

    OrderEntity order = new OrderEntity(
        cart.id(), cart.products(), Estado.PENDIENTE.name(),
        address, new Date(), cart.totalPrice(), cart.userId());

    OrderEntity savedOrder = orderRepository.save(order);
    return OrderMapper.getInstance().fromOrderToOrderDto(
        OrderMapper.getInstance().fromOrderEntityToOrder(savedOrder));
  }

  @Override
  @Transactional
  public OrderDto payWithCard(Long userId, String address, String cardNumber, String expiryMonth, String expiryYear,
      String cvc, String fullName, String accountIban) {
    OrderEntity cart = orderRepository.findByUserIdAndEstado(userId, Estado.CARRITO.name())
        .orElseThrow(
            () -> new ResourceNotFoundException("No se encontró carrito activo para el usuario con id: " + userId));

    if (cart.products() == null || cart.products().isEmpty()) {
      throw new BusinessException("No se puede pagar un carrito vacío");
    }

    if (address == null || address.isBlank()) {
      throw new BusinessException("La dirección es obligatoria para realizar el pago");
    }

    CardPaymentRequest paymentRequest = new CardPaymentRequest(
        cardNumber,
        expiryMonth,
        expiryYear,
        cvc,
        fullName,
        accountIban,
        cart.totalPrice(),
        "Crustaceo-cascarudo");

    bankPaymentService.processCardPayment(paymentRequest);

    OrderEntity order = new OrderEntity(
        cart.id(), cart.products(), Estado.PAGADO.name(),
        address, new Date(), cart.totalPrice(), cart.userId());

    OrderEntity savedOrder = orderRepository.save(order);
    return OrderMapper.getInstance().fromOrderToOrderDto(
        OrderMapper.getInstance().fromOrderEntityToOrder(savedOrder));
  }

  private BigDecimal calculateTotal(List<OrderItemEntity> items) {
    return items.stream()
        .map(item -> item.itemPrice().multiply(BigDecimal.valueOf(item.quantity())))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
