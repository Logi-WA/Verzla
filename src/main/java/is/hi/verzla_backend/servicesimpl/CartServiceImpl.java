package is.hi.verzla_backend.servicesimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import is.hi.verzla_backend.entities.Cart;
import is.hi.verzla_backend.entities.CartItem;
import is.hi.verzla_backend.entities.Product;
import is.hi.verzla_backend.entities.User;
import is.hi.verzla_backend.repositories.CartItemRepository;
import is.hi.verzla_backend.repositories.CartRepository;
import is.hi.verzla_backend.repositories.ProductRepository;
import is.hi.verzla_backend.repositories.UserRepository;
import is.hi.verzla_backend.services.CartService;

/**
 * Implementation of the {@link CartService} interface. Provides methods for
 * managing
 * cart items, including adding, retrieving, and removing items from a user's
 * shopping cart.
 */
@Service
@Transactional
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Retrieves the list of items in a user's cart.
     *
     * @param userId The ID of the user whose cart items are to be retrieved.
     * @return A list of {@link CartItem} objects in the user's cart.
     */
    @Override
    public List<CartItem> getCartItemsByUserId(UUID userId) {
        Cart cart = cartRepository.findByUser_Id(userId);
        return cart != null ? cart.getCartItems() : new ArrayList<>();
    }

    /**
     * Adds a product to a user's cart. If the product already exists in the cart,
     * the quantity is incremented.
     *
     * @param userId    The ID of the user.
     * @param productId The ID of the product to be added to the cart.
     */
    @Override
    public void addProductToCart(UUID userId, UUID productId) {
        Product product = productRepository
                .findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + productId));

        Cart cart = cartRepository.findByUser_Id(userId);
        if (cart == null) {
            cart = new Cart();
            User user = userRepository
                    .findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with id " + userId));
            cart.setUser(user);
            cart = cartRepository.save(cart);
        }

        CartItem existingCartItem = cartItemRepository.findByCartAndProduct(
                cart,
                product);
        if (existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + 1);
            cartItemRepository.save(existingCartItem);
        } else {
            CartItem newCartItem = new CartItem();
            newCartItem.setCart(cart);
            newCartItem.setProduct(product);
            newCartItem.setQuantity(1);
            cartItemRepository.save(newCartItem);
        }
    }

    /**
     * Updates the quantity of an existing item in the cart.
     *
     * @param cartItemId The ID of the cart item to be updated.
     * @param quantity   The new quantity of the item.
     * @return The updated {@link CartItem}.
     */
    @Override
    public void updateCartItemQuantity(
            UUID cartItemId,
            int quantity,
            UUID userId) {
        CartItem cartItem = cartItemRepository
                .findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found with id " + cartItemId));

        // Ensure that the cart item belongs to the user
        if (!cartItem.getCart().getUser().getId().equals(userId)) {
            throw new RuntimeException("Cart item does not belong to user");
        }

        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
    }

    /**
     * Removes a product from a user's cart.
     *
     * @param userId    The ID of the user.
     * @param productId The ID of the product to be removed from the cart.
     */
    @Override
    public void removeCartItem(UUID userId, UUID cartItemId) {
        CartItem item = cartItemRepository
                .findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found with id " + cartItemId));

        // Ensure that the item belongs to the user before deleting it
        if (!item.getCart().getUser().getId().equals(userId)) {
            throw new RuntimeException("Cart item does not belong to user");
        }

        cartItemRepository.delete(item);
    }

    @Override
    public void buyCart(UUID userId) {
        Cart cart = cartRepository.findByUser_Id(userId);
        if (cart != null) {
            cartItemRepository.deleteAllByCart(cart);
        }
    }
}
