package com.shopme.admin.review;

import com.shopme.common.entity.Review;


public interface IReviewService {

	public Review get(Integer id);
	public void save(Review reviewInForm);
	public void delete(Integer id);
}
