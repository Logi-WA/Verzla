package is.hi.verzla_backend.entities;

import java.time.ZonedDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * Represents a product review in the Verzla e-commerce system.
 *
 * <p>This entity contains all the details of a review submitted by a user for a product, including:
 * <ul>
 *   <li>Rating value (1-5)</li>
 *   <li>Review comment</li>
 *   <li>Reviewer information</li>
 *   <li>Submission date</li>
 * </ul>
 * </p>
 */
@Entity
@Table(name = "review")
public class Review {

    /**
     * The unique identifier for the review.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "review_id", updatable = false, nullable = false)
    private UUID reviewId;

    @ManyToOne(fetch = FetchType.LAZY) // LAZY is generally preferred for performance
    @JoinColumn(name = "product_id", nullable = false) // Maps to the foreign key column
    @NotNull(message = "Product cannot be null")
    private Product product;

    /**
     * The rating given in the review (1-5).
     */
    @NotNull(message = "Rating cannot be null")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot be more than 5")
    @Column(nullable = false)
    private Integer rating;

    /**
     * The review comment text.
     */
    @NotEmpty(message = "Comment cannot be empty")
    @Column(columnDefinition = "TEXT")
    private String comment;

    /**
     * The date and time when the review was submitted.
     */
    @NotNull(message = "Date cannot be null")
    @Column(nullable = false)
    private ZonedDateTime date;

    /**
     * The name of the reviewer.
     */
    @NotEmpty(message = "Reviewer name cannot be empty")
    @Column(nullable = false)
    private String reviewerName;

    /**
     * The email of the reviewer.
     */
    @NotEmpty(message = "Reviewer email cannot be empty")
    @Email(message = "Reviewer email should be valid")
    @Column(nullable = false)
    private String reviewerEmail;

    /**
     * Default constructor.
     */
    public Review() {
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
