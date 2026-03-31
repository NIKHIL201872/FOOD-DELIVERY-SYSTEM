package service;

import com.foodservice.dto.Coupons;
import com.foodservice.entity.CouponsEntity;
import com.foodservice.exception.*;
import com.foodservice.repository.CouponsRepository;
import com.foodservice.services.CouponsService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CouponsServiceTest {

    @Mock
    private CouponsRepository repository;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private CouponsService service;

    private Coupons dto;
    private CouponsEntity entity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        dto = new Coupons(1, "SAVE10", new BigDecimal("10"),
                LocalDate.of(2026, 12, 31));

        entity = new CouponsEntity(1, "SAVE10",
                new BigDecimal("10"),
                LocalDate.of(2026, 12, 31));
    }

    // ================= CREATE =================
    @Test
    void createCoupon_success() {

        when(repository.findByCouponCodeIgnoreCase(anyString()))
                .thenReturn(Collections.emptyList());

        when(repository.findAll())
                .thenReturn(Collections.emptyList());

        when(mapper.map(any(), eq(CouponsEntity.class)))
                .thenReturn(entity);

        ResponseEntity<?> response = service.createCoupon(dto);

        assertEquals(201, response.getStatusCode().value());
        assertEquals("Coupon Created Successfully", response.getBody());

        verify(repository).save(any(CouponsEntity.class));
    }

    @Test
    void createCoupon_duplicate() {

        when(repository.findByCouponCodeIgnoreCase(anyString()))
                .thenReturn(List.of(entity));

        assertThrows(DuplicateCouponException.class,
                () -> service.createCoupon(dto));
    }

    // ================= GET ALL =================
    @Test
    void getAllCoupons_success() {

        when(repository.findAll()).thenReturn(List.of(entity));

        List<CouponsEntity> result = service.getAllCoupons();

        assertEquals(1, result.size());
    }

    // ================= GET BY ID =================
    @Test
    void getCoupon_success() {

        when(repository.findById(1))
                .thenReturn(Optional.of(entity));

        CouponsEntity result = service.getCoupon(1);

        assertEquals("SAVE10", result.getCouponCode());
    }

    @Test
    void getCoupon_notFound() {

        when(repository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(CouponNotFoundException.class,
                () -> service.getCoupon(1));
    }

    // ================= FILTER =================
    @Test
    void getByCode_success() {
        when(repository.findByCouponCodeIgnoreCase(anyString()))
                .thenReturn(List.of(entity));

        assertEquals(1, service.getByCouponCode("SAVE10").size());
    }

    @Test
    void getByCode_notFound() {
        when(repository.findByCouponCodeIgnoreCase(anyString()))
                .thenReturn(Collections.emptyList());

        assertThrows(CouponNotFoundException.class,
                () -> service.getByCouponCode("SAVE10"));
    }

    @Test
    void getByDiscount_success() {
        when(repository.findByDiscountAmount(any()))
                .thenReturn(List.of(entity));

        assertEquals(1, service.getByDiscount(new BigDecimal("10")).size());
    }

    @Test
    void getByDiscount_notFound() {
        when(repository.findByDiscountAmount(any()))
                .thenReturn(Collections.emptyList());

        assertThrows(CouponNotFoundException.class,
                () -> service.getByDiscount(new BigDecimal("10")));
    }

    @Test
    void getByExpiry_success() {
        when(repository.findByExpiryDate(any()))
                .thenReturn(List.of(entity));

        assertEquals(1, service.getByExpiry(LocalDate.now()).size());
    }

    @Test
    void getByExpiry_notFound() {
        when(repository.findByExpiryDate(any()))
                .thenReturn(Collections.emptyList());

        assertThrows(CouponNotFoundException.class,
                () -> service.getByExpiry(LocalDate.now()));
    }

    @Test
    void getByCodeAndDiscount_success() {
        when(repository.findByCouponCodeIgnoreCaseAndDiscountAmount(anyString(), any()))
                .thenReturn(List.of(entity));

        assertEquals(1, service.getByCodeAndDiscount("SAVE10", new BigDecimal("10")).size());
    }

    @Test
    void getByCodeAndDiscount_notFound() {
        when(repository.findByCouponCodeIgnoreCaseAndDiscountAmount(anyString(), any()))
                .thenReturn(Collections.emptyList());

        assertThrows(CouponNotFoundException.class,
                () -> service.getByCodeAndDiscount("SAVE10", new BigDecimal("10")));
    }

    @Test
    void getByCodeAndExpiry_success() {
        when(repository.findByCouponCodeIgnoreCaseAndExpiryDate(anyString(), any()))
                .thenReturn(List.of(entity));

        assertEquals(1, service.getByCodeAndExpiry("SAVE10", LocalDate.now()).size());
    }

    @Test
    void getByDiscountAndExpiry_success() {
        when(repository.findByDiscountAmountAndExpiryDate(any(), any()))
                .thenReturn(List.of(entity));

        assertEquals(1, service.getByDiscountAndExpiry(new BigDecimal("10"), LocalDate.now()).size());
    }

    @Test
    void getByCodeStartsWith_success() {
        when(repository.findByCouponCodeStartingWithIgnoreCase(anyString()))
                .thenReturn(List.of(entity));

        assertEquals(1, service.getByCodeStartsWith("SA").size());
    }

    @Test
    void getByMinDiscount_success() {
        when(repository.findByDiscountAmountGreaterThanEqual(any()))
                .thenReturn(List.of(entity));

        assertEquals(1, service.getByMinDiscount(new BigDecimal("5")).size());
    }

    @Test
    void getByMaxDiscount_success() {
        when(repository.findByDiscountAmountLessThanEqual(any()))
                .thenReturn(List.of(entity));

        assertEquals(1, service.getByMaxDiscount(new BigDecimal("20")).size());
    }

    @Test
    void getByDiscountRange_success() {
        when(repository.findByDiscountAmountBetween(any(), any()))
                .thenReturn(List.of(entity));

        assertEquals(1, service.getByDiscountRange(new BigDecimal("5"), new BigDecimal("20")).size());
    }

    // ================= VALIDATE =================
    @Test
    void validateCoupon_valid() {

        when(repository.findById(1))
                .thenReturn(Optional.of(entity));

        assertEquals("Coupon is valid", service.validateCoupon(1));
    }

    @Test
    void validateCoupon_expired() {

        entity.setExpiryDate(LocalDate.now().minusDays(1));

        when(repository.findById(1))
                .thenReturn(Optional.of(entity));

        assertThrows(CouponExpiredException.class,
                () -> service.validateCoupon(1));
    }

    // ================= ACTIVE / EXPIRED =================
    @Test
    void getActiveCoupons() {

        CouponsEntity expired = new CouponsEntity(2, "OLD",
                new BigDecimal("5"),
                LocalDate.now().minusDays(1));

        when(repository.findAll())
                .thenReturn(List.of(entity, expired));

        assertEquals(1, service.getActiveCoupons().size());
    }

    @Test
    void getExpiredCoupons() {

        entity.setExpiryDate(LocalDate.now().minusDays(1));

        when(repository.findAll())
                .thenReturn(List.of(entity));

        assertEquals(1, service.getExpiredCoupons().size());
    }

    // ================= UPDATE =================
    @Test
    void updateDiscount_success() {

        when(repository.findById(1))
                .thenReturn(Optional.of(entity));

        assertEquals("Discount updated successfully",
                service.updateDiscount(1, new BigDecimal("20")));
    }

    @Test
    void updateExpiry_success() {

        when(repository.findById(1))
                .thenReturn(Optional.of(entity));

        assertEquals("Expiry date updated successfully",
                service.updateExpiry(1, LocalDate.now()));
    }

    @Test
    void updateCoupon_success() {

        when(repository.findById(1))
                .thenReturn(Optional.of(entity));

        assertEquals("Coupon updated successfully",
                service.updateCoupon(1, new BigDecimal("50"), LocalDate.now()));
    }
}