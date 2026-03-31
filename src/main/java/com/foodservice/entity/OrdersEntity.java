package com.foodservice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdersEntity {

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "order_status")
    private String orderStatus;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    private CustomersEntity customer;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    @JsonIgnore
    private RestaurantsEntity restaurant;

    @ManyToOne
    @JoinColumn(name = "delivery_driver_id")
    @JsonIgnore
    private DeliveryDriversEntity deliveryDriver;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<OrderItemsEntity> orderItems;

    @OneToMany(mappedBy = "order")
    @JsonIgnore
    private List<OrderCouponsEntity> orderCoupons;
}