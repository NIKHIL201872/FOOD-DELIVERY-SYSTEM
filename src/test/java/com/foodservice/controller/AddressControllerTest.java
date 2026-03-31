package com.foodservice.controller;

import com.foodservice.dto.DeliveryAddresses;
import com.foodservice.entity.CustomersEntity;
import com.foodservice.entity.DeliveryAddressesEntity;
import com.foodservice.exception.NoAddressException;
import com.foodservice.services.AddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AddressControllerTest {

    @RestControllerAdvice
    static class TestExceptionHandler {
        @ExceptionHandler(NoAddressException.class)
        public ResponseEntity<String> handleNoAddress(NoAddressException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @Mock
    private AddressService addressService;

    @InjectMocks
    private AddressController addressController;

    private MockMvc mockMvc;
    private DeliveryAddressesEntity sampleEntity;
    private DeliveryAddresses sampleDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(addressController)
                .setControllerAdvice(new TestExceptionHandler())
                .build();

        CustomersEntity customer = new CustomersEntity();
        customer.setCustomerId(1);

        sampleEntity = new DeliveryAddressesEntity();
        sampleEntity.setAddressId(1);
        sampleEntity.setAddressLine1("12 Park Street");
        sampleEntity.setAddressLine2("Apt 3B");
        sampleEntity.setCity("Kolkata");
        sampleEntity.setState("West Bengal");
        sampleEntity.setPostalCode("700016");
        sampleEntity.setCustomer(customer);

        sampleDto = new DeliveryAddresses(
                1,
                1,
                "12 Park Street",
                "Apt 3B",
                "Kolkata",
                "West Bengal",
                "700016"
        );
    }

    @Test
    void addAddress_returnsSuccessMessage() {
        doNothing().when(addressService).addAddress(any(DeliveryAddressesEntity.class));
        String response = addressController.addAddress(sampleEntity);

        org.junit.jupiter.api.Assertions.assertEquals("Address added", response);
        verify(addressService).addAddress(any(DeliveryAddressesEntity.class));
    }

    @Test
    void getById_returnsAddress() throws Exception {
        when(addressService.getById(1)).thenReturn(sampleDto);

        mockMvc.perform(get("/address/by-id").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.addressId").value(1))
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.city").value("Kolkata"));

        verify(addressService).getById(1);
    }

    @Test
    void getByCity_returnsAddressList() throws Exception {
        when(addressService.getByCity("Kolkata")).thenReturn(List.of(sampleDto));

        mockMvc.perform(get("/address/by-city").param("city", "Kolkata"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].city").value("Kolkata"));

        verify(addressService).getByCity("Kolkata");
    }

    @Test
    void getByState_returnsAddressList() throws Exception {
        when(addressService.getByState("West Bengal")).thenReturn(List.of(sampleDto));

        mockMvc.perform(get("/address/by-state").param("state", "West Bengal"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].state").value("West Bengal"));

        verify(addressService).getByState("West Bengal");
    }

    @Test
    void getByPostalCode_returnsAddressList() throws Exception {
        when(addressService.getByPostalCode("700016")).thenReturn(List.of(sampleDto));

        mockMvc.perform(get("/address/by-postal").param("postalCode", "700016"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].postalCode").value("700016"));

        verify(addressService).getByPostalCode("700016");
    }

    @Test
    void getByCityAndState_returnsAddressList() throws Exception {
        when(addressService.getByCityAndState("Kolkata", "West Bengal"))
                .thenReturn(List.of(sampleDto));

        mockMvc.perform(get("/address/by-city-state")
                        .param("city", "Kolkata")
                        .param("state", "West Bengal"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(addressService).getByCityAndState("Kolkata", "West Bengal");
    }

    @Test
    void getByCityAndState_returnsNotFoundForNoAddressException() throws Exception {
        when(addressService.getByCityAndState("Unknown", "West Bengal"))
                .thenThrow(new NoAddressException("No address found"));

        mockMvc.perform(get("/address/by-city-state")
                        .param("city", "Unknown")
                        .param("state", "West Bengal"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No address found"));
    }

    @Test
    void getByCityAndPostal_returnsAddressList() throws Exception {
        when(addressService.getByCityAndPostal("Kolkata", "700016"))
                .thenReturn(List.of(sampleDto));

        mockMvc.perform(get("/address/by-city-postal")
                        .param("city", "Kolkata")
                        .param("postalCode", "700016"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(addressService).getByCityAndPostal("Kolkata", "700016");
    }

    @Test
    void getByStateAndPostal_returnsAddressList() throws Exception {
        when(addressService.getByStateAndPostal("West Bengal", "700016"))
                .thenReturn(List.of(sampleDto));

        mockMvc.perform(get("/address/by-state-postal")
                        .param("state", "West Bengal")
                        .param("postalCode", "700016"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(addressService).getByStateAndPostal("West Bengal", "700016");
    }

    @Test
    void getCustomerAddresses_returnsAddressList() throws Exception {
        when(addressService.getAddressesByCustomerId(1)).thenReturn(List.of(sampleDto));

        mockMvc.perform(get("/address/customer/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].customerId").value(1));

        verify(addressService).getAddressesByCustomerId(1);
    }

    @Test
    void updateAddress_returnsSuccessMessage() {
        doNothing().when(addressService).updateAddress(anyInt(), any(DeliveryAddressesEntity.class));
        String response = addressController.updateAddress(1, sampleEntity);

        org.junit.jupiter.api.Assertions.assertEquals("Address updated", response);
        verify(addressService).updateAddress(eq(1), any(DeliveryAddressesEntity.class));
    }
}