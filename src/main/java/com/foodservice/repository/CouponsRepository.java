package com.foodservice.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.foodservice.entity.CouponsEntity;

public interface CouponsRepository extends JpaRepository<CouponsEntity, Integer> {
	List<CouponsEntity> findByCouponCodeIgnoreCase(String code);

    List<CouponsEntity> findByDiscountAmount(BigDecimal discount);

    List<CouponsEntity> findByExpiryDate(LocalDate date);

    List<CouponsEntity> findByCouponCodeIgnoreCaseAndDiscountAmount(String code, BigDecimal discount);

    List<CouponsEntity> findByCouponCodeIgnoreCaseAndExpiryDate(String code, LocalDate date);

    List<CouponsEntity> findByDiscountAmountAndExpiryDate(BigDecimal discount, LocalDate date);

    List<CouponsEntity> findByCouponCodeStartingWithIgnoreCase(String prefix);

    List<CouponsEntity> findByDiscountAmountGreaterThanEqual(BigDecimal min);

    List<CouponsEntity> findByDiscountAmountLessThanEqual(BigDecimal max);

    List<CouponsEntity> findByDiscountAmountBetween(BigDecimal min, BigDecimal max);
}