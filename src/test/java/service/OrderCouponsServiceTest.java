package service;

import com.foodservice.DAO.CouponsDAO;
import com.foodservice.DAO.OrderCouponsDAO;
import com.foodservice.dto.Coupons;
import com.foodservice.dto.OrderCoupons;
import com.foodservice.entity.*;
import com.foodservice.exception.*;
import com.foodservice.services.OrderCouponsService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class OrderCouponsServiceTest {

    @Mock
    private OrderCouponsDAO orderCouponsDAO;

    @Mock
    private CouponsDAO couponsDAO;

    @InjectMocks
    private OrderCouponsService service;

    private OrdersEntity order;
    private CouponsEntity coupon;
    private OrderCouponsEntity orderCoupon;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        order = new OrdersEntity();
        order.setOrderId(1);

        coupon = new CouponsEntity(1, "SAVE10",
                new BigDecimal("10"),
                LocalDate.now().plusDays(5));

        orderCoupon = new OrderCouponsEntity(order, coupon);
    }

    // ================= APPLY COUPON =================
    @Test
    void applyCoupon_success() {

        when(orderCouponsDAO.findOrderById(1)).thenReturn(order);
        when(couponsDAO.findById(1)).thenReturn(coupon);
        when(orderCouponsDAO.findByOrderId(1)).thenReturn(Collections.emptyList());

        String result = service.applyCoupon(1, 1);

        assertEquals("Coupon Applied Successfully", result);
        verify(orderCouponsDAO).save(any(OrderCouponsEntity.class));
    }

    @Test
    void applyCoupon_orderNotFound() {

        when(orderCouponsDAO.findOrderById(1)).thenReturn(null);

        assertThrows(RuntimeException.class,
                () -> service.applyCoupon(1, 1));
    }

    @Test
    void applyCoupon_couponNotFound() {

        when(orderCouponsDAO.findOrderById(1)).thenReturn(order);
        when(couponsDAO.findById(1)).thenThrow(new RuntimeException());

        assertThrows(CouponNotFoundException.class,
                () -> service.applyCoupon(1, 1));
    }

    @Test
    void applyCoupon_expiredCoupon() {

        coupon.setExpiryDate(LocalDate.now().minusDays(1));

        when(orderCouponsDAO.findOrderById(1)).thenReturn(order);
        when(couponsDAO.findById(1)).thenReturn(coupon);

        assertThrows(CouponExpiredException.class,
                () -> service.applyCoupon(1, 1));
    }

    @Test
    void applyCoupon_alreadyApplied() {

        when(orderCouponsDAO.findOrderById(1)).thenReturn(order);
        when(couponsDAO.findById(1)).thenReturn(coupon);
        when(orderCouponsDAO.findByOrderId(1)).thenReturn(List.of(orderCoupon));

        assertThrows(RuntimeException.class,
                () -> service.applyCoupon(1, 1));
    }

    // ================= GET COUPONS BY ORDER =================
    @Test
    void getCouponsByOrder_success() {

        when(orderCouponsDAO.findByOrderId(1))
                .thenReturn(List.of(orderCoupon));

        List<Object> result = service.getCouponsByOrder(1);

        assertEquals(1, result.size());
    }

    @Test
    void getCouponsByOrder_notFound() {

        when(orderCouponsDAO.findByOrderId(1))
                .thenReturn(Collections.emptyList());

        assertThrows(CouponNotFoundException.class,
                () -> service.getCouponsByOrder(1));
    }

    // ================= GET BY COUPON =================
    @Test
    void getByCouponId_success() {

        when(orderCouponsDAO.findByCouponId(1))
                .thenReturn(List.of(orderCoupon));

        List<Object> result = service.getByCouponId(1);

        assertEquals(1, result.size());
    }

    @Test
    void getByCouponId_notFound() {

        when(orderCouponsDAO.findByCouponId(1))
                .thenReturn(Collections.emptyList());

        assertThrows(NoRecordsFoundException.class,
                () -> service.getByCouponId(1));
    }

    // ================= GET BY ORDER + COUPON =================
    @Test
    void getByOrderAndCoupon_success() {

        when(orderCouponsDAO.findByOrderId(1))
                .thenReturn(List.of(orderCoupon));

        List<Object> result = service.getByOrderAndCoupon(1, 1);

        assertEquals(1, result.size());
    }

    @Test
    void getByOrderAndCoupon_notFound() {

        when(orderCouponsDAO.findByOrderId(1))
                .thenReturn(Collections.emptyList());

        assertThrows(NoRecordsFoundException.class,
                () -> service.getByOrderAndCoupon(1, 1));
    }

    // ================= GET ALL =================
    @Test
    void getAll_success() {

        when(orderCouponsDAO.findAll())
                .thenReturn(List.of(orderCoupon));

        List<Object> result = service.getAll();

        assertEquals(1, result.size());
    }

    @Test
    void getAll_notFound() {

        when(orderCouponsDAO.findAll())
                .thenReturn(Collections.emptyList());

        assertThrows(NoRecordsFoundException.class,
                () -> service.getAll());
    }
}
