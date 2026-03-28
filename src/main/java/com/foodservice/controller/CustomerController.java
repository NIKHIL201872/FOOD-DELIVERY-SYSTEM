package com.foodservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.foodservice.dto.Customers;
import com.foodservice.entity.CustomersEntity;
import com.foodservice.services.CustomerService;

@RestController
@RequestMapping("/customer")
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;
	
	@PostMapping("/register")   // chole geche 
	public String createCustomer(@RequestBody Customers customers) {
		customerService.customerRegistration(customers);
		return "Customer added";
	}
	
	
	@GetMapping("/allcustomers")    // chole geche
	public List<Customers> getAllCustomerDetails() {
		return customerService.getAllCustomers();
		//return "All customer details";
	}
	
	
	@GetMapping("/{id}")     // chole geche
	public Customers getCustomerById(@PathVariable("id") int id) {
		return customerService.getCustomerById(id);
		//return "Customer fetched";
	}
	
    // 🔹 GET BY NAME
    @GetMapping("/by-name")
    public List<Customers> getByName(@RequestParam("name") String name) {
        return customerService.getCustomerByName(name);
    }

    // 🔹 GET BY EMAIL
    @GetMapping("/by-email")
    public Customers getByEmail(@RequestParam("email") String email) {
        return customerService.getCustomerByEmail(email);
    }

    // 🔹 GET BY PHONE
    @GetMapping("/by-phone")
    public Customers getByPhone(@RequestParam("phone") String phone) {
        return customerService.getCustomerByPhone(phone);
    }

    
	@PutMapping("/{id}")   // chole geche
	public String updateCustomer(@PathVariable("id") int id, @RequestBody Customers customers) {
		customerService.updateCustomer(id, customers);
		return "Customer updated";
	}
	
	
//	@DeleteMapping("/{id}")   // chole geche
//	public String deleteCustomer(@PathVariable("id") int id) {
//		customerService.deleteCustomer(id);
//		return "Customer deleted";
//	}
}
