package com.shopme.customer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.AuthenticationType;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CustomerRepositoryTests {

	@Autowired
	CustomerRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void TestCreateCustomer1() {
		Integer countryId = 20;
		Country country = entityManager.find(Country.class, countryId);
		
		Customer customer = new Customer();
		customer.setCountry(country);
		customer.setFirstName("Ly");
		customer.setLastName("Pham");
		customer.setPassword("password");
		customer.setEmail("phamthily@gmail.com");
		customer.setPhoneNumber("0836-080-308");
		customer.setAddressLine1("449 streets");
		customer.setCity("Ho Chi Minh");
		customer.setState("Tang Nhon Phu");
		customer.setPostalCode("9999");
		customer.setCreateTime(new Date());
		
		Customer savedCustomer = repo.save(customer);
		
		assertThat(savedCustomer).isNotNull();
		assertThat(savedCustomer.getId()).isGreaterThan(0);
		
				
	}
	
	@Test
	public void TestCreateCustomer2() {
		Integer countryId = 26;
		Country country = entityManager.find(Country.class, countryId);
		
		Customer customer = new Customer();
		customer.setCountry(country);
		customer.setFirstName("Aless");
		customer.setLastName("Male");
		customer.setPassword("password");
		customer.setEmail("AlessMale@gmail.com");
		customer.setPhoneNumber("0866-990-328");
		customer.setAddressLine1("173, StreaatC");
		customer.setCity("Aus");
		customer.setState("Func");
		customer.setPostalCode("8643");
		customer.setCreateTime(new Date());
		
		Customer savedCustomer = repo.save(customer);
		
		assertThat(savedCustomer).isNotNull();
		assertThat(savedCustomer.getId()).isGreaterThan(0);
		
				
	}
	
	@Test
	public void testListCustomers() {
		Iterable<Customer> customers = repo.findAll();
		customers.forEach(System.out::println);
		
		assertThat(customers).hasSizeGreaterThan(1);
	}
	
	@Test
	public void testUpdateCustomer() {
		Integer customerId = 1;
		String lastName = "Pham Thi";
		
		Customer customer = repo.findById(customerId).get();
		customer.setLastName(lastName);
		customer.setEnabled(true);
		
		Customer updateCustomer = repo.save(customer);
		assertThat(updateCustomer.getLastName()).isEqualTo(lastName);
	}
	
	@Test
	public void testGetCustomer() {
		Integer customerId = 2;
		Optional<Customer> findById = repo.findById(customerId);
		
		assertThat(findById).isPresent();
		
		Customer customer = findById.get();
		System.out.println(customer);
	}
	
	@Test
	public void testDeleteCustomer() {
		Integer customerId = 3;
		repo.deleteById(customerId);
		
		Optional<Customer> findById = repo.findById(customerId);
		assertThat(findById).isNotPresent();
		
	}
	
	@Test
	public void testFindByEmail() {
		String email ="phamthily@gmail.com";
		Customer customer = repo.findByEmail(email);
		
		assertThat(customer).isNotNull();
		System.out.println(customer);
		
	}
	

	@Test
	public void testFindByVerificationCode() {
		String code ="9999";
		Customer customer = repo.findByVerificationCode(code);
		
		assertThat(customer).isNotNull();
		System.out.println(customer);
		
	}
	
	@Test
	public void testEnabledCustomer() {
		Integer customerId = 5;
		repo.enable(customerId);
		
		Customer customer = repo.findById(customerId).get();
		
		assertThat(customer).isNotNull();

	}
	
	/*
	 * @Test public void testUpdateAuthenticationType() { Integer id = 1;
	 * repo.updateAuthenticationType(id, AuthenticationType.FACEBOOK);
	 * 
	 * Customer customer = repo.findById(1).get();
	 * assertThat(customer.getAuthenticationType()).isEqualTo(AuthenticationType.
	 * FACEBOOK);
	 * 
	 * }
	 */
	@Test
	public void testUpdateAuthenticationType() {
	    Customer customer = repo.findById(1).get();
	    customer.setAuthenticationType(AuthenticationType.DATABASE);
	    repo.save(customer);

	    Customer updatedCustomer = repo.findById(1).get();
	    assertThat(updatedCustomer.getAuthenticationType()).isEqualTo(AuthenticationType.DATABASE);
	}
	
	
	
}
