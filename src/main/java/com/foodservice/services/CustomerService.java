package com.foodservice.services;

import java.util.ArrayList;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foodservice.DAO.CustomerDAO;
import com.foodservice.dto.Customers;
import com.foodservice.entity.CustomersEntity;
import com.foodservice.exception.NoCustomerException;

@Service
public class CustomerService {
	
	@Autowired
	CustomerDAO customerDAO;
	
	@Autowired
	ModelMapper mapper;
	
	public Customers customerRegistration(Customers customers) {
		Integer maxId = customerDAO.findMaxCustomerId();
		Integer newId = (maxId == null? 1:maxId+1);
		
		CustomersEntity customersEntity = new CustomersEntity();
		customersEntity.setCustomerId(newId);
		customersEntity.setCustomerName(customers.getCustomerName());
		customersEntity.setCustomerEmail(customers.getCustomerEmail());
		customersEntity.setCustomerPhone(customers.getCustomerPhone());
		CustomersEntity saved = customerDAO.insertCustomerDetails(customersEntity);
		
		return mapper.map(saved, Customers.class);
	}
	
	public List<Customers> getAllCustomers() {

	    List<CustomersEntity> list = customerDAO.getAllCustomerDetails();

	    if (list == null || list.isEmpty()) {
	        throw new NoCustomerException("No customers available");
	    }

	    return list.stream()
	            .map(entity -> mapper.map(entity, Customers.class))
	            .collect(Collectors.toList());
	}
	
	public Customers getCustomerById(int id) {
		CustomersEntity customer = customerDAO.getCustomerById(id);
		
		
		/*c.setCustomerId(id);
		c.setCustomerName(customer.getCustomerName());
		c.setCustomerEmail(customer.getCustomerEmail());
		c.setCustomerPhone(customer.getCustomerPhone());*/
		if(customer == null) {
			throw new NoCustomerException("Customer not found with ID: " + id);
//			System.out.println("Name: " + customer.getCustomerName());
//			System.out.println("Mobile: " + customer.getCustomerPhone());
//			System.out.println("Email: " + customer.getCustomerEmail());
//			
//			if(customer.getAddresses() != null) {
//	            System.out.println("---- Addresses ----");
//	            customer.getAddresses().forEach(a -> {
//	                System.out.println("City: " + a.getCity());
//	                System.out.println("State: " + a.getState());
//	            });
//	        }
//
//	        if(customer.getOrders() != null) {
//	            System.out.println("---- Orders ----");
//	            customer.getOrders().forEach(o -> {
//	                System.out.println("Order ID: " + o.getOrderId());
//	            });
//	        }
		} else {
			Customers c = mapper.map(customer, Customers.class);   //entity to DTO mapping done by model mapper
			return c;
		}
	}
	
	public List<Customers> getCustomerByName(String name) {
        List<CustomersEntity> list = customerDAO.getByName(name);

        if (list == null || list.isEmpty()) {
            throw new NoCustomerException("No customer found with name: " + name);
        }

        return list.stream()
                .map(e -> mapper.map(e, Customers.class))
                .collect(Collectors.toList());
    }
	
	
	public Customers getCustomerByEmail(String email) {
        CustomersEntity entity = customerDAO.getByEmail(email);

        if (entity == null) {
            throw new NoCustomerException("Customer not found with email: " + email);
        }

        return mapper.map(entity, Customers.class);
    }
	
	
	public Customers getCustomerByPhone(String phone) {
        CustomersEntity entity = customerDAO.getByPhone(phone);

        if (entity == null) {
            throw new NoCustomerException("Customer not found with phone: " + phone);
        }

        return mapper.map(entity, Customers.class);
    }
	
	
	public List<Customers> getCustomers(String name, String email, String phone) {

        // 🔹 BY NAME
        if (name != null) {
            List<CustomersEntity> list = customerDAO.getByName(name);

            if (list == null || list.isEmpty()) {
                throw new NoCustomerException("No customer found with name: " + name);
            }

            return list.stream()
                    .map(e -> mapper.map(e, Customers.class))
                    .collect(Collectors.toList());
        }

        // 🔹 BY EMAIL
        if (email != null) {
            CustomersEntity entity = customerDAO.getByEmail(email);

            if (entity == null) {
                throw new NoCustomerException("Customer not found with email: " + email);
            }

            return List.of(mapper.map(entity, Customers.class));
        }

        // 🔹 BY PHONE
        if (phone != null) {
            CustomersEntity entity = customerDAO.getByPhone(phone);

            if (entity == null) {
                throw new NoCustomerException("Customer not found with phone: " + phone);
            }

            return List.of(mapper.map(entity, Customers.class));
        }

        // ❌ No param passed
        throw new NoCustomerException("Pass at least one param: name / email / phone");
    }
    
	public void updateCustomer(int id, Customers customers) {
		CustomersEntity existingCustomer = customerDAO.getCustomerById(id);
		
		if(existingCustomer != null) {
			existingCustomer.setCustomerName(customers.getCustomerName());
			existingCustomer.setCustomerEmail(customers.getCustomerEmail());
			existingCustomer.setCustomerPhone(customers.getCustomerPhone());
			
			customerDAO.updateCustomer(existingCustomer);
		} else {
			throw new NoCustomerException("Customer not found with ID: " + id);
		}
	}
	
//	public void deleteCustomer(int id) {
//		CustomersEntity customer = customerDAO.getCustomerById(id);
//		
//		if(customer != null) {
//			customerDAO.deleteCustomer(id);
//		} else {
//			throw new NoCustomerException("Customer not found with ID: " + id);
//		}
//	}
}
