package com.foodservice.controller;

import com.foodservice.controller.CouponsController;
import com.foodservice.dto.Coupons;
import com.foodservice.entity.CouponsEntity;
import com.foodservice.exception.CouponNotFoundException;
import com.foodservice.services.CouponsService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CouponsControllerTest {

    // Custom Exception Handler
    @RestControllerAdvice
    static class TestExceptionHandler {

        @ExceptionHandler(CouponNotFoundException.class)
        public ResponseEntity<String> handleNotFound(CouponNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @Mock
    private CouponsService service;

    @InjectMocks
    private CouponsController controller;

    private MockMvc mockMvc;

    private CouponsEntity sampleEntity;
    private Coupons sampleDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new TestExceptionHandler())
                .build();

        sampleEntity = new CouponsEntity(
                1,
                "SAVE20",
                new BigDecimal("10"),
                LocalDate.of(2026, 12, 31)
        );

        sampleDto = new Coupons(
                1, "SAVE20",
                new BigDecimal("20"),
                LocalDate.of(2026, 12, 31)
        );
    }

    // ================= CREATE =================
    @Test
    void createCoupon_success() {

        doReturn(ResponseEntity.ok("Created"))
                .when(service)
                .createCoupon(any(Coupons.class));

        ResponseEntity<?> response =
                (ResponseEntity<?>) controller.create(sampleDto);

        org.junit.jupiter.api.Assertions.assertEquals(200, response.getStatusCode().value());
        org.junit.jupiter.api.Assertions.assertEquals("Created", response.getBody());

        verify(service).createCoupon(any(Coupons.class));
    }

    // ================= GET ALL =================
    @Test
    void getAll_success() throws Exception {
        when(service.getAllCoupons()).thenReturn(List.of(sampleEntity));

        mockMvc.perform(get("/api/coupons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(service).getAllCoupons();
    }

    // ================= GET BY ID =================
    @Test
    void getById_success() throws Exception {
        when(service.getCoupon(1)).thenReturn(sampleEntity);

        mockMvc.perform(get("/api/coupons/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.couponId").value(1));

        verify(service).getCoupon(1);
    }

    @Test
    void getById_notFound() throws Exception {
        when(service.getCoupon(1))
                .thenThrow(new CouponNotFoundException("Coupon not found"));

        mockMvc.perform(get("/api/coupons/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Coupon not found"));
    }

    // ================= BY CODE =================
    @Test
    void getByCode_success() throws Exception {
        when(service.getByCouponCode("SAVE10"))
                .thenReturn(List.of(sampleEntity));

        mockMvc.perform(get("/api/coupons/by-code")
                .param("code", "SAVE10"))
                .andExpect(status().isOk());

        verify(service).getByCouponCode("SAVE10");
    }

    // ================= BY DISCOUNT =================
    @Test
    void getByDiscount_success() throws Exception {
        when(service.getByDiscount(new BigDecimal("10")))
                .thenReturn(List.of(sampleEntity));

        mockMvc.perform(get("/api/coupons/by-discount")
                .param("discount", "10"))
                .andExpect(status().isOk());
    }

    // ================= BY EXPIRY =================
    @Test
    void getByExpiry_success() throws Exception {
        when(service.getByExpiry(LocalDate.of(2026, 12, 31)))
                .thenReturn(List.of(sampleEntity));

        mockMvc.perform(get("/api/coupons/by-expiry")
                .param("date", "2026-12-31"))
                .andExpect(status().isOk());
    }

    // ================= CODE + DISCOUNT =================
    @Test
    void getByCodeDiscount_success() throws Exception {
        when(service.getByCodeAndDiscount("SAVE10", new BigDecimal("10")))
                .thenReturn(List.of(sampleEntity));

        mockMvc.perform(get("/api/coupons/by-code-discount")
                .param("code", "SAVE10")
                .param("discount", "10"))
                .andExpect(status().isOk());
    }

    // ================= CODE + EXPIRY =================
    @Test
    void getByCodeExpiry_success() throws Exception {
        when(service.getByCodeAndExpiry("SAVE10", LocalDate.of(2026, 12, 31)))
                .thenReturn(List.of(sampleEntity));

        mockMvc.perform(get("/api/coupons/by-code-expiry")
                .param("code", "SAVE10")
                .param("date", "2026-12-31"))
                .andExpect(status().isOk());
    }

    // ================= DISCOUNT + EXPIRY =================
    @Test
    void getByDiscountExpiry_success() throws Exception {
        when(service.getByDiscountAndExpiry(new BigDecimal("10"),
                LocalDate.of(2026, 12, 31)))
                .thenReturn(List.of(sampleEntity));

        mockMvc.perform(get("/api/coupons/by-discount-expiry")
                .param("discount", "10")
                .param("date", "2026-12-31"))
                .andExpect(status().isOk());
    }

    // ================= STARTS WITH =================
    @Test
    void getByPrefix_success() throws Exception {
        when(service.getByCodeStartsWith("SA"))
                .thenReturn(List.of(sampleEntity));

        mockMvc.perform(get("/api/coupons/by-code-starts")
                .param("prefix", "SA"))
                .andExpect(status().isOk());
    }

    // ================= MIN DISCOUNT =================
    @Test
    void getMinDiscount_success() throws Exception {
        when(service.getByMinDiscount(new BigDecimal("5")))
                .thenReturn(List.of(sampleEntity));

        mockMvc.perform(get("/api/coupons/by-min-discount")
                .param("min", "5"))
                .andExpect(status().isOk());
    }

    // ================= MAX DISCOUNT =================
    @Test
    void getMaxDiscount_success() throws Exception {
        when(service.getByMaxDiscount(new BigDecimal("20")))
                .thenReturn(List.of(sampleEntity));

        mockMvc.perform(get("/api/coupons/by-max-discount")
                .param("max", "20"))
                .andExpect(status().isOk());
    }

    // ================= RANGE =================
    @Test
    void getRange_success() throws Exception {
        when(service.getByDiscountRange(new BigDecimal("5"), new BigDecimal("20")))
                .thenReturn(List.of(sampleEntity));

        mockMvc.perform(get("/api/coupons/by-discount-range")
                .param("min", "5")
                .param("max", "20"))
                .andExpect(status().isOk());
    }

    // ================= UPDATE DISCOUNT =================
    @Test
    void updateDiscount_success() {
        when(service.updateDiscount(1, new BigDecimal("20")))
                .thenReturn("Updated");

        String response = controller.updateDiscount(1, new BigDecimal("20"));

        org.junit.jupiter.api.Assertions.assertEquals("Updated", response);
        verify(service).updateDiscount(eq(1), any());
    }

    // ================= UPDATE EXPIRY =================
    @Test
    void updateExpiry_success() {
        when(service.updateExpiry(eq(1), any()))
                .thenReturn("Updated");

        String response = controller.updateExpiry(1, "2026-12-31");

        org.junit.jupiter.api.Assertions.assertEquals("Updated", response);
    }

    // ================= UPDATE BOTH =================
    @Test
    void updateCoupon_success() {
        when(service.updateCoupon(eq(1), any(), any()))
                .thenReturn("Updated");

        String response = controller.updateCoupon(1, new BigDecimal("20"), "2026-12-31");

        org.junit.jupiter.api.Assertions.assertEquals("Updated", response);
    }

    // ================= VALIDATE =================
    @Test
    void validate_success() throws Exception {
        when(service.validateCoupon(1)).thenReturn("Coupon is valid");

        mockMvc.perform(get("/api/coupons/validate/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Coupon is valid"));
    }

    // ================= ACTIVE =================
    @Test
    void getActive_success() throws Exception {
        when(service.getActiveCoupons()).thenReturn(List.of(sampleEntity));

        mockMvc.perform(get("/api/coupons/active"))
                .andExpect(status().isOk());
    }

    // ================= EXPIRED =================
    @Test
    void getExpired_success() throws Exception {
        when(service.getExpiredCoupons()).thenReturn(List.of(sampleEntity));

        mockMvc.perform(get("/api/coupons/expired"))
                .andExpect(status().isOk());
    }
}