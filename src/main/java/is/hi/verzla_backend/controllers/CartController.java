package is.hi.verzla_backend.controllers;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import is.hi.verzla_backend.services.CartService;
import jakarta.servlet.http.HttpSession;

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
 * <li>Fetching all cart items for the current user</li>
 * <li>Adding a new product to the cart</li>
 * <li>Updating the quantity of a cart item</li>
 * <li>Removing a product from the cart</li>
 * <li>Rendering the cart view page</li>
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
   * @param session The current HTTP session used to obtain the user ID.
   * @return A {@code List} of {@link CartItemDto} objects in the user's cart.
   */
  @GetMapping
  public ResponseEntity<ApiResponse<List<CartItemDto>>> getCartItems(HttpSession session) {
    UUID userId = (UUID) session.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity
          .status(HttpStatus.UNAUTHORIZED)
          .body(ApiResponse.error("User must be logged in to view cart"));
    }

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
   * @param session        The current HTTP session used to obtain the user ID.
   * @return A {@link ResponseEntity} containing a success message if the
   *         operation
   *         is successful, or an error message if it fails.
   *
   * @apiNote The user must be logged in to perform this operation. If the user
   *          is not authenticated, an {@code UNAUTHORIZED} status is returned.
   */
  @PostMapping
  public ResponseEntity<String> addToCart(
      @RequestBody ProductRequest productRequest,
      HttpSession session) {
    UUID userId = (UUID) session.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity
          .status(HttpStatus.UNAUTHORIZED)
          .body("User must be logged in to add items to cart");
    }
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
   * Updates the quantity of a specific cart item.
   *
   * @param id       The ID of the cart item to update.
   * @param quantity The new quantity to set for the cart item.
   * @return The updated {@link CartItem} object.
   *
   * @apiNote The quantity must be a positive integer. Additional validation can
   *          be
   *          implemented to enforce business rules.
   */
  @PatchMapping("/{cartItemId}")
  public ResponseEntity<?> updateCartItemQuantity(
      @PathVariable UUID cartItemId,
      @RequestBody Map<String, Integer> requestBody,
      HttpSession session) {
    UUID userId = (UUID) session.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity
          .status(HttpStatus.UNAUTHORIZED)
          .body("User must be logged in to update cart items");
    }

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
   * Removes a specified product from the current user's shopping cart.
   *
   * @param productId The ID of the product to remove from the cart.
   * @param session   The current HTTP session used to obtain the user ID.
   * @return A {@link ResponseEntity} containing a success message if the
   *         operation
   *         is successful, or an error message if it fails.
   *
   * @apiNote The user must be logged in to perform this operation. If the user
   *          is not authenticated, an {@code UNAUTHORIZED} status is returned.
   */
  @DeleteMapping("/{cartItemId}")
  public ResponseEntity<String> removeFromCart(
      @PathVariable UUID cartItemId,
      HttpSession session) {
    UUID userId = (UUID) session.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity
          .status(HttpStatus.UNAUTHORIZED)
          .body("User must be logged in to remove items from cart");
    }
    try {
      cartService.removeCartItem(userId, cartItemId);
      return ResponseEntity.ok("Product removed from cart");
    } catch (Exception e) {
      return ResponseEntity
          .status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error removing product from cart");
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

  @PostMapping("/buy")
  public ResponseEntity<String> buyCart(HttpSession session) {
    UUID userId = (UUID) session.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body("User must be logged in to buy items");
    }
    try {
      cartService.buyCart(userId);
      return ResponseEntity.ok("Items bought successfully");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error buying items");
    }
  }
}
