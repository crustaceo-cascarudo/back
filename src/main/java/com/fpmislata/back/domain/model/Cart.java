package com.fpmislata.back.domain.model;

import com.fpmislata.back.domain.enumerado.Estado;
import java.math.BigDecimal;
import java.util.List;

public class Cart {
    private Long id;
    private Long userId;
    private List<OrderItem> products;
    private Estado estado;
    private BigDecimal totalPrice;

    public Cart() {}

    public Cart(Long id, Long userId, List<OrderItem> products, Estado estado, BigDecimal totalPrice) {
        this.id = id;
        this.userId = userId;
        this.products = products;
        this.estado = estado;
        this.totalPrice = totalPrice;
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public List<OrderItem> getProducts() { return products; }
    public Estado getEstado() { return estado; }
    public BigDecimal getTotalPrice() { return totalPrice; }
}