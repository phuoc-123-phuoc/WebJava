package com.shopme.admin.review;

import java.util.NoSuchElementException;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.product.ProductRepository;
import com.shopme.common.entity.Review;

import jakarta.transaction.Transactional;


@Service
@Transactional
public class ReviewService implements IReviewService{
	
	public static final int REVIEWS_PER_PAGE = 5;

	
	private ReviewRepository reviewRepo;
	
	private ProductRepository productRepo;
	
	@Autowired 
	public ReviewService(ReviewRepository reviewRepo, ProductRepository productRepo) {
		super();
		this.reviewRepo = reviewRepo;
		this.productRepo = productRepo;
	}

	public void listByPage(int pageNum, PagingAndSortingHelper helper) {
		helper.listEntities(pageNum, REVIEWS_PER_PAGE, reviewRepo);
	}

	@Override
	public Review get(Integer id)  {
		// TODO Auto-generated method stub
		
			return reviewRepo.findById(id);
		
	}

	@Override
	public void save(Review reviewInForm) {
		// TODO Auto-generated method stub
		Review reviewInDB = reviewRepo.findById(reviewInForm.getId());
		reviewInDB.setHeadline(reviewInForm.getHeadline());
		reviewInDB.setComment(reviewInForm.getComment());

		reviewRepo.save(reviewInDB);
		//productRepo.updateReviewCountAndAverageRating(reviewInDB.getProduct().getId());
	}

	@Override
	public void delete(Integer id)  {
		// TODO Auto-generated method stub

		reviewRepo.deleteById(id);
	}

}
