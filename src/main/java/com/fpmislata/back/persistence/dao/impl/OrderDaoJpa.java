package com.fpmislata.back.persistence.dao.impl;

import com.fpmislata.back.persistence.dao.OrderDao;
import com.fpmislata.back.persistence.dao.impl.entity.OrderItemJpaEntity;
import com.fpmislata.back.persistence.dao.impl.entity.OrderJpaEntity;
import com.fpmislata.back.persistence.dao.impl.entity.ProductJpaEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

public class OrderDaoJpa implements OrderDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<OrderJpaEntity> findById(Long id) {
        return Optional.ofNullable(entityManager.find(OrderJpaEntity.class, id));
    }

    @Override
    public Optional<OrderJpaEntity> findByUserIdAndEstado(Long userId, String estado) {
        String jpql = "SELECT o FROM OrderJpaEntity o WHERE o.userId = :userId AND o.estado = :estado";
        List<OrderJpaEntity> results = entityManager.createQuery(jpql, OrderJpaEntity.class)
                .setParameter("userId", userId)
                .setParameter("estado", estado)
                .getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public List<OrderJpaEntity> findByUserId(Long userId) {
        String jpql = "SELECT o FROM OrderJpaEntity o WHERE o.userId = :userId ORDER BY o.orderDate DESC";
        return entityManager.createQuery(jpql, OrderJpaEntity.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<OrderJpaEntity> findByUserIdExcludingEstado(Long userId, String estado) {
        String jpql = "SELECT o FROM OrderJpaEntity o WHERE o.userId = :userId AND o.estado != :estado ORDER BY o.orderDate DESC";
        return entityManager.createQuery(jpql, OrderJpaEntity.class)
                .setParameter("userId", userId)
                .setParameter("estado", estado)
                .getResultList();
    }

    @Override
    public OrderJpaEntity insert(OrderJpaEntity entity) {
        // Asegurar que los items referencien al order padre
        if (entity.getItems() != null) {
            for (OrderItemJpaEntity item : entity.getItems()) {
                item.setOrder(entity);
                // Asegurar que el producto está managed
                if (item.getProduct() != null && item.getProduct().getId() != null) {
                    ProductJpaEntity managedProduct = entityManager.find(ProductJpaEntity.class, item.getProduct().getId());
                    item.setProduct(managedProduct);
                }
            }
        }
        entityManager.persist(entity);
        entityManager.flush();
        return entity;
    }

    @Override
    public OrderJpaEntity update(OrderJpaEntity entity) {
        OrderJpaEntity managed = entityManager.find(OrderJpaEntity.class, entity.getId());
        if (managed == null) {
            throw new RuntimeException("Order not found with id " + entity.getId());
        }

        managed.setEstado(entity.getEstado());
        managed.setAddress(entity.getAddress());
        managed.setOrderDate(entity.getOrderDate());
        managed.setTotalPrice(entity.getTotalPrice());
        managed.setUserId(entity.getUserId());

        // Sincronizar items: limpiar los existentes y agregar los nuevos
        managed.getItems().clear();
        entityManager.flush();

        if (entity.getItems() != null) {
            for (OrderItemJpaEntity item : entity.getItems()) {
                OrderItemJpaEntity itemToAdd;
                
                // Si el item tiene ID, intentar recuperarlo del contexto
                if (item.getId() != null) {
                    OrderItemJpaEntity existingItem = entityManager.find(OrderItemJpaEntity.class, item.getId());
                    if (existingItem != null) {
                        // Actualizar item existente
                        existingItem.setQuantity(item.getQuantity());
                        existingItem.setItemPrice(item.getItemPrice());
                        existingItem.setOrder(managed);
                        if (item.getProduct() != null && item.getProduct().getId() != null) {
                            ProductJpaEntity managedProduct = entityManager.find(ProductJpaEntity.class, item.getProduct().getId());
                            existingItem.setProduct(managedProduct);
                        }
                        itemToAdd = existingItem;
                    } else {
                        // El ID no existe en BD, crear nuevo sin ID
                        itemToAdd = new OrderItemJpaEntity();
                        itemToAdd.setQuantity(item.getQuantity());
                        itemToAdd.setItemPrice(item.getItemPrice());
                        itemToAdd.setOrder(managed);
                        if (item.getProduct() != null && item.getProduct().getId() != null) {
                            ProductJpaEntity managedProduct = entityManager.find(ProductJpaEntity.class, item.getProduct().getId());
                            itemToAdd.setProduct(managedProduct);
                        }
                    }
                } else {
                    // Item nuevo sin ID
                    itemToAdd = new OrderItemJpaEntity();
                    itemToAdd.setQuantity(item.getQuantity());
                    itemToAdd.setItemPrice(item.getItemPrice());
                    itemToAdd.setOrder(managed);
                    if (item.getProduct() != null && item.getProduct().getId() != null) {
                        ProductJpaEntity managedProduct = entityManager.find(ProductJpaEntity.class, item.getProduct().getId());
                        itemToAdd.setProduct(managedProduct);
                    }
                }
                
                managed.getItems().add(itemToAdd);
            }
        }

        entityManager.flush();
        return managed;
    }

    @Override
    public void deleteById(Long id) {
        OrderJpaEntity entity = entityManager.find(OrderJpaEntity.class, id);
        if (entity != null) {
            entityManager.remove(entity);
        }
    }

    @Override
    public void deleteItemsByOrderId(Long orderId) {
        OrderJpaEntity order = entityManager.find(OrderJpaEntity.class, orderId);
        if (order != null) {
            order.getItems().clear();
            entityManager.flush();
        }
    }
}
