package com.shopme.admin.order;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.report.OrderDetailRepository;
import com.shopme.common.entity.order.OrderDetail;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class OrderDetailRepositoryTests {

	@Autowired private OrderDetailRepository repo;
	
	@Test
	public void testFindWithCategoryAndTimeBetween() throws ParseException {
		
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		Date startTime = dateFormatter.parse("2023-08-16");
		Date endTime = dateFormatter.parse("2023-08-23");
		
		List<OrderDetail> listOrderDetails =  repo.findWithCategoryAndTimeBetween(startTime, endTime);
		
		assertThat(listOrderDetails.size()).isGreaterThan(0);
		
		for(OrderDetail orderDetail : listOrderDetails) {
			System.out.printf("%30s | %d | %10.2f | %10.2f | %10.2f \n",
					orderDetail.getProduct().getCategory().getName(),orderDetail.getQuantity(),orderDetail.getProductCost(),
					orderDetail.getShippingCost(),orderDetail.getSubtotal());
		
		}
	}
	
	@Test
	public void testFindWithProductAndTimeBetween() throws ParseException {
		
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		Date startTime = dateFormatter.parse("2023-08-16");
		Date endTime = dateFormatter.parse("2023-08-23");
		
		List<OrderDetail> listOrderDetails =  repo.findWithProductAndTimeBetween(startTime, endTime);
		assertThat(listOrderDetails.size()).isGreaterThan(0);
		
		for(OrderDetail orderDetail : listOrderDetails) {
			System.out.printf("%30s | %d | %10.2f | %10.2f | %10.2f \n",
					orderDetail.getProduct().getName(),orderDetail.getQuantity(),orderDetail.getProductCost(),
					orderDetail.getShippingCost(),orderDetail.getSubtotal());
		
		}
	}
	
}
