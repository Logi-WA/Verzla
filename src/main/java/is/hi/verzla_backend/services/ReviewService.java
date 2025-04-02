package is.hi.verzla_backend.services;

import java.util.List;
import java.util.UUID;

import is.hi.verzla_backend.dto.CreateReviewDto;
import is.hi.verzla_backend.dto.UpdateReviewDto;
import is.hi.verzla_backend.entities.Review;

/**
 * Service interface for handling Review business logic.
 * <p>
 * This interface defines the operations available for managing reviews
 * including creating, retrieving, updating, and deleting reviews.
 * </p>
 */
public interface ReviewService {

    List<Review> getAllReviews();

    Review getReviewById(UUID reviewId);

    List<Review> getReviewsByProductId(UUID productId);

    // Use DTO for creation input
    Review createReviewFromDto(CreateReviewDto createReviewDto);

    // Use DTO for update input
    Review updateReviewFromDto(UUID reviewId, UpdateReviewDto updateReviewDto);

    void deleteReview(UUID reviewId);
}
