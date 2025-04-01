package is.hi.verzla_backend.dto;

import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Data Transfer Object (DTO) for Review entities.
 *
 * <p>This class is used to transfer review data between the service layer and the controller,
 * providing a clean separation between the internal entity representation and the API response.</p>
 */
public class ReviewDto {

    private UUID reviewId;
    private UUID productId;
    private Integer rating;
    private String comment;
    private ZonedDateTime date;
    private String reviewerName;
    private String reviewerEmail;

    /**
     * Default constructor.
     */
    public ReviewDto() {
    }

    /**
     * Gets the review ID.
     *
     * @return the review ID
     */
    public UUID getReviewId() {
        return reviewId;
    }

    /**
     * Sets the review ID.
     *
     * @param reviewId the review ID to set
     */
    public void setReviewId(UUID reviewId) {
        this.reviewId = reviewId;
    }

    /**
     * Gets the product ID.
     *
     * @return the product ID
     */
    public UUID getProductId() {
        return productId;
    }

    /**
     * Sets the product ID.
     *
     * @param productId the product ID to set
     */
    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    /**
     * Gets the rating.
     *
     * @return the rating
     */
    public Integer getRating() {
        return rating;
    }

    /**
     * Sets the rating.
     *
     * @param rating the rating to set
     */
    public void setRating(Integer rating) {
        this.rating = rating;
    }

    /**
     * Gets the comment.
     *
     * @return the comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the comment.
     *
     * @param comment the comment to set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Gets the date.
     *
     * @return the date
     */
    public ZonedDateTime getDate() {
        return date;
    }

    /**
     * Sets the date.
     *
     * @param date the date to set
     */
    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    /**
     * Gets the reviewer name.
     *
     * @return the reviewer name
     */
    public String getReviewerName() {
        return reviewerName;
    }

    /**
     * Sets the reviewer name.
     *
     * @param reviewerName the reviewer name to set
     */
    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    /**
     * Gets the reviewer email.
     *
     * @return the reviewer email
     */
    public String getReviewerEmail() {
        return reviewerEmail;
    }

    /**
     * Sets the reviewer email.
     *
     * @param reviewerEmail the reviewer email to set
     */
    public void setReviewerEmail(String reviewerEmail) {
        this.reviewerEmail = reviewerEmail;
    }
}
