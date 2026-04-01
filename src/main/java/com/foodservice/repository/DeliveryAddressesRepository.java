package com.foodservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.foodservice.entity.DeliveryAddressesEntity;

public interface DeliveryAddressesRepository extends JpaRepository<DeliveryAddressesEntity,Integer>{
	List<DeliveryAddressesEntity> findByCustomerCustomerId(int customerId);
	
	List<DeliveryAddressesEntity> findByCity(String city);

    List<DeliveryAddressesEntity> findByState(String state);

    List<DeliveryAddressesEntity> findByPostalCode(String postalCode);
    
    
 // CITY + STATE
    @Query("SELECT a FROM DeliveryAddressesEntity a WHERE a.city = :city AND a.state = :state")
    List<DeliveryAddressesEntity> findByCityAndState(@Param("city") String city,
                                                     @Param("state") String state);

    //  CITY + POSTAL CODE
    @Query("SELECT a FROM DeliveryAddressesEntity a WHERE a.city = :city AND a.postalCode = :postalCode")
    List<DeliveryAddressesEntity> findByCityAndPostal(@Param("city") String city,
                                                      @Param("postalCode") String postalCode);

    //  STATE + POSTAL CODE
    @Query("SELECT a FROM DeliveryAddressesEntity a WHERE a.state = :state AND a.postalCode = :postalCode")
    List<DeliveryAddressesEntity> findByStateAndPostal(@Param("state") String state,
                                                       @Param("postalCode") String postalCode);
}
