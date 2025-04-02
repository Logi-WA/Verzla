package is.hi.verzla_backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import is.hi.verzla_backend.dto.ApiResponse;
import is.hi.verzla_backend.dto.CreateReviewDto;
import is.hi.verzla_backend.dto.ReviewDto;
import is.hi.verzla_backend.dto.UpdateReviewDto;
import is.hi.verzla_backend.entities.Review;
import is.hi.verzla_backend.exceptions.ResourceNotFoundException;
import is.hi.verzla_backend.services.ReviewService;
import jakarta.validation.Valid;

/**
 * REST controller for managing reviews in the Verzla e-commerce application.
 *
 * <p>This controller provides endpoints for review management operations including:
 * <ul>
 *   <li>Retrieving all reviews or filtering by product</li>
 *   <li>Retrieving specific reviews by ID</li>
 *   <li>Creating new reviews</li>
 *   <li>Updating existing reviews</li>
 *   <li>Deleting reviews</li>
 *   <li>Retrieving average product ratings</li>
 * </ul>
 * </p>
 *
 * <p>The controller converts between Review entities and ReviewDto objects
 * to provide a consistent API response format and protect internal implementation
 * details.</p>
 */
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    /**
     * Retrieves all reviews, with optional filtering by product ID.
     *
     * @param productId Optional product ID to filter reviews by
     * @return ResponseEntity containing a list of reviews
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ReviewDto>>> getReviews(
            @RequestParam(required = false) UUID productId) {
        List<Review> reviews;

        if (productId != null) {
            reviews = reviewService.getReviewsByProductId(productId);
        } else {
            reviews = reviewService.getAllReviews();
        }

        List<ReviewDto> reviewDtos = reviews.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success(reviewDtos));
    }

    /**
     * Retrieves a specific review by its ID.
     *
     * @param id The UUID of the review to retrieve
     * @return ResponseEntity containing the review or a 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReviewDto>> getReviewById(@PathVariable UUID id) {
        try {
            Review review = reviewService.getReviewById(id);
            return ResponseEntity.ok(ApiResponse.success(convertToDto(review)));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Creates a new review.
     *
     * @param review The review to create
     * @return ResponseEntity containing the created review
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ReviewDto>> createReview(@RequestBody @Valid CreateReviewDto createReviewDto) {
        // Service method would need to adapt to take DTO
        Review createdReview = reviewService.createReviewFromDto(createReviewDto); // Example new service method
        return new ResponseEntity<>(
                ApiResponse.success(convertToDto(createdReview)),
                HttpStatus.CREATED);
    }

    /**
     * Updates an existing review using an UpdateReviewDto.
     * Only allows updating rating and comment.
     *
     * @param id              The UUID of the review to update
     * @param updateReviewDto DTO containing the fields to update (rating, comment)
     * @return ResponseEntity containing the updated review DTO or an error message
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateReview(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateReviewDto updateReviewDto) { // Changed to use UpdateReviewDto
        try {
            // Call the service method that accepts the DTO
            Review updatedReview = reviewService.updateReviewFromDto(id, updateReviewDto);
            return ResponseEntity.ok(ApiResponse.success(convertToDto(updatedReview)));
        } catch (ResourceNotFoundException e) { // Be specific
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) { // Catch other potential errors
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to update review: " + e.getMessage()));
        }
    }

    /**
     * Deletes a review.
     *
     * @param id The UUID of the review to delete
     * @return ResponseEntity with success or error message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteReview(@PathVariable UUID id) {
        try {
            reviewService.deleteReview(id);
            return ResponseEntity.ok(ApiResponse.success("Review deleted successfully"));
        } catch (ResourceNotFoundException e) { // Be specific
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            // Log the exception e.g., log.error("Error deleting review {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) // Use appropriate status
                    .body(ApiResponse.error("Failed to delete review."));
        }
    }

    /**
     * Converts a Review entity to ReviewDto.
     *
     * @param review The Review entity to convert
     * @return The ReviewDto
     */
    private ReviewDto convertToDto(Review review) {
        ReviewDto dto = new ReviewDto();
        dto.setReviewId(review.getReviewId());
        // Populate productId from the associated Product entity
        if (review.getProduct() != null) {
            dto.setProductId(review.getProduct().getId());
        }
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setDate(review.getDate());
        dto.setReviewerName(review.getReviewerName());
        dto.setReviewerEmail(review.getReviewerEmail());
        return dto;
    }
}
