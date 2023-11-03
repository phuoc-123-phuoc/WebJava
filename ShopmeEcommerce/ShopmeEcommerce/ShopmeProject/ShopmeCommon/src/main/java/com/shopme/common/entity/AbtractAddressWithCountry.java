package com.shopme.common.entity;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbtractAddressWithCountry extends AbstractAddress{
	@ManyToOne
	@JoinColumn(name = "country_id")
	protected Country country;
	
	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}
	@Override
	public String toString() {
	    String address = firstName;

	    if (lastName != null && !lastName.isEmpty()) {
	        address += " , " + lastName;
	    }
	    if (!addressLine1.isEmpty()) {
	        address += " , " + addressLine1;
	    }
	    if (addressLine2 != null && !addressLine2.isEmpty()) {
	        address += " , " + addressLine2;
	    }
	    if (!city.isEmpty()) {
	        address += " , " + city;
	    }
	    if (state != null && !state.isEmpty()) {
	        address += " , " + state;
	    }
	    if (country != null && country.getName() != null && !country.getName().isEmpty()) {
	        address += " , " + country.getName();
	    }

	    if (!postalCode.isEmpty()) {
	        address += " . PostalCode : " + postalCode;
	    }

	    if (!phoneNumber.isEmpty()) {
	        address += ". Phone Number : " + phoneNumber;
	    }
	    
	    return address;
	}
}
