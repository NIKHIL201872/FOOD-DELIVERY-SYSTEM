package com.foodservice.controller;

import com.foodservice.dto.Customers;
import com.foodservice.exception.NoCustomerException;
import com.foodservice.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CustomerControllerTest {

    @RestControllerAdvice
    static class TestExceptionHandler {
        @ExceptionHandler(NoCustomerException.class)
        public ResponseEntity<String> handleNoCustomer(NoCustomerException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private MockMvc mockMvc;
    private Customers sampleCustomer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders
                .standaloneSetup(customerController)
                .setControllerAdvice(new TestExceptionHandler())
                .build();

        sampleCustomer = new Customers();
        sampleCustomer.setCustomerId(1);
        sampleCustomer.setCustomerName("John");
        sampleCustomer.setCustomerEmail("john@test.com");
        sampleCustomer.setCustomerPhone("9999999999");
    }

    @Test
    void registerCustomer_returnsSuccessMessage() {
        when(customerService.customerRegistration(any(Customers.class))).thenReturn(sampleCustomer);

        ResponseEntity<String> response = customerController.createCustomer(sampleCustomer);

        org.junit.jupiter.api.Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        org.junit.jupiter.api.Assertions.assertEquals("Customer added successfully", response.getBody());
        verify(customerService).customerRegistration(any(Customers.class));
    }

    @Test
    void updateCustomer_returnsSuccessMessage() {
        doNothing().when(customerService).updateCustomer(anyInt(), any(Customers.class));

        String response = customerController.updateCustomer(1, sampleCustomer);

        org.junit.jupiter.api.Assertions.assertEquals("Customer updated", response);
        verify(customerService).updateCustomer(eq(1), any(Customers.class));
    }

    @Test
    void getAllCustomers_returnsCustomerList() throws Exception {
        when(customerService.getAllCustomers()).thenReturn(List.of(sampleCustomer));

        mockMvc.perform(get("/customer/allcustomers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].customerId").value(1))
                .andExpect(jsonPath("$[0].customerEmail").value("john@test.com"));

        verify(customerService).getAllCustomers();
    }

    @Test
    void getCustomerById_returnsCustomer() throws Exception {
        when(customerService.getCustomerById(1)).thenReturn(sampleCustomer);

        mockMvc.perform(get("/customer/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.customerName").value("John"))
                .andExpect(jsonPath("$.customerPhone").value("9999999999"));

        verify(customerService).getCustomerById(1);
    }

    @Test
    void getByName_returnsCustomerList() throws Exception {
        when(customerService.getCustomerByName("John")).thenReturn(List.of(sampleCustomer));

        mockMvc.perform(get("/customer/by-name").param("name", "John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].customerName").value("John"));

        verify(customerService).getCustomerByName("John");
    }

    @Test
    void getByEmail_returnsCustomer() throws Exception {
        when(customerService.getCustomerByEmail("john@test.com")).thenReturn(sampleCustomer);

        mockMvc.perform(get("/customer/by-email").param("email", "john@test.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerEmail").value("john@test.com"));

        verify(customerService).getCustomerByEmail("john@test.com");
    }

    @Test
    void getByPhone_returnsCustomer() throws Exception {
        when(customerService.getCustomerByPhone("9999999999")).thenReturn(sampleCustomer);

        mockMvc.perform(get("/customer/by-phone").param("phone", "9999999999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerPhone").value("9999999999"));

        verify(customerService).getCustomerByPhone("9999999999");
    }

    @Test
    void getByNameAndEmail_returnsCustomerList() throws Exception {
        when(customerService.getByNameAndEmail("John", "john@test.com")).thenReturn(List.of(sampleCustomer));

        mockMvc.perform(get("/customer/by-name-email")
                        .param("name", "John")
                        .param("email", "john@test.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].customerEmail").value("john@test.com"));

        verify(customerService).getByNameAndEmail("John", "john@test.com");
    }

    @Test
    void getByNameAndPhone_returnsCustomerList() throws Exception {
        when(customerService.getByNameAndPhone("John", "9999999999")).thenReturn(List.of(sampleCustomer));

        mockMvc.perform(get("/customer/by-name-phone")
                        .param("name", "John")
                        .param("phone", "9999999999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].customerPhone").value("9999999999"));

        verify(customerService).getByNameAndPhone("John", "9999999999");
    }

    @Test
    void getByEmailAndPhone_returnsCustomer() throws Exception {
        when(customerService.getByEmailAndPhone("john@test.com", "9999999999")).thenReturn(sampleCustomer);

        mockMvc.perform(get("/customer/by-email-phone")
                        .param("email", "john@test.com")
                        .param("phone", "9999999999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerEmail").value("john@test.com"))
                .andExpect(jsonPath("$.customerPhone").value("9999999999"));

        verify(customerService).getByEmailAndPhone("john@test.com", "9999999999");
    }

    @Test
    void getByNameAndEmail_returnsNotFoundForNoCustomerException() throws Exception {
        when(customerService.getByNameAndEmail(eq("Unknown"), eq("x@test.com")))
                .thenThrow(new NoCustomerException("No customer found with name & email"));

        mockMvc.perform(get("/customer/by-name-email")
                        .param("name", "Unknown")
                        .param("email", "x@test.com"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No customer found with name & email"));

        verify(customerService).getByNameAndEmail("Unknown", "x@test.com");
    }
}

