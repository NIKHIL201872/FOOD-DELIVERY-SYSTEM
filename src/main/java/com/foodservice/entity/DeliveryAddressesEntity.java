package com.foodservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "deliveryaddresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryAddressesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer addressId;

    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postalCode;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonBackReference
    private CustomersEntity customer;
}