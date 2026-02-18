package com.fpmislata.back.domain.mapper;

import com.fpmislata.back.domain.model.OrderItem;
import com.fpmislata.back.domain.repository.entity.OrderItemEntity;
import com.fpmislata.back.domain.service.dto.OrderItemDto;

public class OrderItemMapper {

    private static OrderItemMapper instance;
    private OrderItemMapper() {}

    public static OrderItemMapper getInstance() {
        if (instance == null) instance = new OrderItemMapper();
        return instance;
    }

    public OrderItem fromEntityToModel(OrderItemEntity entity) {
        if (entity == null) return null;
        return new OrderItem(
            entity.id(),
            ProductMapper.getInstance().fromProductEntityToProduct(entity.productEntity()),
            entity.quantity(),
            entity.itemPrice()
        );
    }

    public OrderItem fromDtoToModel(OrderItemDto dto) {
        if (dto == null) return null;
        return new OrderItem(
            dto.id(),
            ProductMapper.getInstance().fromProductDtoToProduct(dto.productDto()),
            dto.quantity(),
            dto.itemPrice()
        );
    }

    public OrderItemDto fromModelToDto(OrderItem item) {
        if (item == null) return null;
        return new OrderItemDto(
            item.getId(),
            ProductMapper.getInstance().fromProductToProductDto(item.getProduct()),
            item.getQuantity(),
            item.getItemPrice()
        );
    }
    
    public OrderItemEntity fromModelToEntity(OrderItem item) {
        if (item == null) return null;
        return new OrderItemEntity(
            item.getId(),
            ProductMapper.getInstance().fromProductToProductEntity(item.getProduct()),
            item.getQuantity(),
            item.getItemPrice()
        );
    }
}