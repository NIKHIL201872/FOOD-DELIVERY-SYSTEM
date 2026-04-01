package com.foodservice.services;

import com.foodservice.dto.Coupons;
import com.foodservice.entity.CouponsEntity;
import com.foodservice.exception.*;
import com.foodservice.repository.CouponsRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class CouponsService {

    @Autowired
    private CouponsRepository repository;

    @Autowired
    private ModelMapper mapper;

    public ResponseEntity<?> createCoupon(Coupons dto) {

        List<CouponsEntity> existing = repository.findByCouponCodeIgnoreCase(dto.getCouponCode());

        if (!existing.isEmpty()) {
            throw new DuplicateCouponException("Coupon already exists...");
        }

        Integer newId = repository.findAll().stream()
                .map(CouponsEntity::getCouponId)
                .max(Integer::compareTo)
                .orElse(0) + 1;

        // ModelMapper conversion
        CouponsEntity c = mapper.map(dto, CouponsEntity.class);
        c.setCouponId(newId);

        repository.save(c);

        return new ResponseEntity<>("Coupon Created Successfully", HttpStatus.CREATED);
    }


    //  GET ALL
    public List<CouponsEntity> getAllCoupons() {
        return repository.findAll();
    }

    //  GET BY ID
    public CouponsEntity getCoupon(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new CouponNotFoundException("Coupon not found"));
    }

    //  FILTERS (NO STREAMS NOW )

    public List<CouponsEntity> getByCouponCode(String code) {
        List<CouponsEntity> list = repository.findByCouponCodeIgnoreCase(code);
        if (list.isEmpty()) throw new CouponNotFoundException("Coupon not found");
        return list;
    }

    public List<CouponsEntity> getByDiscount(BigDecimal discount) {
        List<CouponsEntity> list = repository.findByDiscountAmount(discount);
        if (list.isEmpty()) throw new CouponNotFoundException("Coupon not found");
        return list;
    }

    public List<CouponsEntity> getByExpiry(LocalDate date) {
        List<CouponsEntity> list = repository.findByExpiryDate(date);
        if (list.isEmpty()) throw new CouponNotFoundException("Coupon not found");
        return list;
    }

    public List<CouponsEntity> getByCodeAndDiscount(String code, BigDecimal discount) {
        List<CouponsEntity> list =
                repository.findByCouponCodeIgnoreCaseAndDiscountAmount(code, discount);
        if (list.isEmpty()) throw new CouponNotFoundException("No matching coupons");
        return list;
    }

    public List<CouponsEntity> getByCodeAndExpiry(String code, LocalDate date) {
        List<CouponsEntity> list =
                repository.findByCouponCodeIgnoreCaseAndExpiryDate(code, date);
        if (list.isEmpty()) throw new CouponNotFoundException("No matching coupons");
        return list;
    }

    public List<CouponsEntity> getByDiscountAndExpiry(BigDecimal discount, LocalDate date) {
        List<CouponsEntity> list =
                repository.findByDiscountAmountAndExpiryDate(discount, date);
        if (list.isEmpty()) throw new CouponNotFoundException("No matching coupons");
        return list;
    }

    public List<CouponsEntity> getByCodeStartsWith(String prefix) {
        List<CouponsEntity> list =
                repository.findByCouponCodeStartingWithIgnoreCase(prefix);
        if (list.isEmpty()) throw new CouponNotFoundException("No coupons found");
        return list;
    }

    public List<CouponsEntity> getByMinDiscount(BigDecimal min) {
        List<CouponsEntity> list =
                repository.findByDiscountAmountGreaterThanEqual(min);
        if (list.isEmpty()) throw new CouponNotFoundException("No coupons found");
        return list;
    }

    public List<CouponsEntity> getByMaxDiscount(BigDecimal max) {
        List<CouponsEntity> list =
                repository.findByDiscountAmountLessThanEqual(max);
        if (list.isEmpty()) throw new CouponNotFoundException("No coupons found");
        return list;
    }

    public List<CouponsEntity> getByDiscountRange(BigDecimal min, BigDecimal max) {
        List<CouponsEntity> list =
                repository.findByDiscountAmountBetween(min, max);
        if (list.isEmpty()) throw new CouponNotFoundException("No coupons found");
        return list;
    }

    //  VALIDATION
    public String validateCoupon(Integer id) {

        CouponsEntity coupon = getCoupon(id);

        if (coupon.getExpiryDate() != null &&
                coupon.getExpiryDate().isBefore(LocalDate.now())) {
            throw new CouponExpiredException("Coupon expired");
        }

        return "Coupon is valid";
    }

    //  ACTIVE / EXPIRED (still using logic — fine)
    public List<CouponsEntity> getActiveCoupons() {
        return repository.findAll().stream()
                .filter(c -> c.getExpiryDate() == null ||
                        !c.getExpiryDate().isBefore(LocalDate.now()))
                .toList();
    }

    public List<CouponsEntity> getExpiredCoupons() {
        return repository.findAll().stream()
                .filter(c -> c.getExpiryDate() != null &&
                        c.getExpiryDate().isBefore(LocalDate.now()))
                .toList();
    }

    //  UPDATES
    public String updateDiscount(Integer id, BigDecimal discount) {

        CouponsEntity coupon = getCoupon(id);

        coupon.setDiscountAmount(discount);
        repository.save(coupon);

        return "Discount updated successfully";
    }

    public String updateExpiry(Integer id, LocalDate date) {

        CouponsEntity coupon = getCoupon(id);

        coupon.setExpiryDate(date);
        repository.save(coupon);

        return "Expiry date updated successfully";
    }

    public String updateCoupon(Integer id, BigDecimal discount, LocalDate date) {

        CouponsEntity coupon = getCoupon(id);

        coupon.setDiscountAmount(discount);
        coupon.setExpiryDate(date);

        repository.save(coupon);

        return "Coupon updated successfully";
    }
}