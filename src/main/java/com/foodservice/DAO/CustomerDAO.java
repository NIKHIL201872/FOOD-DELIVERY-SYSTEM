package com.foodservice.DAO;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.foodservice.entity.CustomersEntity;
import com.foodservice.repository.CustomersRepository;

@Repository
public class CustomerDAO {
	
	@Autowired
	CustomersRepository customersRepository;
	
	public CustomersEntity insertCustomerDetails(CustomersEntity customersEntity) {
		CustomersEntity cmEntity = customersRepository.save(customersEntity);
		return cmEntity;
	}
	
	public List<CustomersEntity> getAllCustomerDetails(){
		//select
		return customersRepository.findAll();
	}
	
	public CustomersEntity getCustomerById(int id) {
		Optional<CustomersEntity> optional = customersRepository.findById(id);
		return optional.orElse(null);
	}
	
	public void updateCustomer(CustomersEntity customersEntity) {
		customersRepository.save(customersEntity);
	}
	
//	public void deleteCustomer(int id) {
//		customersRepository.deleteById(id);
//	}
	
	public Integer findMaxCustomerId() {
		return customersRepository.MaxCustomerId();
	}
	
	public List<CustomersEntity> getByName(String name) {
        return customersRepository.findByCustomerName(name);
    }

    public CustomersEntity getByEmail(String email) {
        return customersRepository.findByCustomerEmail(email);
    }

    public CustomersEntity getByPhone(String phone) {
        return customersRepository.findByCustomerPhone(phone);
    }
    
    public List<CustomersEntity> getByNameAndEmail(String name, String email) {
        return customersRepository.findByNameAndEmail(name, email);
    }

    public List<CustomersEntity> getByNameAndPhone(String name, String phone) {
        return customersRepository.findByNameAndPhone(name, phone);
    }

    public CustomersEntity getByEmailAndPhone(String email, String phone) {
        return customersRepository.findByEmailAndPhone(email, phone);
    }
}
