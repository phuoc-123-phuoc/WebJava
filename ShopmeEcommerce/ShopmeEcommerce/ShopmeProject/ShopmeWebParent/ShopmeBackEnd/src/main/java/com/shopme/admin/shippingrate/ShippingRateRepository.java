package com.shopme.admin.shippingrate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.shopme.admin.paging.SearchRepository;
import com.shopme.common.entity.ShippingRate;

public interface ShippingRateRepository extends JpaRepository<ShippingRate, Integer>,SearchRepository<ShippingRate, Integer>{

	@Query("select sp from ShippingRate sp where sp.country.id = ?1 and sp.state = ?2")
	public ShippingRate findByCountryAndState(Integer countryId,String state);
	
	@Query("update ShippingRate sp set sp.codSupported = ?2 where sp.id = ?1")
	@Modifying
	public void updateCODSupport(Integer id, boolean enabled);
	
	@Query("select sp from ShippingRate sp where sp.country.name like %?1% or sp.state like %?1%")
	public Page<ShippingRate> findAll(String keyword,Pageable pageable);
	
	public Long countById(Integer id);



}
