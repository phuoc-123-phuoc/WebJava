package com.shopme.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.shopme.common.entity.product.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
	
	@Query("SELECT p FROM Product p WHERE p.enabled = true "
	        + "AND (p.category.id = ?1 OR p.category.allParentIDs LIKE %?2%) "
	        + "ORDER BY p.name ASC")
	public Page<Product> listByCategory(Integer categoryId, String categoryIDMatch, Pageable pageable);

	
	public Product findByAlias(String alias);
	
	@Query(value = "select * from products where enabled = true and "
			+ "MATCH(name, short_description, full_description) AGAINST (?1)",
			nativeQuery = true)
	public Page<Product> search(String keyword,Pageable pageable);
	
	@Query(value = "UPDATE `shopmedb`.`products` \r\n"
			+ "SET \r\n"
			+ "`average_rating` = COALESCE((SELECT AVG(r.rating) FROM shopmedb.reviews r WHERE r.product_id = ?1),0), \r\n"
			+ "`review_count` = (SELECT COUNT(r.id) FROM shopmedb.reviews r WHERE r.product_id = ?1)\r\n"
			+ " WHERE (`id` = ?1);", nativeQuery = true)
	@Modifying
	public void updateReviewCountAndAverageRating(Integer productId);

}
