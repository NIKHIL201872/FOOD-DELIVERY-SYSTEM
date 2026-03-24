package com.foodservice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;

    private LocalDateTime orderDate;

    private String orderStatus;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private CustomersEntity customer;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private RestaurantsEntity restaurant;

    @ManyToOne
    @JoinColumn(name = "delivery_driver_id")
    private DeliveryDriversEntity deliveryDriver;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItemsEntity> orderItems;

    @OneToMany(mappedBy = "order")
    private List<OrderCouponsEntity> orderCoupons;
}