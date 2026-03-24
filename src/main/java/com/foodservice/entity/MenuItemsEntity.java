package com.foodservice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "menuitems")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer itemId;

    private String itemName;

    @Column(columnDefinition = "TEXT")
    private String itemDescription;

    private BigDecimal itemPrice;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private RestaurantsEntity restaurant;
}