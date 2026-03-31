package com.foodservice.services;

import com.foodservice.DAO.CustomerDAO;
import com.foodservice.dto.Customers;
import com.foodservice.exception.DuplicateCustomerException;
import com.foodservice.entity.CustomersEntity;
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

class CustomerServiceTest {

    @Mock
    private CustomerDAO customerDAO;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private CustomerService customerService;

    private CustomersEntity customerEntity;
    private Customers customerDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customerEntity = new CustomersEntity();
        customerEntity.setCustomerId(1);
        customerEntity.setCustomerName("John");
        customerEntity.setCustomerEmail("john@test.com");
        customerEntity.setCustomerPhone("9999999999");

        customerDto = new Customers(1, "John", "john@test.com", "9999999999");
    }

    @Test
    void customerRegistration_setsNextIdAndReturnsDto() {
        Customers input = new Customers(null, "John", "john@test.com", "9999999999");

        when(customerDAO.checkDuplicate("john@test.com", "9999999999")).thenReturn(List.of());
        when(customerDAO.findMaxCustomerId()).thenReturn(10);
        when(customerDAO.insertCustomerDetails(any(CustomersEntity.class))).thenReturn(customerEntity);
        when(mapper.map(customerEntity, Customers.class)).thenReturn(customerDto);

        Customers result = customerService.customerRegistration(input);

        assertNotNull(result);
        assertEquals(1, result.getCustomerId());
        verify(customerDAO).checkDuplicate("john@test.com", "9999999999");
        verify(customerDAO).findMaxCustomerId();
        verify(customerDAO).insertCustomerDetails(any(CustomersEntity.class));
        verify(mapper).map(customerEntity, Customers.class);
    }

    @Test
    void customerRegistration_throwsWhenEmailDuplicate() {
        Customers input = new Customers(null, "John", "john@test.com", "9999999999");

        CustomersEntity dup = new CustomersEntity();
        dup.setCustomerId(2);
        dup.setCustomerName("Other");
        dup.setCustomerEmail("john@test.com");
        dup.setCustomerPhone("0000000000");

        when(customerDAO.checkDuplicate("john@test.com", "9999999999")).thenReturn(List.of(dup));

        DuplicateCustomerException ex = assertThrows(
                DuplicateCustomerException.class,
                () -> customerService.customerRegistration(input)
        );

        assertEquals("Email already exists", ex.getMessage());
        verify(customerDAO).checkDuplicate("john@test.com", "9999999999");
        verify(customerDAO, times(0)).insertCustomerDetails(any(CustomersEntity.class));
    }

    @Test
    void customerRegistration_throwsWhenPhoneDuplicate() {
        Customers input = new Customers(null, "John", "john@test.com", "9999999999");

        CustomersEntity dup = new CustomersEntity();
        dup.setCustomerId(3);
        dup.setCustomerName("Other");
        dup.setCustomerEmail("other@test.com");
        dup.setCustomerPhone("9999999999");

        when(customerDAO.checkDuplicate("john@test.com", "9999999999")).thenReturn(List.of(dup));

        DuplicateCustomerException ex = assertThrows(
                DuplicateCustomerException.class,
                () -> customerService.customerRegistration(input)
        );

        assertEquals("Phone already exists", ex.getMessage());
        verify(customerDAO).checkDuplicate("john@test.com", "9999999999");
        verify(customerDAO, times(0)).insertCustomerDetails(any(CustomersEntity.class));
    }

    @Test
    void getAllCustomers_returnsMappedList() {
        when(customerDAO.getAllCustomerDetails()).thenReturn(List.of(customerEntity));
        when(mapper.map(customerEntity, Customers.class)).thenReturn(customerDto);

        List<Customers> result = customerService.getAllCustomers();

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getCustomerName());
        verify(customerDAO).getAllCustomerDetails();
        verify(mapper).map(customerEntity, Customers.class);
    }

    @Test
    void getAllCustomers_throwsWhenEmpty() {
        when(customerDAO.getAllCustomerDetails()).thenReturn(List.of());

        NoCustomerException ex = assertThrows(NoCustomerException.class, () -> customerService.getAllCustomers());

        assertEquals("No customers available", ex.getMessage());
        verify(customerDAO).getAllCustomerDetails();
    }

    @Test
    void getCustomerById_returnsMappedDto() {
        when(customerDAO.getCustomerById(1)).thenReturn(customerEntity);
        when(mapper.map(customerEntity, Customers.class)).thenReturn(customerDto);

        Customers result = customerService.getCustomerById(1);

        assertEquals(1, result.getCustomerId());
        verify(customerDAO).getCustomerById(1);
        verify(mapper).map(customerEntity, Customers.class);
    }

    @Test
    void getCustomerById_throwsWhenNotFound() {
        when(customerDAO.getCustomerById(99)).thenReturn(null);

        NoCustomerException ex = assertThrows(NoCustomerException.class, () -> customerService.getCustomerById(99));

        assertEquals("Customer not found with ID: 99", ex.getMessage());
        verify(customerDAO).getCustomerById(99);
    }

    @Test
    void getCustomerByName_returnsMappedList() {
        when(customerDAO.getByName("John")).thenReturn(List.of(customerEntity));
        when(mapper.map(customerEntity, Customers.class)).thenReturn(customerDto);

        List<Customers> result = customerService.getCustomerByName("John");

        assertEquals(1, result.size());
        verify(customerDAO).getByName("John");
        verify(mapper).map(customerEntity, Customers.class);
    }

    @Test
    void getCustomerByName_throwsWhenEmpty() {
        when(customerDAO.getByName("Unknown")).thenReturn(List.of());

        NoCustomerException ex = assertThrows(NoCustomerException.class, () -> customerService.getCustomerByName("Unknown"));

        assertEquals("No customer found with name: Unknown", ex.getMessage());
        verify(customerDAO).getByName("Unknown");
    }

    @Test
    void getCustomerByEmail_returnsMappedDto() {
        when(customerDAO.getByEmail("john@test.com")).thenReturn(customerEntity);
        when(mapper.map(customerEntity, Customers.class)).thenReturn(customerDto);

        Customers result = customerService.getCustomerByEmail("john@test.com");

        assertEquals("john@test.com", result.getCustomerEmail());
        verify(customerDAO).getByEmail("john@test.com");
        verify(mapper).map(customerEntity, Customers.class);
    }

    @Test
    void getCustomerByEmail_throwsWhenMissing() {
        when(customerDAO.getByEmail("x@test.com")).thenReturn(null);

        NoCustomerException ex = assertThrows(NoCustomerException.class, () -> customerService.getCustomerByEmail("x@test.com"));

        assertEquals("Customer not found with email: x@test.com", ex.getMessage());
        verify(customerDAO).getByEmail("x@test.com");
    }

    @Test
    void getCustomerByPhone_returnsMappedDto() {
        when(customerDAO.getByPhone("9999999999")).thenReturn(customerEntity);
        when(mapper.map(customerEntity, Customers.class)).thenReturn(customerDto);

        Customers result = customerService.getCustomerByPhone("9999999999");

        assertEquals("9999999999", result.getCustomerPhone());
        verify(customerDAO).getByPhone("9999999999");
        verify(mapper).map(customerEntity, Customers.class);
    }

    @Test
    void getCustomerByPhone_throwsWhenMissing() {
        when(customerDAO.getByPhone("000")).thenReturn(null);

        NoCustomerException ex = assertThrows(NoCustomerException.class, () -> customerService.getCustomerByPhone("000"));

        assertEquals("Customer not found with phone: 000", ex.getMessage());
        verify(customerDAO).getByPhone("000");
    }

    @Test
    void getCustomers_byName_returnsList() {
        when(customerDAO.getByName("John")).thenReturn(List.of(customerEntity));
        when(mapper.map(customerEntity, Customers.class)).thenReturn(customerDto);

        List<Customers> result = customerService.getCustomers("John", null, null);

        assertEquals(1, result.size());
        verify(customerDAO).getByName("John");
    }

    @Test
    void getCustomers_byEmail_returnsList() {
        when(customerDAO.getByEmail("john@test.com")).thenReturn(customerEntity);
        when(mapper.map(customerEntity, Customers.class)).thenReturn(customerDto);

        List<Customers> result = customerService.getCustomers(null, "john@test.com", null);

        assertEquals(1, result.size());
        verify(customerDAO).getByEmail("john@test.com");
    }

    @Test
    void getCustomers_byPhone_returnsList() {
        when(customerDAO.getByPhone("9999999999")).thenReturn(customerEntity);
        when(mapper.map(customerEntity, Customers.class)).thenReturn(customerDto);

        List<Customers> result = customerService.getCustomers(null, null, "9999999999");

        assertEquals(1, result.size());
        verify(customerDAO).getByPhone("9999999999");
    }

    @Test
    void getCustomers_throwsWhenNoParamProvided() {
        NoCustomerException ex = assertThrows(
                NoCustomerException.class,
                () -> customerService.getCustomers(null, null, null)
        );

        assertEquals("Pass at least one param: name / email / phone", ex.getMessage());
    }

    @Test
    void getByNameAndEmail_returnsMappedList() {
        when(customerDAO.getByNameAndEmail("John", "john@test.com")).thenReturn(List.of(customerEntity));
        when(mapper.map(customerEntity, Customers.class)).thenReturn(customerDto);

        List<Customers> result = customerService.getByNameAndEmail("John", "john@test.com");

        assertEquals(1, result.size());
        verify(customerDAO).getByNameAndEmail("John", "john@test.com");
    }

    @Test
    void getByNameAndEmail_throwsWhenEmpty() {
        when(customerDAO.getByNameAndEmail("Unknown", "x@test.com")).thenReturn(List.of());

        NoCustomerException ex = assertThrows(
                NoCustomerException.class,
                () -> customerService.getByNameAndEmail("Unknown", "x@test.com")
        );

        assertEquals("No customer found with name & email", ex.getMessage());
    }

    @Test
    void getByNameAndPhone_returnsMappedList() {
        when(customerDAO.getByNameAndPhone("John", "9999999999")).thenReturn(List.of(customerEntity));
        when(mapper.map(customerEntity, Customers.class)).thenReturn(customerDto);

        List<Customers> result = customerService.getByNameAndPhone("John", "9999999999");

        assertEquals(1, result.size());
        verify(customerDAO).getByNameAndPhone("John", "9999999999");
    }

    @Test
    void getByNameAndPhone_throwsWhenEmpty() {
        when(customerDAO.getByNameAndPhone("Unknown", "000")).thenReturn(List.of());

        NoCustomerException ex = assertThrows(
                NoCustomerException.class,
                () -> customerService.getByNameAndPhone("Unknown", "000")
        );

        assertEquals("No customer found with name & phone", ex.getMessage());
    }

    @Test
    void getByEmailAndPhone_returnsMappedDto() {
        when(customerDAO.getByEmailAndPhone("john@test.com", "9999999999")).thenReturn(customerEntity);
        when(mapper.map(customerEntity, Customers.class)).thenReturn(customerDto);

        Customers result = customerService.getByEmailAndPhone("john@test.com", "9999999999");

        assertEquals("john@test.com", result.getCustomerEmail());
        verify(customerDAO).getByEmailAndPhone("john@test.com", "9999999999");
    }

    @Test
    void getByEmailAndPhone_throwsWhenMissing() {
        when(customerDAO.getByEmailAndPhone("x@test.com", "000")).thenReturn(null);

        NoCustomerException ex = assertThrows(
                NoCustomerException.class,
                () -> customerService.getByEmailAndPhone("x@test.com", "000")
        );

        assertEquals("No customer found with email & phone", ex.getMessage());
    }

    @Test
    void updateCustomer_updatesWhenCustomerExists() {
        Customers input = new Customers(1, "Jane", "jane@test.com", "8888888888");
        when(customerDAO.getCustomerById(1)).thenReturn(customerEntity);
        doNothing().when(customerDAO).updateCustomer(any(CustomersEntity.class));

        customerService.updateCustomer(1, input);

        verify(customerDAO).getCustomerById(1);
        verify(customerDAO).updateCustomer(customerEntity);
        assertEquals("Jane", customerEntity.getCustomerName());
        assertEquals("jane@test.com", customerEntity.getCustomerEmail());
        assertEquals("8888888888", customerEntity.getCustomerPhone());
    }

    @Test
    void updateCustomer_throwsWhenCustomerMissing() {
        when(customerDAO.getCustomerById(anyInt())).thenReturn(null);

        NoCustomerException ex = assertThrows(
                NoCustomerException.class,
                () -> customerService.updateCustomer(99, customerDto)
        );

        assertEquals("Customer not found with ID: 99", ex.getMessage());
        verify(customerDAO).getCustomerById(99);
        verify(customerDAO, times(0)).updateCustomer(any(CustomersEntity.class));
    }
}

