package com.shopme.address;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class AddressRepositoryTests {

	@Autowired private AddressRepository repo;
	
	@Test
	public void testAddNew() {
		Integer customerId = 9;
		Integer countryId = 3;
		
		Address newAddress = new Address();
		newAddress.setCustomer(new Customer(customerId));
		newAddress.setCountry(new Country(countryId));
		newAddress.setFirstName("Ana Maria");
		newAddress.setLastName("H");
		newAddress.setPhoneNumber("0836-564-123");
		newAddress.setAddressLine1("Fuchia");
		newAddress.setAddressLine2("China");
		newAddress.setCity("QuangDong");
		newAddress.setState("Thu Duc");
		newAddress.setPostalCode("0909");
		
		Address savedAddress = repo.save(newAddress);
		
		assertThat(savedAddress).isNotNull();
		assertThat(savedAddress.getId()).isGreaterThan(0);
		
	}
	
	@Test
	public void testFindByCustomer() {
		Integer customerId = 9;
		List<Address> listAddresses = repo.findByCustomer(new Customer(customerId));
		assertThat(listAddresses.size()).isGreaterThan(0);
		
		listAddresses.forEach(System.out::println);
	}
	
	@Test 
	public void testFindByIdAndCustomer() {
		Integer customerId = 9;
		Integer addressId = 1;
		
		Address address = repo.findByIdAndCustomer(addressId, customerId);
		
		assertThat(address).isNotNull();
		System.out.println(address);
	}
	

	@Test 
	public void testUpdate() {
		Integer addressId = 4;
		String phoneNumber = "999-999-999";
		
		Address address = repo.findById(addressId).get();
		address.setDefaultForShipping(true);
		
		Address updateAddress = repo.save(address);
		
		assertThat(updateAddress.getPhoneNumber()).isEqualTo(phoneNumber);

	}

	@Test 
	public void testDeleteByIdAndCustomer() {
		Integer addressId = 3;
		Integer customerId = 9;
	
		repo.deleteByIdAndCustomer(addressId, customerId);
		
		Address deleteAddress = repo.findByIdAndCustomer(addressId, customerId);
		
		assertThat(deleteAddress).isNull();

	}
	
	@Test
	public void testSetDefault() {
		
		Integer addressId = 6;
		repo.setDefaultAddress(addressId);
		
		Address address = repo.findById(addressId).get();
		
		assertThat(address.isDefaultForShipping()).isTrue();
	}
	
	@Test
	public void testSetNoneDefaultAddresses() {
	    Integer addressId = 6;
	    Integer customerId = 9;

	    repo.setNoneDefaultForOthers(addressId, customerId);
	}
	
	@Test
	public void testGetDefault() {
		Integer customerId = 9;
		Address address = repo.findDefaultByCustomer(customerId);
		assertThat(address).isNotNull();
		System.out.println(address);
	}
	
}
