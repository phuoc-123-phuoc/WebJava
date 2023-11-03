package com.shopme.admin.shippingrate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.product.ProductRepository;
import com.shopme.admin.setting.country.CountryRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.entity.product.Product;

@Service
public class ShippingRateService {

	public static final int RATES_PER_PAGE = 10;
	private static final int DIM_DIVISER =139;
	
	@Autowired private ShippingRateRepository shippingRepo;
	@Autowired private ProductRepository productRepo;
	@Autowired private CountryRepository countryRepo;
	
	public void listByPage(int pageNum, PagingAndSortingHelper helper) {
		helper.listEntities(pageNum, RATES_PER_PAGE, shippingRepo);
	}
	
	public List<Country> listAllCountries() {
		return countryRepo.findAllByOrderByNameAsc();
	}
	
	public void save(ShippingRate rateInForm) throws ShippingRateAlreadyExistsException {
		ShippingRate rateInDB = shippingRepo.findByCountryAndState(
				rateInForm.getCountry().getId(), rateInForm.getState());
		
		boolean foundExistingRateNewMode = rateInForm.getId() == null && rateInDB != null;
		boolean foundDifferentExistingRateInEditMode = rateInForm.getId() != null && rateInDB != null;
		
		if (foundExistingRateNewMode || foundDifferentExistingRateInEditMode) {
			throw new ShippingRateAlreadyExistsException("There's already a rate for the destination " 
					+ rateInForm.getCountry().getName() + ", " + rateInForm.getState());
			
		}
		shippingRepo.save(rateInForm);
	}
	
	public ShippingRate get(Integer id) throws ShippingRateNotFoundException {
		return shippingRepo.findById(id)
				.orElseThrow(() -> new ShippingRateNotFoundException("Could not find shipping rate with Id " + id));
	}
	
	public void updateSODSurpport(Integer id, boolean codSupported) throws ShippingRateNotFoundException {
		Long count = shippingRepo.countById(id);
		if (count == null || count == 0) {
			throw new ShippingRateNotFoundException("Could not update shipping rate with ID " + id);
		}
		shippingRepo.updateCODSupport(id, codSupported);
	}

	public void delete(Integer id) throws ShippingRateNotFoundException {
		Long count = shippingRepo.countById(id);
		if (count == null || count == 0) {
			throw new ShippingRateNotFoundException("Could not delete shipping rate with ID " + id);
		}
		shippingRepo.deleteById(id);
	}
	
	public float caculateShippingCost(Integer productId,Integer countryId,String state) 
			throws ShippingRateNotFoundException {
		ShippingRate shippingRate =	shippingRepo.findByCountryAndState(countryId, state);
		
		if(shippingRate == null) {
			throw new ShippingRateNotFoundException("No shipping rate found for the given "
						+ " destination. You have to enter shipping cost manually.");
		}
		Product product = productRepo.findById(productId).get();
		float dimWeight = (product.getLength() * product.getWidth() * product.getHeight()) / DIM_DIVISER;
		float finalWeight = product.getWeight() > dimWeight ? product.getWeight() : dimWeight;	
		
		return finalWeight * shippingRate.getRate() ;
	}
}