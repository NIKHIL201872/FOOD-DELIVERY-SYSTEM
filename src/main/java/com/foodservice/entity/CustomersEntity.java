package com.foodservice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer customerId;

    private String customerName;

    @Column(unique = true)
    private String customerEmail;

    private String customerPhone;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<DeliveryAddressesEntity> addresses;

    @OneToMany(mappedBy = "customer")
    private List<OrdersEntity> orders;
}