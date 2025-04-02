package is.hi.verzla_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

import is.hi.verzla_backend.entities.Product;
import is.hi.verzla_backend.entities.Wishlist;
import is.hi.verzla_backend.entities.WishlistItem;

/**
 * Repository interface for performing CRUD operations on {@link WishlistItem} entities.
 *
 * <p>This repository provides methods to interact with wishlist item data in the database,
 * supporting wishlist functionality in the Verzla e-commerce platform. It extends
 * JpaRepository to inherit standard data access methods while adding custom query
 * methods tailored to wishlist item operations.</p>
 *
 * <p>WishlistItems represent individual products that have been saved to a user's wishlist,
 * each tracking:
 * <ul>
 *   <li>The product that was added to the wishlist</li>
 *   <li>The parent wishlist containing this item</li>
 *   <li>An association to the user (indirectly through the wishlist)</li>
 * </ul>
 * </p>
 *
 * <p>This repository is essential for key e-commerce operations including:
 * <ul>
 *   <li>Adding products to wishlists</li>
 *   <li>Removing products from wishlists</li>
 *   <li>Checking if a product is already in a user's wishlist</li>
 *   <li>Displaying all products in a user's wishlist</li>
 * </ul>
 * </p>
 *
 * @see is.hi.verzla_backend.entities.WishlistItem
 * @see is.hi.verzla_backend.entities.Wishlist
 * @see is.hi.verzla_backend.entities.Product
 * @see is.hi.verzla_backend.controllers.WishlistController
 */
public interface WishlistItemRepository
        extends JpaRepository<WishlistItem, UUID> {
    /**
     * Finds a {@link WishlistItem} by its associated {@link Wishlist} and
     * {@link Product}.
     *
     * <p>This method is primarily used when adding products to the wishlist to determine
     * if the product already exists in the wishlist (in which case no action is needed)
     * or if a new wishlist item should be created. It's also used when removing specific
     * products from a wishlist.</p>
     *
     * <p>This query leverages the many-to-one relationships that WishlistItem maintains
     * with both Wishlist and Product entities.</p>
     *
     * @param wishlist The {@link Wishlist} to search within.
     * @param product  The {@link Product} to find in the wishlist.
     * @return The {@link WishlistItem} matching the given wishlist and product, or
     * {@code null} if no such item exists.
     * @throws IllegalArgumentException if {@code wishlist} or {@code product} is
     *                                  {@code null}.
     */
    WishlistItem findByWishlistAndProduct(Wishlist wishlist, Product product);

    /**
     * Finds all wishlist items that belong to a specific wishlist.
     *
     * <p>This method is used to retrieve all products currently saved in a user's
     * wishlist, allowing the application to display the wishlist contents. It's a
     * fundamental operation for rendering wishlist pages and enabling quick access
     * to previously saved products.</p>
     *
     * @param wishlist The wishlist whose items should be retrieved
     * @return A list of {@link WishlistItem} entities belonging to the specified wishlist
     * @throws IllegalArgumentException if wishlist is null
     */
    List<WishlistItem> findByWishlist(Wishlist wishlist);

    /**
     * Deletes all wishlist items associated with a specific wishlist.
     *
     * <p>This method is used to clear a user's wishlist, typically when the user
     * explicitly requests to empty their wishlist or when the user account is
     * being deleted. It efficiently removes all items in a single operation.</p>
     *
     * @param wishlist The wishlist whose items should be deleted
     * @throws IllegalArgumentException if wishlist is null
     */
    void deleteAllByWishlist(Wishlist wishlist);
}
