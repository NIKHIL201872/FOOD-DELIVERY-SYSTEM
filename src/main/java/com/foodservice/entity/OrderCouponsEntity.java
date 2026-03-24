package com.foodservice.entity;

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
    private OrdersEntity order;

    @Id
    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private CouponsEntity coupon;
}