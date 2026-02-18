package com.fpmislata.back.domain.model;

import java.math.BigDecimal;

public class OrderItem {
    private Long id;
    private Product product;
    private Integer quantity;
    private BigDecimal itemPrice;

    public OrderItem() {}

    public OrderItem(Long id, Product product, Integer quantity, BigDecimal itemPrice) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.itemPrice = itemPrice;
    }

    public Long getId() { return id; }
    public Product getProduct() { return product; }
    public Integer getQuantity() { return quantity; }
    public BigDecimal getItemPrice() { return itemPrice; }
}