package com.shopme.checkout.paypal;


import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class PayPalApiTests {

	private static final String BASE_URL = "https://api.sandbox.paypal.com";
	private static final String GET_ORDER_API = "/v2/checkout/orders/";
	private static final String CLIENT_ID = "AZLn_zUEE1V8Ej4NdlOgyVn13Y_ktI2GNwoANEIqL9sBla8P_waXIbN2paYTj88MaaTLPYJGOsz0Crgf";
	private static final String CLIENT_SECRET = "ECupn9PEnFN14jn5skE43VC2a7St-ya2PwTcbsuGXgRDcJfdcbScOpfh2WMRz5LzBpZtSHVlvI2YM6rc";
	
	@Test
	public void testGetOrderDetails() {
		String orderId = "123Abc";
		String requestURL = BASE_URL + GET_ORDER_API + orderId;
		
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Accept-language", "en-US");
		headers.setBasicAuth(CLIENT_ID,CLIENT_SECRET);
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
		RestTemplate restTemplate = new RestTemplate();
		
		ResponseEntity<PayPalOrderResponse> response = restTemplate.exchange(requestURL, HttpMethod.GET,request,PayPalOrderResponse.class);

		PayPalOrderResponse orderResponse = response.getBody();
		
		System.out.println("Order ID:" + orderResponse.getId());
		System.out.println("Validated:" + orderResponse.validate(orderId));
	} 
}
