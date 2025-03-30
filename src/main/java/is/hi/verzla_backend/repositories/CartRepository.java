package is.hi.verzla_backend.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import is.hi.verzla_backend.entities.Cart;

/**
 * Repository interface for performing CRUD operations on {@link Cart} entities.
 * 
 * <p>This repository provides methods to interact with shopping cart data in the database,
 * forming a crucial component of the e-commerce functionality in the Verzla platform.
 * It extends JpaRepository to inherit standard data access methods while adding
 * custom query methods specific to shopping cart operations.</p>
 * 
 * <p>The shopping cart is a central feature of any e-commerce application, supporting:
 * <ul>
 *   <li>Temporary storage of products a user intends to purchase</li>
 *   <li>Calculation of order totals before checkout</li>
 *   <li>Persistent storage of cart contents between user sessions</li>
 *   <li>Conversion of cart contents to orders during checkout</li>
 * </ul>
 * </p>
 * 
 * <p>Each user has exactly one cart, which is created automatically when the user
 * account is created. The cart maintains a one-to-many relationship with cart items,
 * each representing a product added to the cart.</p>
 * 
 * @see is.hi.verzla_backend.entities.Cart
 * @see is.hi.verzla_backend.entities.CartItem
 * @see is.hi.verzla_backend.entities.User
 * @see is.hi.verzla_backend.controllers.CartController
 */
public interface CartRepository extends JpaRepository<Cart, UUID> {

  /**
   * Finds a {@link Cart} by the associated user's ID.
   * 
   * <p>This method is the primary way to access a user's shopping cart throughout
   * the application. It's used whenever cart operations are performed, including:
   * <ul>
   *   <li>Displaying cart contents to the user</li>
   *   <li>Adding or removing products from the cart</li>
   *   <li>Updating quantities of products in the cart</li>
   *   <li>Converting the cart to an order during checkout</li>
   * </ul>
   * </p>
   *
   * @param userId The ID of the user whose cart is to be retrieved
   * @return The {@link Cart} associated with the specified user ID, or {@code null} if not found
   * @throws IllegalArgumentException if userId is null
   */
  Cart findByUser_Id(UUID userId);
}
