package com.foodservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "orderscoupons")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(OrderCouponsId.class)
public class OrderCouponsEntity {

    @Id
    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private OrdersEntity order;

    @Id
    @ManyToOne
    @JoinColumn(name = "coupon_id")
    @JsonIgnore
    private CouponsEntity coupon;
}