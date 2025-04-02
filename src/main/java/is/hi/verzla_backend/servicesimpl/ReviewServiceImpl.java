package is.hi.verzla_backend.servicesimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import is.hi.verzla_backend.dto.CreateReviewDto;
import is.hi.verzla_backend.dto.UpdateReviewDto;
import is.hi.verzla_backend.entities.Product;
import is.hi.verzla_backend.entities.Review;
import is.hi.verzla_backend.exceptions.ResourceNotFoundException;
import is.hi.verzla_backend.repositories.ReviewRepository;
import is.hi.verzla_backend.services.ProductService;
import is.hi.verzla_backend.services.ReviewService;

/**
 * Implementation of ReviewService interface providing business logic for reviews.
 * <p>
 * This class handles the application logic related to reviews including
 * validation, and integration with the ReviewRepository.
 * </p>
 */
@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductService productService; // Needed to fetch Product

    @Override
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    @Override
    public Review getReviewById(UUID reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + reviewId));
    }

    @Override
    public List<Review> getReviewsByProductId(UUID productId) {
        // Call the corrected repository method
        return reviewRepository.findByProduct_Id(productId);
    }

    @Override
    public Review createReviewFromDto(CreateReviewDto dto) {
        // 1. Find the product associated with the review
        Product product = productService.getProductById(dto.getProductId()); // Throws if not found

        // 2. Create a new Review entity
        Review review = new Review();

        // 3. Map data from DTO to Entity
        review.setProduct(product); // Set the actual Product entity
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        review.setReviewerName(dto.getReviewerName());
        review.setReviewerEmail(dto.getReviewerEmail());
        review.setDate(ZonedDateTime.now()); // Set the current timestamp

        // 4. Save the new Review
        Review savedReview = reviewRepository.save(review);

        // 5. Update the associated product's average rating
        productService.updateProductRating(product.getId());

        // 6. Return the saved entity
        return savedReview;
    }

    @Override
    public Review updateReviewFromDto(UUID reviewId, UpdateReviewDto dto) {
        // 1. Get the existing review
        Review existingReview = getReviewById(reviewId); // Throws if not found

        boolean ratingChanged = false;

        // 2. Update fields from DTO
        if (dto.getRating() != null && !dto.getRating().equals(existingReview.getRating())) {
            existingReview.setRating(dto.getRating());
            ratingChanged = true;
        }
        if (dto.getComment() != null) { // Assuming comment can be updated
            existingReview.setComment(dto.getComment());
        }
        // reviewerName/Email are generally not updated, but could be added here if needed

        // 3. Save the updated review
        Review updatedReview = reviewRepository.save(existingReview);

        // 4. Update product rating only if the review's rating actually changed
        if (ratingChanged) {
            productService.updateProductRating(updatedReview.getProduct().getId());
        }

        // 5. Return the updated entity
        return updatedReview;
    }

    @Override
    public void deleteReview(UUID reviewId) {
        Review review = getReviewById(reviewId); // Ensure review exists
        UUID productId = review.getProduct().getId(); // Get product ID before deleting

        reviewRepository.delete(review); // Delete the review

        // Update the associated product's average rating AFTER deletion
        productService.updateProductRating(productId);
    }

}
