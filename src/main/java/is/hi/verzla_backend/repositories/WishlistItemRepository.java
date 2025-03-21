package is.hi.verzla_backend.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import is.hi.verzla_backend.entities.Product;
import is.hi.verzla_backend.entities.Wishlist;
import is.hi.verzla_backend.entities.WishlistItem;

/**
 * Repository interface for performing CRUD operations on {@link WishlistItem}
 * entities.
 * <p>
 * Extends {@link JpaRepository} to provide standard data access methods, and
 * includes
 * custom query methods tailored to the application's requirements.
 * </p>
 *
 * @see org.springframework.data.jpa.repository.JpaRepository
 * @see is.hi.verzla_backend.entities.WishlistItem
 */
public interface WishlistItemRepository
    extends JpaRepository<WishlistItem, UUID> {
  /**
   * Finds a {@link WishlistItem} by its associated {@link Wishlist} and
   * {@link Product}.
   *
   * @param wishlist The {@link Wishlist} to search within.
   * @param product  The {@link Product} to find in the wishlist.
   * @return The {@link WishlistItem} matching the given wishlist and product, or
   *         {@code null}
   *         if no such item exists.
   *
   * @throws IllegalArgumentException if {@code wishlist} or {@code product} is
   *                                  {@code null}.
   */
  WishlistItem findByWishlistAndProduct(Wishlist wishlist, Product product);

  List<WishlistItem> findByWishlist(Wishlist wishlist);

  void deleteAllByWishlist(Wishlist wishlist);
}
