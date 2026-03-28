package com.foodservice.DAO;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.foodservice.dto.DeliveryAddresses;
import com.foodservice.entity.DeliveryAddressesEntity;
import com.foodservice.repository.DeliveryAddressesRepository;

import jakarta.transaction.Transactional;

@Repository
public class AddressDAO {
	
	@Autowired
	DeliveryAddressesRepository addressRepository;
	
	public void saveAddress(DeliveryAddressesEntity address) {
		addressRepository.save(address);
	}
	
	public DeliveryAddressesEntity getById(int id) {
        return addressRepository.findById(id).orElse(null);
    }

    public List<DeliveryAddressesEntity> getByCity(String city) {
        return addressRepository.findByCity(city);
    }

    public List<DeliveryAddressesEntity> getByState(String state) {
        return addressRepository.findByState(state);
    }

    public List<DeliveryAddressesEntity> getByPostalCode(String postalCode) {
        return addressRepository.findByPostalCode(postalCode);
    }
	public List<DeliveryAddressesEntity> getAddressesByCustomerId(int customerId){
		return addressRepository.findByCustomerCustomerId(customerId);
	}
	
	
	public DeliveryAddressesEntity getAddressById(int id) {
		Optional<DeliveryAddressesEntity> optional = addressRepository.findById(id);
		return optional.orElse(null);
	}
	
	public void updateAddress(DeliveryAddressesEntity address) {
        addressRepository.save(address);
    }
	
//	//@Transactional
//	public void deleteAddress(int id) {
//		System.out.println("Trying to delete id: "+id);
//		addressRepository.deleteById(id);
//		System.out.println("Delete executed");
//	}
}
