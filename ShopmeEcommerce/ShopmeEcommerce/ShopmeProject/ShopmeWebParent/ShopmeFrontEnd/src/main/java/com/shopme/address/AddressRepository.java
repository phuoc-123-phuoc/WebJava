package com.shopme.address;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Customer;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {

    List<Address> findByCustomer(Customer customer);

    @Query("select a from Address a where a.id = ?1 and a.customer.id = ?2")
    Address findByIdAndCustomer(Integer addressId, Integer customerId);

    @Modifying
    @Query("delete from Address a where a.id = ?1 and a.customer.id = ?2")
    void deleteByIdAndCustomer(Integer addressId, Integer customerId);

    @Modifying
    @Query("update Address a set a.defaultForShipping = true where a.id = ?1")
    public void setDefaultAddress(Integer id);
    
    @Modifying
    @Query("update Address a set a.defaultForShipping = false where a.id <> ?1 and a.customer.id = ?2")
    public void setNoneDefaultForOthers(Integer defaultAddressId, Integer customerId);

    @Query("select a from Address a where a.customer.id = ?1 and a.defaultForShipping = true")
    public Address findDefaultByCustomer(Integer customerId);    
}