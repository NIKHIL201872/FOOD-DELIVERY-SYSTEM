package com.foodservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import com.foodservice.exception.NoCustomerException;
import com.foodservice.services.CustomerService;

@RestController
@RequestMapping("/customer")
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;
	
	@PostMapping("/register")  
	public ResponseEntity<String> createCustomer(@RequestBody Customers customers) {
		customerService.customerRegistration(customers);
		return ResponseEntity.ok("Customer added successfully");
	}
	
	
	@GetMapping("/allcustomers")  
	public List<Customers> getAllCustomerDetails() {
		return customerService.getAllCustomers();
		//return "All customer details";
	}
	
	
	@GetMapping("/{id}")     
	public Customers getCustomerById(@PathVariable("id") int id) {
		return customerService.getCustomerById(id);
		//return "Customer fetched";
	}
	
    //GET BY NAME
    @GetMapping("/by-name")
    public List<Customers> getByName(@RequestParam("name") String name) {
        return customerService.getCustomerByName(name);
    }

    // GET BY EMAIL
    @GetMapping("/by-email")
    public Customers getByEmail(@RequestParam("email") String email) {
        return customerService.getCustomerByEmail(email);
    }

    // GET BY PHONE
    @GetMapping("/by-phone")
    public Customers getByPhone(@RequestParam("phone") String phone) {
        return customerService.getCustomerByPhone(phone);
    }

    @GetMapping("/by-name-email")
    public List<Customers> getByNameAndEmail(
            @RequestParam String name,
            @RequestParam String email) throws NoCustomerException {

        return customerService.getByNameAndEmail(name, email);
    }

    //NAME + PHONE
    @GetMapping("/by-name-phone")
    public List<Customers> getByNameAndPhone(
            @RequestParam String name,
            @RequestParam String phone) throws NoCustomerException {

        return customerService.getByNameAndPhone(name, phone);
    }

    // EMAIL + PHONE
    @GetMapping("/by-email-phone")
    public Customers getByEmailAndPhone(
            @RequestParam String email,
            @RequestParam String phone) throws NoCustomerException {

        return customerService.getByEmailAndPhone(email, phone);
    }
	@PutMapping("/{id}")  
	public String updateCustomer(@PathVariable("id") int id, @RequestBody Customers customers) {
		customerService.updateCustomer(id, customers);
		return "Customer updated";
	}
	
	
//	@DeleteMapping("/{id}")   
//	public String deleteCustomer(@PathVariable("id") int id) {
//		customerService.deleteCustomer(id);
//		return "Customer deleted";
//	}
}
