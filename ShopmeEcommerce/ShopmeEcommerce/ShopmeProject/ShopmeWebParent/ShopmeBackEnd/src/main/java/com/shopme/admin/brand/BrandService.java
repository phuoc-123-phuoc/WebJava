package com.shopme.admin.brand;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.common.entity.Brand;

@Service
public class BrandService {
	
	public static final int BRAND_PER_PAGE = 10;
	@Autowired
	private BrandRepository repo;
	
	public List<Brand> listAll(){
		return (List<Brand>) repo.findAll();
	}
	public void listByPage(int pageNum,PagingAndSortingHelper helper){
		helper.listEntities(pageNum,BRAND_PER_PAGE , repo);

	}

	
	public Brand save(Brand brand) {
		return repo.save(brand);
	}
	public Brand get(Integer id) throws BrandNotFoundException{
		try {
			return repo.findById(id);
		}catch(NoSuchElementException ex) {
			throw new BrandNotFoundException("could not fint any brand with ID" + id);
			
		}
	}
	public void delete(Integer id) throws BrandNotFoundException{
		Long countById = repo.countById(id);
		
		if(countById == null || countById ==0) {
			throw new BrandNotFoundException("could not fint any brand with ID" + id);
		}
		repo.deleteById(id);
	}
	public String checkUnique(Integer id, String name) {
		// TODO Auto-generated method stub
		boolean isGreatingNew = (id == null || id ==0);
		Brand brandByName = repo.findByName(name);
		
		if(isGreatingNew) {
			if(brandByName != null) return "Duplicate";
		}else {
				if(brandByName != null && brandByName.getId() != id) {
					return "Duplicate";
				}
			}
			return "OK";
	}
}
