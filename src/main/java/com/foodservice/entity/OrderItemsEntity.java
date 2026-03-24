package com.foodservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "orderitems")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderItemId;

    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrdersEntity order;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private MenuItemsEntity item;
}