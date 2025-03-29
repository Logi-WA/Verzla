package is.hi.verzla_backend.controllers;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import is.hi.verzla_backend.dto.ApiResponse;
import is.hi.verzla_backend.dto.CartItemDto;
import is.hi.verzla_backend.entities.CartItem;
import is.hi.verzla_backend.security.UserDetailsImpl;
import is.hi.verzla_backend.services.CartService;

/**
 * REST controller for managing shopping cart-related actions such as adding,
 * removing, updating, and viewing products in the user's cart.
 * <p>
 * This controller handles HTTP requests mapped to {@code /api/cart} and
 * interacts with the {@link CartService} to perform operations on
 * {@link CartItem} entities.
 * </p>
 *
 * <p>
 * Supported operations include:
 * <ul>
 * <li>Fetching all items in the current user's cart</li>
 * <li>Adding a new product to the cart</li>
 * <li>Updating the quantity of a cart item</li>
 * <li>Removing a product from the cart</li>
 * <li>Completing the purchase of all items in the cart</li>
 * </ul>
 * </p>
 *
 * @see CartService
 * @see CartItem
 */
@RestController
@RequestMapping("/api/cart")
public class CartController {

  /**
   * Service layer for handling cart-related business logic.
   */
  @Autowired
  private CartService cartService;

  /**
   * Retrieves all cart items associated with the currently logged-in user.
   *
   * @return A {@code List} of {@link CartItemDto} objects in the user's cart.
   */
  @GetMapping
  public ResponseEntity<ApiResponse<List<CartItemDto>>> getCartItems() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl)) {
      return ResponseEntity
          .status(HttpStatus.UNAUTHORIZED)
          .body(ApiResponse.error("User must be logged in to view cart"));
    }
    
    UUID userId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
    List<CartItem> cartItems = cartService.getCartItemsByUserId(userId);
    List<CartItemDto> cartItemDtos = cartItems.stream()
        .map(item -> new CartItemDto(
            item.getId(),
            item.getProduct().getId(),
            item.getProduct().getName(),
            item.getProduct().getImageUrl(),
            item.getProduct().getPrice(),
            item.getQuantity()))
        .collect(Collectors.toList());

    return ResponseEntity.ok(ApiResponse.success(cartItemDtos));
  }

  /**
   * Adds a specified product to the current user's shopping cart.
   *
   * @param productRequest The request payload containing the ID of the product to
   *                       add.
   * @return A {@link ResponseEntity} containing a success message if the
   *         operation
   *         is successful, or an error message if it fails.
   *
   * @apiNote The user must be logged in to perform this operation. If the user
   *          is not authenticated, an {@code UNAUTHORIZED} status is returned.
   */
  @PostMapping
  public ResponseEntity<String> addToCart(@RequestBody ProductRequest productRequest) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl)) {
      return ResponseEntity
          .status(HttpStatus.UNAUTHORIZED)
          .body("User must be logged in to add items to cart");
    }
    
    UUID userId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
    UUID productId = productRequest.getProductId();
    try {
      cartService.addProductToCart(userId, productId);
      return ResponseEntity.ok("Product added to cart");
    } catch (Exception e) {
      return ResponseEntity
          .status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error adding product to cart");
    }
  }

  /**
   * Updates the quantity of a specific item in the user's cart.
   *
   * @param cartItemId   The ID of the cart item to update.
   * @param requestBody  A mapping containing the new quantity, which must be
   *                     greater than or equal to 1.
   * @return A {@link ResponseEntity} containing a success message if the update
   *         is successful, or an error message otherwise.
   *
   * @apiNote The key in the request map should be "quantity" with a positive
   *          integer value.
   */
  @PatchMapping("/{cartItemId}")
  public ResponseEntity<?> updateCartItemQuantity(
      @PathVariable UUID cartItemId,
      @RequestBody Map<String, Integer> requestBody) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl)) {
      return ResponseEntity
          .status(HttpStatus.UNAUTHORIZED)
          .body("User must be logged in to update cart items");
    }
    
    UUID userId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
    int newQuantity = requestBody.get("quantity");
    if (newQuantity < 1) {
      return ResponseEntity
          .status(HttpStatus.BAD_REQUEST)
          .body("Quantity must be at least 1");
    }

    try {
      cartService.updateCartItemQuantity(cartItemId, newQuantity, userId);
      return ResponseEntity.ok("Cart item quantity updated");
    } catch (Exception e) {
      return ResponseEntity
          .status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error updating cart item quantity");
    }
  }

  /**
   * Removes an item from the current user's cart.
   *
   * @param cartItemId The ID of the cart item to be removed.
   * @return A {@link ResponseEntity} containing a success message if the removal
   *         is successful, or an error message otherwise.
   *
   * @apiNote The user must be logged in to perform this operation.
   */
  @DeleteMapping("/{cartItemId}")
  public ResponseEntity<String> removeFromCart(@PathVariable UUID cartItemId) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl)) {
      return ResponseEntity
          .status(HttpStatus.UNAUTHORIZED)
          .body("User must be logged in to remove items from cart");
    }
    
    UUID userId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
    try {
      cartService.removeCartItem(userId, cartItemId);
      return ResponseEntity.ok("Item removed from cart");
    } catch (Exception e) {
      return ResponseEntity
          .status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error removing item from cart");
    }
  }

  /**
   * Completes the purchase of all items currently in the user's cart.
   *
   * @return A {@link ResponseEntity} containing a success message if the purchase
   *         is successful, or an error message otherwise.
   *
   * @apiNote The user must be logged in to perform this operation.
   */
  @PostMapping("/buy")
  public ResponseEntity<String> buyCart() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body("User must be logged in to buy items");
    }
    
    UUID userId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
    try {
      cartService.buyCart(userId);
      return ResponseEntity.ok("Items bought successfully");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error buying items");
    }
  }

  /**
   * Inner static class representing the payload for adding a product to the
   * shopping cart.
   */
  public static class ProductRequest {
    /**
     * The ID of the product to be added to the cart.
     */
    private UUID productId;

    /**
     * Retrieves the product ID from the request.
     *
     * @return The ID of the product to add.
     */
    public UUID getProductId() {
      return productId;
    }

    /**
     * Sets the product ID for the request.
     *
     * @param productId The ID of the product to add.
     */
    public void setProductId(UUID productId) {
      this.productId = productId;
    }
  }
}
