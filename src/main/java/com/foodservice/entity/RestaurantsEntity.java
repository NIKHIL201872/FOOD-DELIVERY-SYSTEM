package com.foodservice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "restaurants")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer restaurantId;

    private String restaurantName;
    private String restaurantAddress;
    private String restaurantPhone;

    
    @OneToMany(mappedBy = "restaurant")
    @JsonIgnore
    private List<MenuItemsEntity> menuItems;

    @OneToMany(mappedBy = "restaurant")
    @JsonBackReference
    private List<OrdersEntity> orders;
}