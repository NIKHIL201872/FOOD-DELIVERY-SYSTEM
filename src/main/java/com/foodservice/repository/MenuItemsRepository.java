package com.foodservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foodservice.entity.MenuItemsEntity;

public interface MenuItemsRepository extends JpaRepository<MenuItemsEntity, Integer>{

}
