package com.shopme.customer;

import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.AuthenticationType;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.setting.CountryRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CustomerService {

	@Autowired private CountryRepository countryRepo;
	@Autowired private CustomerRepository customerRepo;
	@Autowired PasswordEncoder passwordEncoder;
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int TOKEN_LENGTH = 30;
	
	public List<Country> listAllCountries(){
		return	countryRepo.findAllByOrderByNameAsc();
	}
	
	public boolean isEmailUnique(String email) {
	Customer customer = customerRepo.findByEmail(email);
		return customer == null;
	}
	
	public void registerCustomer(Customer customer) {
		encodePassword(customer);
		customer.setEnabled(false);
		customer.setCreateTime(new Date());
		customer.setAuthenticationType(AuthenticationType.DATABASE);
		String randomCode = RandomStringUtils.randomAlphanumeric(64);
		customer.setVerificationCode(randomCode);
		
		/* System.out.println("Verification code: " + randomCode); */
		customerRepo.save(customer);
	}
	
	

	private void encodePassword(Customer customer) {
			String encodePassword =	passwordEncoder.encode(customer.getPassword());
			customer.setPassword(encodePassword);
	}
	
	public boolean verify(String verifycationCode) {
		 Customer customer = customerRepo.findByVerificationCode(verifycationCode);
		
		 if(customer == null || customer.isEnabled()) {
			 return false;
		 }else {
			 customerRepo.enable(customer.getId());
			 return true;
		 }
		 
	}
	
	public void updateAuthenticationType(Customer customer,AuthenticationType type) {
		if(!customer.getAuthenticationType().equals(type)) {
			customerRepo.updateAuthenticationType(customer.getId(), type);
		}
	}

	public Customer getCustomerByEmail(String email) {
		return customerRepo.findByEmail(email);
	}
	
	public void addNewCustomerUponOAuthLogin(String name, String email,String countryCode,
			AuthenticationType authenticationType) {
		Customer customer = new Customer();
		customer.setEmail(email);
		setName(name, customer);

		customer.setEnabled(true);
		customer.setCreateTime(new Date());
		customer.setAuthenticationType(authenticationType);
		customer.setPassword("");
		customer.setAddressLine1("");
		customer.setAddressLine2("");
		customer.setCity("");
		customer.setState("");
		customer.setPhoneNumber("");
		customer.setPostalCode("");
		customer.setCountry(countryRepo.findByCode(countryCode));
		//customer.setCountry(new Country(6));

		customerRepo.save(customer);
	}
	
	private void setName(String name,Customer customer) {
		String[] nameArray = name.split(" ");
		if(nameArray.length < 2) {
			customer.setFirstName(name);
			customer.setLastName("");
		}else {
			String firstName = nameArray[0];
			customer.setFirstName(firstName);
			
			String lastName = name.replaceFirst(firstName + " ", "");
			customer.setLastName(lastName);
		}
	}
	
	public void update(Customer customerInForm) {
        Customer customerInDB = customerRepo.findById(customerInForm.getId()).get();

        if(customerInDB.getAuthenticationType().equals(AuthenticationType.DATABASE)) {
        	 if (!customerInForm.getPassword().isEmpty()) {
     	        String encodedPassword = passwordEncoder.encode(customerInForm.getPassword());
     	        customerInForm.setPassword(encodedPassword);
     	    } else if (customerInDB != null) {
     	            customerInForm.setPassword(customerInDB.getPassword());
     	        }
     	    }else {
 	            customerInForm.setPassword(customerInDB.getPassword());
     	    }
	   
	    customerInForm.setEnabled(customerInDB.isEnabled());
	    customerInForm.setCreateTime(customerInDB.getCreateTime());
	    customerInForm.setVerificationCode(customerInDB.getVerificationCode());
	    customerInForm.setAuthenticationType(customerInDB.getAuthenticationType());
	    customerInForm.setResetPasswordToken(customerInDB.getResetPasswordToken());
	    
	    customerRepo.save(customerInForm);
	}

	public String updateResetPasswordToken(String email) throws CustomerNotFoundException {
        Customer customer = customerRepo.findByEmail(email);
        if (customer != null) {
            String token = generateRandomString(TOKEN_LENGTH);
            customer.setResetPasswordToken(token);
            customerRepo.save(customer);
            
            return token;
        }else {
        	throw new CustomerNotFoundException("Could find any Customer with this email" + email);
        }
    }

	 private String generateRandomString(int length) {
	        Random random = new SecureRandom();
	        StringBuilder sb = new StringBuilder(length);
	        for (int i = 0; i < length; i++) {
	            int randomIndex = random.nextInt(CHARACTERS.length());
	            char randomChar = CHARACTERS.charAt(randomIndex);
	            sb.append(randomChar);
	        }

	        return sb.toString();
	    }
	 
		
	 public Customer getByResetPasswordToken(String token) { 
			return customerRepo.findByResetPasswordToken(token);
	}
	 
	 public void updatePassword(String token,String newPassword) throws CustomerNotFoundException {
		 Customer customer =customerRepo.findByResetPasswordToken(token);
		 
		 if(customer == null) {
			 throw new CustomerNotFoundException("No customer found: invalid token");
		 }
		 
		 customer.setPassword(newPassword);
		 customer.setResetPasswordToken(null);
		 encodePassword(customer);
		 
		 customerRepo.save(customer);
	 }
}
