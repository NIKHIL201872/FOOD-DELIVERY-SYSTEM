package com.foodservice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "menuitems")
@Setter
@Getter

@NoArgsConstructor
@AllArgsConstructor
public class MenuItemsEntity {

    @Id
    private Integer itemId;

    private String itemName;

    @Column(columnDefinition = "TEXT")
    private String itemDescription;

    @Column(precision = 38, scale = 2)
    private BigDecimal itemPrice;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    @JsonBackReference
    private RestaurantsEntity restaurant;
}