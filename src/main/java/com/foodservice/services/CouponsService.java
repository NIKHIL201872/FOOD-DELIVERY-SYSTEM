package com.foodservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.foodservice.DAO.CouponsDAO;
import com.foodservice.DAO.OrderCouponsDAO;
import com.foodservice.dto.Coupons;
import com.foodservice.entity.CouponsEntity;
import com.foodservice.exception.CouponExpiredException;
import com.foodservice.exception.CouponNotFoundException;
import com.foodservice.exception.DuplicateCouponException;

import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class CouponsService {

    @Autowired
    private CouponsDAO dao;
    
    public ResponseEntity<?> createCoupon(Coupons dto) {

        List<CouponsEntity> allCoupons = dao.findAll();

        long count = allCoupons.stream()
                .filter(c -> c.getCouponCode().equalsIgnoreCase(dto.getCouponCode()))
                .count();

        if (count > 0) {
            throw new DuplicateCouponException("Coupon already exists...");
        }

        Integer newId = allCoupons.stream()
                .map(CouponsEntity::getCouponId)
                .max(Integer::compareTo)
                .orElse(0) + 1;

        CouponsEntity c = new CouponsEntity();
        c.setCouponId(newId); 
        c.setCouponCode(dto.getCouponCode());
        c.setDiscountAmount(dto.getDiscountAmount());
        c.setExpiryDate(dto.getExpiryDate());

        dao.save(c);

        return new ResponseEntity<>("Coupon Created Successfully", HttpStatus.CREATED);
    }

    public List<CouponsEntity> getAllCoupons() {
        return dao.findAll();
    }

    public CouponsEntity getCoupon(Integer id) {
        try {
            return dao.findById(id);
        } catch (Exception e) {
            throw new CouponNotFoundException("Coupon not found");
        }
    }
    
    public List<CouponsEntity> getByCouponCode(String code) {
        List<CouponsEntity> list = dao.findAll()
                .stream()
                .filter(c -> c.getCouponCode().equalsIgnoreCase(code))
                .toList();

        if (list.isEmpty())
            throw new CouponNotFoundException("Coupon not found");

        return list;
    }

    public List<CouponsEntity> getByDiscount(BigDecimal discount) {
        List<CouponsEntity> list = dao.findAll()
                .stream()
                .filter(c -> c.getDiscountAmount().compareTo(discount) == 0)
                .toList();

        if (list.isEmpty())
            throw new CouponNotFoundException("Coupon not found");

        return list;
    }
    
    public List<CouponsEntity> getByExpiry(LocalDate date) {
        List<CouponsEntity> list = dao.findAll()
                .stream()
                .filter(c -> date.equals(c.getExpiryDate()))
                .toList();

        if (list.isEmpty())
            throw new CouponNotFoundException("Coupon not found");

        return list;
    }
    
    public List<CouponsEntity> getByCodeAndDiscount(String code, BigDecimal discount) {
        List<CouponsEntity> list = dao.findAll()
                .stream()
                .filter(c -> c.getCouponCode().equalsIgnoreCase(code)
                        && c.getDiscountAmount().compareTo(discount) == 0)
                .toList();

        if (list.isEmpty())
            throw new CouponNotFoundException("No matching coupons");

        return list;
    }

    public List<CouponsEntity> getByCodeAndExpiry(String code, LocalDate date) {
        List<CouponsEntity> list = dao.findAll()
                .stream()
                .filter(c -> c.getCouponCode().equalsIgnoreCase(code)
                        && date.equals(c.getExpiryDate()))
                .toList();

        if (list.isEmpty())
            throw new CouponNotFoundException("No matching coupons");

        return list;
    }
    
    public List<CouponsEntity> getByDiscountAndExpiry(BigDecimal discount, LocalDate date) {
        List<CouponsEntity> list = dao.findAll()
                .stream()
                .filter(c -> c.getDiscountAmount().compareTo(discount) == 0
                        && date.equals(c.getExpiryDate()))
                .toList();

        if (list.isEmpty())
            throw new CouponNotFoundException("No matching coupons");

        return list;
    }

    public List<CouponsEntity> getByAll(String code, BigDecimal discount, LocalDate date) {
        List<CouponsEntity> list = dao.findAll()
                .stream()
                .filter(c -> c.getCouponCode().equalsIgnoreCase(code)
                        && c.getDiscountAmount().compareTo(discount) == 0
                        && date.equals(c.getExpiryDate()))
                .toList();

        if (list.isEmpty())
            throw new CouponNotFoundException("No matching coupons");

        return list;
    }
    
    public List<CouponsEntity> getByCodeStartsWith(String prefix) {

        List<CouponsEntity> list = dao.findAll()
                .stream()
                .filter(c -> c.getCouponCode().toLowerCase().startsWith(prefix.toLowerCase()))
                .toList();

        if (list.isEmpty())
            throw new CouponNotFoundException("No coupons found starting with: " + prefix);

        return list;
    }
    
    public List<CouponsEntity> getByMinDiscount(BigDecimal min) {

        List<CouponsEntity> list = dao.findAll()
                .stream()
                .filter(c -> c.getDiscountAmount().compareTo(min) >= 0)
                .toList();

        if (list.isEmpty())
            throw new CouponNotFoundException("No coupons found with discount >= " + min);

        return list;
    }
    
    public List<CouponsEntity> getByMaxDiscount(BigDecimal max) {

        List<CouponsEntity> list = dao.findAll()
                .stream()
                .filter(c -> c.getDiscountAmount().compareTo(max) <= 0)
                .toList();

        if (list.isEmpty())
            throw new CouponNotFoundException("No coupons found with discount <= " + max);

        return list;
    }
    
    public List<CouponsEntity> getByDiscountRange(BigDecimal min, BigDecimal max) {

        List<CouponsEntity> list = dao.findAll()
                .stream()
                .filter(c -> c.getDiscountAmount().compareTo(min) >= 0 &&
                             c.getDiscountAmount().compareTo(max) <= 0)
                .toList();

        if (list.isEmpty())
            throw new CouponNotFoundException("No coupons found in given range");

        return list;
    }

    public String validateCoupon(Integer id) {

        CouponsEntity coupon = dao.findById(id);

        if (coupon.getExpiryDate() != null &&
                coupon.getExpiryDate().isBefore(LocalDate.now())) {
            throw new CouponExpiredException("Coupon expired");
        }

        return "Coupon is valid";
    }
    public List<CouponsEntity> getActiveCoupons() {

        return dao.findAll().stream()
                .filter(c -> c.getExpiryDate() == null ||
                        !c.getExpiryDate().isBefore(LocalDate.now()))
                .toList();
    }

    public List<CouponsEntity> getExpiredCoupons() {

        return dao.findAll().stream()
                .filter(c -> c.getExpiryDate() != null &&
                        c.getExpiryDate().isBefore(LocalDate.now()))
                .toList();
    }
    
    public String updateDiscount(Integer id, BigDecimal discount) {

        CouponsEntity coupon = dao.findById(id);

        if (coupon == null) {
            throw new CouponNotFoundException("Coupon not found");
        }

        coupon.setDiscountAmount(discount);
        dao.save(coupon);

        return "Discount updated successfully";
    }
    
    public String updateExpiry(Integer id, LocalDate date) {

        CouponsEntity coupon = dao.findById(id);

        if (coupon == null) {
            throw new CouponNotFoundException("Coupon not found");
        }

        coupon.setExpiryDate(date);
        dao.save(coupon);

        return "Expiry date updated successfully";
    }
    
    public String updateCoupon(Integer id, BigDecimal discount, LocalDate date) {

        CouponsEntity coupon = dao.findById(id);

        if (coupon == null) {
            throw new CouponNotFoundException("Coupon not found");
        }

        coupon.setDiscountAmount(discount);
        coupon.setExpiryDate(date);

        dao.save(coupon);

        return "Coupon updated successfully";
    }
    
}