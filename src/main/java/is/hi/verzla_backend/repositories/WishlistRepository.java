package is.hi.verzla_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

import is.hi.verzla_backend.entities.Wishlist;

/**
 * Repository interface for performing CRUD operations on {@link Wishlist} entities.
 *
 * <p>This repository provides methods to interact with wishlist data in the database,
 * supporting the wishlist functionality in the Verzla e-commerce platform. It extends
 * JpaRepository to inherit standard data access methods while adding custom query
 * methods specific to wishlist operations.</p>
 *
 * <p>Wishlists are a key feature in e-commerce applications, allowing users to:
 * <ul>
 *   <li>Save products of interest for future consideration</li>
 *   <li>Create personal collections of desired products</li>
 *   <li>Easily track products without immediately adding them to a cart</li>
 *   <li>Return to previously viewed products of interest</li>
 * </ul>
 * </p>
 *
 * <p>Each user in the system has exactly one wishlist, which is created automatically
 * when the user account is created. The wishlist maintains a one-to-many relationship
 * with wishlist items, each representing a product added to the wishlist.</p>
 *
 * @see is.hi.verzla_backend.entities.Wishlist
 * @see is.hi.verzla_backend.entities.WishlistItem
 * @see is.hi.verzla_backend.entities.User
 * @see is.hi.verzla_backend.controllers.WishlistController
 */
public interface WishlistRepository extends JpaRepository<Wishlist, UUID> {
    /**
     * Finds a {@link Wishlist} by the ID of its associated user.
     *
     * <p>This method is the primary way to access a user's wishlist throughout
     * the application. It's used whenever wishlist operations are performed, including:
     * <ul>
     *   <li>Displaying wishlist contents to the user</li>
     *   <li>Adding or removing products from the wishlist</li>
     *   <li>Converting wishlist items to cart items when purchasing</li>
     * </ul>
     * </p>
     *
     * @param userId The ID of the user whose wishlist is to be retrieved
     * @return The {@link Wishlist} associated with the specified user ID, or {@code null}
     * if no wishlist exists for the user
     * @throws IllegalArgumentException if {@code userId} is {@code null}
     */
    Wishlist findByUser_Id(UUID userId);
}
