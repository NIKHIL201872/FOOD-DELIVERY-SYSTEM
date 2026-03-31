package com.foodservice.services;

import com.foodservice.DAO.AddressDAO;
import com.foodservice.DAO.CustomerDAO;
import com.foodservice.dto.DeliveryAddresses;
import com.foodservice.entity.CustomersEntity;
import com.foodservice.entity.DeliveryAddressesEntity;
import com.foodservice.exception.NoAddressException;
import com.foodservice.exception.NoCustomerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AddressServiceTest {

    @Mock
    private AddressDAO addressDAO;

    @Mock
    private CustomerDAO customerDAO;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private AddressService addressService;

    private CustomersEntity customerEntity;
    private DeliveryAddressesEntity addressEntity;
    private DeliveryAddresses addressDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customerEntity = new CustomersEntity();
        customerEntity.setCustomerId(1);

        addressEntity = new DeliveryAddressesEntity();
        addressEntity.setAddressId(10);
        addressEntity.setAddressLine1("Line1");
        addressEntity.setAddressLine2("Line2");
        addressEntity.setCity("Kolkata");
        addressEntity.setState("West Bengal");
        addressEntity.setPostalCode("700016");
        addressEntity.setCustomer(customerEntity);

        addressDto = new DeliveryAddresses(
                10,
                1,
                "Line1",
                "Line2",
                "Kolkata",
                "West Bengal",
                "700016"
        );
    }

    @Test
    void addAddress_savesWhenCustomerExists() {
        DeliveryAddressesEntity input = new DeliveryAddressesEntity();
        input.setCustomer(customerEntity);

        when(customerDAO.getCustomerById(1)).thenReturn(customerEntity);
        doNothing().when(addressDAO).saveAddress(any(DeliveryAddressesEntity.class));

        addressService.addAddress(input);

        verify(customerDAO).getCustomerById(1);
        verify(addressDAO).saveAddress(input);
    }

    @Test
    void addAddress_throwsWhenCustomerMissing() {
        DeliveryAddressesEntity input = new DeliveryAddressesEntity();
        input.setCustomer(customerEntity);

        when(customerDAO.getCustomerById(1)).thenReturn(null);

        NoCustomerException ex = assertThrows(
                NoCustomerException.class,
                () -> addressService.addAddress(input)
        );

        assertEquals("Customer not found with ID: 1", ex.getMessage());
        verify(customerDAO).getCustomerById(1);
    }

    @Test
    void getById_returnsMappedDto() {
        when(addressDAO.getById(10)).thenReturn(addressEntity);
        when(mapper.map(addressEntity, DeliveryAddresses.class)).thenReturn(addressDto);

        DeliveryAddresses result = addressService.getById(10);

        assertNotNull(result);
        assertEquals(10, result.getAddressId());
        verify(addressDAO).getById(10);
        verify(mapper).map(addressEntity, DeliveryAddresses.class);
    }

    @Test
    void getById_throwsWhenNotFound() {
        when(addressDAO.getById(99)).thenReturn(null);

        NoAddressException ex = assertThrows(
                NoAddressException.class,
                () -> addressService.getById(99)
        );

        assertEquals("Address not found with id: 99", ex.getMessage());
        verify(addressDAO).getById(99);
    }

    @Test
    void getByCity_returnsMappedList() {
        when(addressDAO.getByCity("Kolkata")).thenReturn(List.of(addressEntity));
        when(mapper.map(addressEntity, DeliveryAddresses.class)).thenReturn(addressDto);

        List<DeliveryAddresses> result = addressService.getByCity("Kolkata");

        assertEquals(1, result.size());
        assertEquals("Kolkata", result.get(0).getCity());
        verify(addressDAO).getByCity("Kolkata");
        verify(mapper).map(addressEntity, DeliveryAddresses.class);
    }

    @Test
    void getByCity_throwsWhenEmpty() {
        when(addressDAO.getByCity("Unknown")).thenReturn(List.of());

        NoAddressException ex = assertThrows(
                NoAddressException.class,
                () -> addressService.getByCity("Unknown")
        );

        assertEquals("No addresses found in city: Unknown", ex.getMessage());
        verify(addressDAO).getByCity("Unknown");
    }

    @Test
    void getByState_returnsMappedList() {
        when(addressDAO.getByState("West Bengal")).thenReturn(List.of(addressEntity));
        when(mapper.map(addressEntity, DeliveryAddresses.class)).thenReturn(addressDto);

        List<DeliveryAddresses> result = addressService.getByState("West Bengal");

        assertEquals(1, result.size());
        assertEquals("West Bengal", result.get(0).getState());
        verify(addressDAO).getByState("West Bengal");
        verify(mapper).map(addressEntity, DeliveryAddresses.class);
    }

    @Test
    void getByState_throwsWhenEmpty() {
        when(addressDAO.getByState("Unknown")).thenReturn(List.of());

        NoAddressException ex = assertThrows(
                NoAddressException.class,
                () -> addressService.getByState("Unknown")
        );

        assertEquals("No addresses found in state: Unknown", ex.getMessage());
        verify(addressDAO).getByState("Unknown");
    }

    @Test
    void getByPostalCode_returnsMappedList() {
        when(addressDAO.getByPostalCode("700016")).thenReturn(List.of(addressEntity));
        when(mapper.map(addressEntity, DeliveryAddresses.class)).thenReturn(addressDto);

        List<DeliveryAddresses> result = addressService.getByPostalCode("700016");

        assertEquals(1, result.size());
        assertEquals("700016", result.get(0).getPostalCode());
        verify(addressDAO).getByPostalCode("700016");
        verify(mapper).map(addressEntity, DeliveryAddresses.class);
    }

    @Test
    void getByPostalCode_throwsWhenEmpty() {
        when(addressDAO.getByPostalCode("000000")).thenReturn(List.of());

        NoAddressException ex = assertThrows(
                NoAddressException.class,
                () -> addressService.getByPostalCode("000000")
        );

        assertEquals("No addresses found with postal code: 000000", ex.getMessage());
        verify(addressDAO).getByPostalCode("000000");
    }

    @Test
    void getByCityAndState_returnsMappedList() {
        when(addressDAO.getByCityAndState("Kolkata", "West Bengal"))
                .thenReturn(List.of(addressEntity));
        when(mapper.map(addressEntity, DeliveryAddresses.class)).thenReturn(addressDto);

        List<DeliveryAddresses> result =
                addressService.getByCityAndState("Kolkata", "West Bengal");

        assertEquals(1, result.size());
        verify(addressDAO).getByCityAndState("Kolkata", "West Bengal");
        verify(mapper).map(addressEntity, DeliveryAddresses.class);
    }

    @Test
    void getByCityAndState_throwsWhenEmpty() {
        when(addressDAO.getByCityAndState("Unknown", "WB")).thenReturn(List.of());

        NoAddressException ex = assertThrows(
                NoAddressException.class,
                () -> addressService.getByCityAndState("Unknown", "WB")
        );

        assertEquals("No address found for city & state", ex.getMessage());
        verify(addressDAO).getByCityAndState("Unknown", "WB");
    }

    @Test
    void getByCityAndPostal_returnsMappedList() {
        when(addressDAO.getByCityAndPostal("Kolkata", "700016"))
                .thenReturn(List.of(addressEntity));
        when(mapper.map(addressEntity, DeliveryAddresses.class)).thenReturn(addressDto);

        List<DeliveryAddresses> result =
                addressService.getByCityAndPostal("Kolkata", "700016");

        assertEquals(1, result.size());
        verify(addressDAO).getByCityAndPostal("Kolkata", "700016");
        verify(mapper).map(addressEntity, DeliveryAddresses.class);
    }

    @Test
    void getByCityAndPostal_throwsWhenEmpty() {
        when(addressDAO.getByCityAndPostal("Unknown", "000000"))
                .thenReturn(List.of());

        NoAddressException ex = assertThrows(
                NoAddressException.class,
                () -> addressService.getByCityAndPostal("Unknown", "000000")
        );

        assertEquals("No address found for city & postalCode", ex.getMessage());
        verify(addressDAO).getByCityAndPostal("Unknown", "000000");
    }

    @Test
    void getByStateAndPostal_returnsMappedList() {
        when(addressDAO.getByStateAndPostal("West Bengal", "700016"))
                .thenReturn(List.of(addressEntity));
        when(mapper.map(addressEntity, DeliveryAddresses.class)).thenReturn(addressDto);

        List<DeliveryAddresses> result =
                addressService.getByStateAndPostal("West Bengal", "700016");

        assertEquals(1, result.size());
        verify(addressDAO).getByStateAndPostal("West Bengal", "700016");
        verify(mapper).map(addressEntity, DeliveryAddresses.class);
    }

    @Test
    void getByStateAndPostal_throwsWhenEmpty() {
        when(addressDAO.getByStateAndPostal("Unknown", "000000"))
                .thenReturn(List.of());

        NoAddressException ex = assertThrows(
                NoAddressException.class,
                () -> addressService.getByStateAndPostal("Unknown", "000000")
        );

        assertEquals("No address found for state & postalCode", ex.getMessage());
        verify(addressDAO).getByStateAndPostal("Unknown", "000000");
    }

    @Test
    void getAddressesByCustomerId_returnsMappedList() {
        when(addressDAO.getAddressesByCustomerId(1)).thenReturn(List.of(addressEntity));
        when(mapper.map(addressEntity, DeliveryAddresses.class)).thenReturn(addressDto);

        List<DeliveryAddresses> result = addressService.getAddressesByCustomerId(1);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getCustomerId());
        verify(addressDAO).getAddressesByCustomerId(1);
        verify(mapper).map(addressEntity, DeliveryAddresses.class);
    }

    @Test
    void getAddressesByCustomerId_throwsWhenEmpty() {
        when(addressDAO.getAddressesByCustomerId(99)).thenReturn(List.of());

        NoAddressException ex = assertThrows(
                NoAddressException.class,
                () -> addressService.getAddressesByCustomerId(99)
        );

        assertEquals("No addresses found for customer id: 99", ex.getMessage());
        verify(addressDAO).getAddressesByCustomerId(99);
    }

    @Test
    void updateAddress_updatesWhenExists() {
        DeliveryAddressesEntity input = new DeliveryAddressesEntity();
        input.setAddressLine1("New1");
        input.setAddressLine2("New2");
        input.setCity("NewCity");
        input.setState("NewState");
        input.setPostalCode("123456");

        when(addressDAO.getAddressById(10)).thenReturn(addressEntity);
        doNothing().when(addressDAO).updateAddress(any(DeliveryAddressesEntity.class));

        addressService.updateAddress(10, input);

        verify(addressDAO).getAddressById(10);
        verify(addressDAO).updateAddress(addressEntity);
        assertEquals("New1", addressEntity.getAddressLine1());
        assertEquals("NewCity", addressEntity.getCity());
    }

    @Test
    void updateAddress_throwsWhenNotFound() {
        when(addressDAO.getAddressById(anyInt())).thenReturn(null);

        NoAddressException ex = assertThrows(
                NoAddressException.class,
                () -> addressService.updateAddress(99, addressEntity)
        );

        assertEquals("Address not found with id: 99", ex.getMessage());
        verify(addressDAO).getAddressById(99);
        verify(addressDAO, times(0)).updateAddress(any(DeliveryAddressesEntity.class));
    }
}

