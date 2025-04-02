package is.hi.verzla_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

import is.hi.verzla_backend.entities.Cart;
import is.hi.verzla_backend.entities.CartItem;
import is.hi.verzla_backend.entities.Product;

/**
 * Repository interface for performing CRUD operations on {@link CartItem} entities.
 *
 * <p>This repository provides methods to interact with cart item data in the database,
 * supporting shopping cart functionality in the Verzla e-commerce platform. It extends
 * JpaRepository to inherit standard data access methods while adding custom query
 * methods tailored to cart item operations.</p>
 *
 * <p>CartItems represent individual products that have been added to a user's shopping
 * cart, each tracking:
 * <ul>
 *   <li>The product that was added to the cart</li>
 *   <li>The quantity of the product</li>
 *   <li>The parent cart containing this item</li>
 * </ul>
 * </p>
 *
 * <p>This repository is essential for key e-commerce operations including:
 * <ul>
 *   <li>Adding products to carts</li>
 *   <li>Removing products from carts</li>
 *   <li>Updating product quantities</li>
 *   <li>Converting cart items to order items during checkout</li>
 * </ul>
 * </p>
 *
 * @see is.hi.verzla_backend.entities.CartItem
 * @see is.hi.verzla_backend.entities.Cart
 * @see is.hi.verzla_backend.entities.Product
 * @see is.hi.verzla_backend.controllers.CartController
 */
public interface CartItemRepository extends JpaRepository<CartItem, UUID> {

    /**
     * Finds a {@link CartItem} by the associated cart and product.
     *
     * <p>This method is primarily used when adding products to the cart to determine
     * if the product already exists in the cart (in which case the quantity should
     * be incremented) or if a new cart item should be created.</p>
     *
     * <p>This query leverages the many-to-one relationships that CartItem maintains
     * with both Cart and Product entities.</p>
     *
     * @param cart    The cart to search within
     * @param product The product to search for
     * @return The found {@link CartItem}, or {@code null} if no matching item is found
     * @throws IllegalArgumentException if cart or product is null
     */
    CartItem findByCartAndProduct(Cart cart, Product product);

    /**
     * Finds all cart items that belong to a specific cart.
     *
     * <p>This method is used to retrieve all products currently in a user's shopping
     * cart, allowing the application to display the cart contents and calculate totals.
     * It's a fundamental operation for rendering cart pages and checkout flows.</p>
     *
     * @param cart The cart whose items should be retrieved
     * @return A list of {@link CartItem} entities belonging to the specified cart
     * @throws IllegalArgumentException if cart is null
     */
    List<CartItem> findByCart(Cart cart);

    /**
     * Deletes all cart items associated with a specific cart.
     *
     * <p>This method is used to clear a user's shopping cart, typically after an order
     * has been successfully placed or when the user explicitly requests to empty their
     * cart. It efficiently removes all items in a single operation.</p>
     *
     * @param cart The cart whose items should be deleted
     * @throws IllegalArgumentException if cart is null
     */
    void deleteAllByCart(Cart cart);
}
