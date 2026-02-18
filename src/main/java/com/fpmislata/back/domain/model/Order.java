package com.fpmislata.back.domain.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fpmislata.back.domain.enumerado.Estado;

public class Order {
  private Long id;
  private List<OrderItem> products;
  private Estado estado;
  private String address;
  private Date orderDate;
  private BigDecimal totalPrice;
  private Long userId;

  public Order() {}

  public Order(Long id, List<OrderItem> products, Estado estado, String address, Date orderDate, BigDecimal totalPrice, Long userId) {
    this.id = id;
    this.products = products;
    this.estado = estado;
    this.address = address;
    this.orderDate = orderDate;
    this.totalPrice = totalPrice;
    this.userId = userId;
  }

  public Long getId() { return id; }
  public List<OrderItem> getProducts() { return products; }
  public Estado getEstado() { return estado; }
  public String getAddress() { return address; }
  public Date getOrderDate() { return orderDate; }
  public BigDecimal getTotalPrice() { return totalPrice; }
  public Long getUserId() { return userId; }
}
