package is.hi.verzla_backend.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import is.hi.verzla_backend.entities.Cart;

/**
 * Repository interface for performing CRUD operations on {@link Cart} entities.
 */
public interface CartRepository extends JpaRepository<Cart, UUID> {

  /**
   * Finds a {@link Cart} by the associated user's ID.
   *
   * @param userId the ID of the user whose cart is to be retrieved
   * @return the {@link Cart} associated with the specified user ID, or {@code null} if not found
   */
  Cart findByUser_Id(UUID userId);
}
