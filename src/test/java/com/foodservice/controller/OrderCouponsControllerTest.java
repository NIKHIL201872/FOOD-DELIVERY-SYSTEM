package com.foodservice.controller;

import com.foodservice.services.OrderCouponsService;
import com.foodservice.controller.OrderCouponsController;
import com.foodservice.exception.CouponNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class OrderCouponsControllerTest {

    // ✅ Exception Handler (same pattern as before)
    @RestControllerAdvice
    static class TestExceptionHandler {

        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<String> handleError(RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @Mock
    private OrderCouponsService service;

    @InjectMocks
    private OrderCouponsController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new TestExceptionHandler())
                .build();
    }

    // ================= APPLY COUPON =================
    @Test
    void applyCoupon_success() {

        doReturn("Coupon Applied")
                .when(service)
                .applyCoupon(1, 10);

        String response = controller.applyCoupon(1, 10);

        org.junit.jupiter.api.Assertions.assertEquals("Coupon Applied", response);

        verify(service).applyCoupon(1, 10);
    }

    // ================= GET COUPONS BY ORDER =================
    @Test
    void getCoupons_success() throws Exception {

        doReturn(List.of("SAVE10", "DISC20"))
                .when(service)
                .getCouponsByOrder(1);

        mockMvc.perform(get("/api/ordercoupons/1/coupons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(service).getCouponsByOrder(1);
    }

    // ================= GET BY COUPON =================
    @Test
    void getByCoupon_success() throws Exception {

        doReturn(List.of("Order1", "Order2"))
                .when(service)
                .getByCouponId(10);

        mockMvc.perform(get("/api/ordercoupons/coupon/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(service).getByCouponId(10);
    }

    // ================= GET BY ORDER + COUPON =================
    @Test
    void getByOrderAndCoupon_success() throws Exception {

        doReturn(List.of("Match"))
                .when(service)
                .getByOrderAndCoupon(1, 10);

        mockMvc.perform(get("/api/ordercoupons/order/1/coupon/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(service).getByOrderAndCoupon(1, 10);
    }

    // ================= GET ALL =================
    @Test
    void getAll_success() throws Exception {

        doReturn(List.of("Data1", "Data2", "Data3"))
                .when(service)
                .getAll();

        mockMvc.perform(get("/api/ordercoupons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));

        verify(service).getAll();
    }

    // ================= EXCEPTION TEST =================
    @Test
    void getCoupons_notFound() throws Exception {

        doThrow(new RuntimeException("No coupons found"))
                .when(service)
                .getCouponsByOrder(99);

        mockMvc.perform(get("/api/ordercoupons/99/coupons"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No coupons found"));
    }
}