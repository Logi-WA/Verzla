package is.hi.verzla_backend.repositories;

import java.util.UUID;

import is.hi.verzla_backend.entities.Wishlist;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for performing CRUD operations on {@link Wishlist} entities.
 * <p>
 * Extends {@link JpaRepository} to provide standard data access methods, and includes
 * custom query methods tailored to the application's requirements.
 * </p>
 *
 * @see org.springframework.data.jpa.repository.JpaRepository
 * @see is.hi.verzla_backend.entities.Wishlist
 */
public interface WishlistRepository extends JpaRepository<Wishlist, UUID> {
  /**
   * Finds a {@link Wishlist} by the ID of its associated user.
   *
   * @param userId The ID of the user whose wishlist is to be retrieved.
   * @return The {@link Wishlist} associated with the specified user ID, or {@code null}
   *         if no wishlist exists for the user.
   *
   * @throws IllegalArgumentException if {@code userId} is {@code null}.
   */
  Wishlist findByUser_Id(UUID userId);
}
