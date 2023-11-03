package com.shopme.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.shopme.common.entity.Customer;
import com.shopme.common.entity.order.Order;

public interface OrderRepository extends JpaRepository<Order, Integer>{
 
	@Query("select distinct o from Order o join o.orderDetails od join od.product p "
			+ "where o.customer.id= ?2 "
			+ " and (p.name like %?1% or o.status like %?1%)")
	public Page<Order> findAll(String keyword,Integer customerId,Pageable pageable);
		
	@Query("select o from Order o where o.customer.id = ?1")
	public Page<Order> findAll(Integer customerId, Pageable pageable);
	
	public Order findByIdAndCustomer(Integer id,Customer customer);
}
