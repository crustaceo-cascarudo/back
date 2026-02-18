package com.fpmislata.back.persistence.repository.mapper;

import com.fpmislata.back.domain.repository.entity.OrderEntity;
import com.fpmislata.back.domain.repository.entity.OrderItemEntity;
import com.fpmislata.back.domain.repository.entity.ProductEntity;
import com.fpmislata.back.persistence.dao.impl.entity.OrderItemJpaEntity;
import com.fpmislata.back.persistence.dao.impl.entity.OrderJpaEntity;
import com.fpmislata.back.persistence.dao.impl.entity.ProductJpaEntity;

import java.util.ArrayList;
import java.util.List;

public class OrderMapper {

    private static OrderMapper instance;

    private OrderMapper() {}

    public static OrderMapper getInstance() {
        if (instance == null) {
            instance = new OrderMapper();
        }
        return instance;
    }

    public OrderEntity fromOrderJpaEntityToOrderEntity(OrderJpaEntity jpaEntity) {
        if (jpaEntity == null) return null;

        List<OrderItemEntity> items = new ArrayList<>();
        if (jpaEntity.getItems() != null) {
            items = jpaEntity.getItems().stream()
                    .map(this::fromOrderItemJpaEntityToOrderItemEntity)
                    .toList();
        }

        return new OrderEntity(
                jpaEntity.getId(),
                items,
                jpaEntity.getEstado(),
                jpaEntity.getAddress(),
                jpaEntity.getOrderDate(),
                jpaEntity.getTotalPrice(),
                jpaEntity.getUserId()
        );
    }

    public OrderJpaEntity fromOrderEntityToOrderJpaEntity(OrderEntity orderEntity) {
        if (orderEntity == null) return null;

        OrderJpaEntity jpaEntity = new OrderJpaEntity();
        jpaEntity.setId(orderEntity.id());
        jpaEntity.setEstado(orderEntity.estado());
        jpaEntity.setAddress(orderEntity.address());
        jpaEntity.setOrderDate(orderEntity.orderDate());
        jpaEntity.setTotalPrice(orderEntity.totalPrice());
        jpaEntity.setUserId(orderEntity.userId());

        List<OrderItemJpaEntity> jpaItems = new ArrayList<>();
        if (orderEntity.products() != null) {
            for (OrderItemEntity itemEntity : orderEntity.products()) {
                OrderItemJpaEntity itemJpa = fromOrderItemEntityToOrderItemJpaEntity(itemEntity);
                itemJpa.setOrder(jpaEntity);
                jpaItems.add(itemJpa);
            }
        }
        jpaEntity.setItems(jpaItems);

        return jpaEntity;
    }

    public OrderItemEntity fromOrderItemJpaEntityToOrderItemEntity(OrderItemJpaEntity jpaItem) {
        if (jpaItem == null) return null;

        ProductEntity productEntity = null;
        if (jpaItem.getProduct() != null) {
            productEntity = ProductMapper.getInstance()
                    .fromProductJpaEntityToProductEntity(jpaItem.getProduct());
        }

        return new OrderItemEntity(
                jpaItem.getId(),
                productEntity,
                jpaItem.getQuantity(),
                jpaItem.getItemPrice()
        );
    }

    public OrderItemJpaEntity fromOrderItemEntityToOrderItemJpaEntity(OrderItemEntity itemEntity) {
        if (itemEntity == null) return null;

        OrderItemJpaEntity jpaItem = new OrderItemJpaEntity();
        jpaItem.setId(itemEntity.id());
        jpaItem.setQuantity(itemEntity.quantity());
        jpaItem.setItemPrice(itemEntity.itemPrice());

        if (itemEntity.productEntity() != null) {
            ProductJpaEntity productJpa = new ProductJpaEntity();
            // Solo seteamos el id para que JPA haga la referencia
            productJpa = ProductMapper.getInstance()
                    .fromProductEntityToProductJpaEntity(itemEntity.productEntity());
            jpaItem.setProduct(productJpa);
        }

        return jpaItem;
    }
}
