package com.foodservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.foodservice.entity.CouponsEntity;

public interface CouponsRepository extends JpaRepository<CouponsEntity, Integer> {
}