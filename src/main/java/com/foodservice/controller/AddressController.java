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

import com.foodservice.dto.DeliveryAddresses;
import com.foodservice.entity.DeliveryAddressesEntity;
import com.foodservice.services.AddressService;

@RestController
@RequestMapping("/address")
public class AddressController {
	
	@Autowired
	AddressService addressService;
	
	@PostMapping       //chole geche
	public String addAddress(@RequestBody DeliveryAddressesEntity address) {
		addressService.addAddress(address);
		return "Address added";
	}
	
	// 🔹 GET BY ID
    @GetMapping("/by-id")
    public DeliveryAddresses getById(@RequestParam("id") int id) {
        return addressService.getById(id);
    }

    // 🔹 GET BY CITY
    @GetMapping("/by-city")
    public List<DeliveryAddresses> getByCity(@RequestParam("city") String city) {
        return addressService.getByCity(city);
    }

    // 🔹 GET BY STATE
    @GetMapping("/by-state")
    public List<DeliveryAddresses> getByState(@RequestParam("state") String state) {
        return addressService.getByState(state);
    }

    // 🔹 GET BY POSTAL CODE
    @GetMapping("/by-postal")
    public List<DeliveryAddresses> getByPostalCode(@RequestParam("postalCode") String postalCode) {
        return addressService.getByPostalCode(postalCode);
    }
    
	@GetMapping("/customer/{id}")   // chole geche
	public List<DeliveryAddresses> getCustomerAddresses(@PathVariable("id") int id) {
		return addressService.getAddressesByCustomerId(id);
		//return "Customer addresses fetched";
	}
	
	@PutMapping("/{id}")
	public String updateAddress(@PathVariable("id") int id,@RequestBody DeliveryAddressesEntity address) {
		addressService.updateAddress(id, address);
		return "Address updated";
	}
	
//	@DeleteMapping("/{id}")
//    public String deleteAddress(@PathVariable("id") int id) {
//        addressService.deleteAddress(id);
//        return "Address deleted";
//    }
}
