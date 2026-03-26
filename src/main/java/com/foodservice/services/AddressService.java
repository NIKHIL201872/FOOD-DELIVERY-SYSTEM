package com.foodservice.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foodservice.DAO.AddressDAO;
import com.foodservice.dto.DeliveryAddresses;
import com.foodservice.entity.DeliveryAddressesEntity;
import com.foodservice.exception.NoAddressException;

@Service
public class AddressService {
	
	@Autowired
	AddressDAO addressDAO;
	
	@Autowired
	ModelMapper mapper;
	
	public void addAddress(DeliveryAddressesEntity address) {
		addressDAO.saveAddress(address);
	}
	
	// 🔹 BY ID
    public DeliveryAddresses getById(int id) {
        DeliveryAddressesEntity entity = addressDAO.getById(id);

        if (entity == null) {
            throw new NoAddressException("Address not found with id: " + id);
        }

        return mapper.map(entity, DeliveryAddresses.class);
    }

    // 🔹 BY CITY
    public List<DeliveryAddresses> getByCity(String city) {
        List<DeliveryAddressesEntity> list = addressDAO.getByCity(city);

        if (list == null || list.isEmpty()) {
            throw new NoAddressException("No addresses found in city: " + city);
        }

        return list.stream()
                .map(e -> mapper.map(e, DeliveryAddresses.class))
                .collect(Collectors.toList());
    }

    // 🔹 BY STATE
    public List<DeliveryAddresses> getByState(String state) {
        List<DeliveryAddressesEntity> list = addressDAO.getByState(state);

        if (list == null || list.isEmpty()) {
            throw new NoAddressException("No addresses found in state: " + state);
        }

        return list.stream()
                .map(e -> mapper.map(e, DeliveryAddresses.class))
                .collect(Collectors.toList());
    }

    // 🔹 BY POSTAL CODE
    public List<DeliveryAddresses> getByPostalCode(String postalCode) {
        List<DeliveryAddressesEntity> list = addressDAO.getByPostalCode(postalCode);

        if (list == null || list.isEmpty()) {
            throw new NoAddressException("No addresses found with postal code: " + postalCode);
        }

        return list.stream()
                .map(e -> mapper.map(e, DeliveryAddresses.class))
                .collect(Collectors.toList());
    }
    
	public List<DeliveryAddresses> getAddressesByCustomerId(int customerId) {
		List<DeliveryAddressesEntity> list = addressDAO.getAddressesByCustomerId(customerId);
		
		if(list!=null && !list.isEmpty()) {
			return list.stream().map(entity->mapper.map(entity,DeliveryAddresses.class))
					.collect(Collectors.toList());
//			for (DeliveryAddressesEntity a : list) {
//                System.out.println("Address ID: " + a.getAddressId());
//                System.out.println("Line1: " + a.getAddressLine1());
//                System.out.println("City: " + a.getCity());
//                System.out.println("State: " + a.getState());
//                System.out.println("Pincode: " + a.getPostalCode());
//                System.out.println("==============================");
//            }
		} else {
			throw new NoAddressException("No addresses found for customer id: " + customerId);
		}
	}
	
	public void updateAddress(int id, DeliveryAddressesEntity address) {
        DeliveryAddressesEntity existing = addressDAO.getAddressById(id);

        if (existing != null) {
            existing.setAddressLine1(address.getAddressLine1());
            existing.setAddressLine2(address.getAddressLine2());
            existing.setCity(address.getCity());
            existing.setState(address.getState());
            existing.setPostalCode(address.getPostalCode());

            addressDAO.updateAddress(existing);
        } else {
            throw new NoAddressException("Address not found with id: " + id);
        }
    }
	
//	public void deleteAddress(int id) {
//		DeliveryAddressesEntity address = addressDAO.getAddressById(id);
//		if(address != null) {
//			addressDAO.deleteAddress(id);
//		} else {
//			 throw new RuntimeException("bokachoda");
//			//throw new NoCustomerException("Address not found with id: "+id);
//		}
//	}
}
