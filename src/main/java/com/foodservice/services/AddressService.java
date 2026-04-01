package com.foodservice.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foodservice.DAO.AddressDAO;
import com.foodservice.DAO.CustomerDAO;
import com.foodservice.dto.DeliveryAddresses;
import com.foodservice.entity.CustomersEntity;
import com.foodservice.entity.DeliveryAddressesEntity;
import com.foodservice.exception.NoAddressException;
import com.foodservice.exception.NoCustomerException;


@Service
public class AddressService {
	
	@Autowired
	AddressDAO addressDAO;
	
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	ModelMapper mapper;
	
	public void addAddress(DeliveryAddressesEntity address) {
//		DeliveryAddressesEntity entity=addressDAO.getById(address.getCustomer().getCustomerId());
//		if(entity!=null) {
//			throw new RuntimeException();
//		}
//		System.out.println(address.getCustomer());
//		addressDAO.saveAddress(address);
		
		Integer customerId = address.getCustomer().getCustomerId();

	    CustomersEntity customer = customerDAO.getCustomerById(customerId);

	    if (customer == null) {
	        throw new NoCustomerException("Customer not found with ID: " + customerId);
	    }

	    address.setCustomer(customer);

	    addressDAO.saveAddress(address);
	}
	
	//  BY ID
    public DeliveryAddresses getById(int id) {
        DeliveryAddressesEntity entity = addressDAO.getById(id);

        if (entity == null) {
            throw new NoAddressException("Address not found with id: " + id);
        }

        return mapper.map(entity, DeliveryAddresses.class);
    }

    //  BY CITY
    public List<DeliveryAddresses> getByCity(String city) {
        List<DeliveryAddressesEntity> list = addressDAO.getByCity(city);

        if (list == null || list.isEmpty()) {
            throw new NoAddressException("No addresses found in city: " + city);
        }

        return list.stream()
                .map(e -> mapper.map(e, DeliveryAddresses.class))
                .collect(Collectors.toList());
    }

    //  BY STATE
    public List<DeliveryAddresses> getByState(String state) {
        List<DeliveryAddressesEntity> list = addressDAO.getByState(state);

        if (list == null || list.isEmpty()) {
            throw new NoAddressException("No addresses found in state: " + state);
        }

        return list.stream()
                .map(e -> mapper.map(e, DeliveryAddresses.class))
                .collect(Collectors.toList());
    }

    //  BY POSTAL CODE
    public List<DeliveryAddresses> getByPostalCode(String postalCode) {
        List<DeliveryAddressesEntity> list = addressDAO.getByPostalCode(postalCode);

        if (list == null || list.isEmpty()) {
            throw new NoAddressException("No addresses found with postal code: " + postalCode);
        }

        return list.stream()
                .map(e -> mapper.map(e, DeliveryAddresses.class))
                .collect(Collectors.toList());
    }
    
 //  CITY + STATE
    public List<DeliveryAddresses> getByCityAndState(String city, String state) throws NoAddressException {

        List<DeliveryAddressesEntity> list = addressDAO.getByCityAndState(city, state);

        if (list == null || list.isEmpty()) {
            throw new NoAddressException("No address found for city & state");
        }

        return list.stream()
                .map(e -> mapper.map(e, DeliveryAddresses.class))
                .collect(Collectors.toList());
    }

    //  CITY + POSTAL
    public List<DeliveryAddresses> getByCityAndPostal(String city, String postalCode) throws NoAddressException {

        List<DeliveryAddressesEntity> list = addressDAO.getByCityAndPostal(city, postalCode);

        if (list == null || list.isEmpty()) {
            throw new NoAddressException("No address found for city & postalCode");
        }

        return list.stream()
                .map(e -> mapper.map(e, DeliveryAddresses.class))
                .collect(Collectors.toList());
    }

    //  STATE + POSTAL
    public List<DeliveryAddresses> getByStateAndPostal(String state, String postalCode) throws NoAddressException {

        List<DeliveryAddressesEntity> list = addressDAO.getByStateAndPostal(state, postalCode);

        if (list == null || list.isEmpty()) {
            throw new NoAddressException("No address found for state & postalCode");
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
}
