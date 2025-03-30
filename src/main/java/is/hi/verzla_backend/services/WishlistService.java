package is.hi.verzla_backend.services;

import java.util.List;
import java.util.UUID;

import is.hi.verzla_backend.entities.WishlistItem;

/**
 * Service interface for managing user wishlists.
 * <p>
 * Provides methods to add, retrieve, and remove products from a user's
 * wishlist.
 * This abstraction allows for different implementations, facilitating testing
 * and
 * future enhancements.
 * </p>
 *
 * <p>
 * Typical usage involves fetching a user's wishlist items, adding new products
 * to
 * the wishlist, or removing existing products.
 * </p>
 *
 * @see is.hi.verzla_backend.entities.WishlistItem
 */
public interface WishlistService {
  /**
   * Retrieves the list of items in a user's wishlist.
   *
   * @param userId The ID of the user whose wishlist items are to be retrieved.
   * @return A {@code List} of {@link WishlistItem} objects in the user's
   *         wishlist.
   *         Returns an empty list if the user has no wishlist items.
   *
   * @throws IllegalArgumentException if {@code userId} is {@code null}.
   */
  List<WishlistItem> getWishlistByUserId(UUID userId);

  /**
   * Adds a product to a user's wishlist.
   *
   * @param userId    The ID of the user.
   * @param productId The ID of the product to be added to the wishlist.
   *
   * @throws RuntimeException         if the user or product cannot be found.
   * @throws IllegalArgumentException if {@code userId} or {@code productId} is
   *                                  {@code null}.
   */
  void addProductToWishlist(UUID userId, UUID productId);

  /**
   * Removes a product from a user's wishlist.
   *
   * @param userId        The ID of the user.
   * @param wishlistItemId The ID of the wishlist item to be removed.
   *
   * @throws RuntimeException         if the user or wishlist item cannot be found.
   * @throws IllegalArgumentException if {@code userId} or {@code wishlistItemId} is
   *                                  {@code null}.
   */
  void removeWishlistItem(UUID userId, UUID wishlistItemId);

  /**
   * Adds all products in the user's wishlist to their shopping cart.
   *
   * @param userId The ID of the user whose wishlist items should be added to cart.
   *
   * @throws RuntimeException         if the user cannot be found or if there's an issue
   *                                  with adding items to the cart.
   * @throws IllegalArgumentException if {@code userId} is {@code null}.
   */
  void addAllToCart(UUID userId);

  /**
   * Removes all items from a user's wishlist.
   *
   * @param userId The ID of the user whose wishlist should be cleared.
   *
   * @throws RuntimeException         if the user cannot be found.
   * @throws IllegalArgumentException if {@code userId} is {@code null}.
   */
  void clearWishlist(UUID userId);
}
