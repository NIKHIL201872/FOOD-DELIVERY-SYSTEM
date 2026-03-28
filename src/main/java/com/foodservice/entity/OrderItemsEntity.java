package com.foodservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "orderitems")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemsEntity {

    @Id
<<<<<<< Updated upstream
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
=======
    @GeneratedValue(strategy = GenerationType.IDENTITY)
>>>>>>> Stashed changes
    @Column(name = "order_item_id")
    private Integer orderItemId;

    @Column(name = "quantity")
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private OrdersEntity order;

    @ManyToOne
    @JoinColumn(name = "item_id")
    @JsonIgnore
<<<<<<< Updated upstream
    private MenuItemsEntity item;
=======
    private MenuItemsEntity menuItem;
>>>>>>> Stashed changes
}