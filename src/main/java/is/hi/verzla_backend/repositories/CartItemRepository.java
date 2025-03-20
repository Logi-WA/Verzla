package is.hi.verzla_backend.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import is.hi.verzla_backend.entities.Cart;
import is.hi.verzla_backend.entities.CartItem;
import is.hi.verzla_backend.entities.Product;

/**
 * Repository interface for performing CRUD operations on {@link CartItem}
 * entities.
 */
public interface CartItemRepository extends JpaRepository<CartItem, UUID> {

  /**
   * Finds a {@link CartItem} by the associated cart and product.
   *
   * @param cart    the cart to search within
   * @param product the product to search for
   * @return the found {@link CartItem}, or {@code null} if no matching item is
   *         found
   */
  CartItem findByCartAndProduct(Cart cart, Product product);

  void deleteAllByCart(Cart cart);
}
