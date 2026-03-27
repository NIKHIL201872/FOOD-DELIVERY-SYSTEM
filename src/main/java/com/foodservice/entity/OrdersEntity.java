package com.foodservice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
    @JsonBackReference
    private CustomersEntity customer;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    @JsonBackReference
    private RestaurantsEntity restaurant;

    @ManyToOne
    @JoinColumn(name = "delivery_driver_id")
    @JsonBackReference
    private DeliveryDriversEntity deliveryDriver;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItemsEntity> orderItems;

    @OneToMany(mappedBy = "order")
    private List<OrderCouponsEntity> orderCoupons;
}