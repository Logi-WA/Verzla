package is.hi.verzla_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

import is.hi.verzla_backend.entities.Review;

/**
 * Repository interface for Review entities.
 * <p>
 * This interface provides CRUD operations for Review entities and
 * custom query methods for retrieving reviews by product ID.
 * </p>
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {

    /**
     * Find all reviews for a specific product.
     *
     * @param productId The ID of the product
     * @return List of reviews for the specified product
     */
    List<Review> findByProduct_Id(UUID productId);
}
