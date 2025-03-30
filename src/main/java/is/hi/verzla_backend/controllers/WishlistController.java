package is.hi.verzla_backend.controllers;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import is.hi.verzla_backend.dto.ApiResponse;
import is.hi.verzla_backend.dto.WishlistItemDto;
import is.hi.verzla_backend.entities.WishlistItem;
import is.hi.verzla_backend.security.UserDetailsImpl;
import is.hi.verzla_backend.services.WishlistService;

/**
 * REST controller for managing wishlist-related actions such as adding,
 * removing, and viewing products in a user's wishlist.
 * <p>
 * This controller handles HTTP requests mapped to {@code /api/wishlist} and
 * interacts with the {@link WishlistService} to perform operations on
 * {@link WishlistItem} entities.
 * </p>
 *
 * <p>
 * Supported operations include:
 * <ul>
 * <li>Fetching all wishlist items for the current user</li>
 * <li>Adding a new product to the wishlist</li>
 * <li>Removing a product from the wishlist</li>
 * <li>Rendering the wishlist view page</li>
 * </ul>
 * </p>
 *
 * @see WishlistService
 * @see WishlistItem
 */
@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

  /**
   * Service layer for handling wishlist-related business logic.
   */
  @Autowired
  private WishlistService wishlistService;

  /**
   * Retrieves all wishlist items associated with the currently logged-in user.
   *
   * @return A {@code List} of {@link WishlistItemDto} objects belonging to the user.
   */
  @GetMapping
  public ResponseEntity<ApiResponse<List<WishlistItemDto>>> getWishlist() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl)) {
      return ResponseEntity
          .status(HttpStatus.UNAUTHORIZED)
          .body(ApiResponse.error("User must be logged in to view wishlist"));
    }
    
    UUID userId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
    List<WishlistItem> wishlistItems = wishlistService.getWishlistByUserId(userId);
    List<WishlistItemDto> wishlistItemDtos = wishlistItems.stream()
        .map(item -> new WishlistItemDto(
            item.getId(),
            item.getProduct().getId(),
            item.getProduct().getName(),
            item.getProduct().getImageUrl(),
            item.getProduct().getPrice(),
            item.getProduct().getDescription()
        ))
        .collect(Collectors.toList());
    
    return ResponseEntity.ok(ApiResponse.success(wishlistItemDtos));
  }

  /**
   * Adds a specified product to the current user's wishlist.
   *
   * @param productRequest The request payload containing the ID of the product to add.
   * @return A {@link ResponseEntity} containing a success message if the operation
   *         is successful, or an error message if it fails.
   *
   * @apiNote The user must be logged in to perform this operation. If the user
   *          is not authenticated, an {@code UNAUTHORIZED} status is returned.
   */
  @PostMapping
  public ResponseEntity<String> addToWishlist(@RequestBody ProductRequest productRequest) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl)) {
      return ResponseEntity
          .status(HttpStatus.UNAUTHORIZED)
          .body("User must be logged in to add items to wishlist");
    }
    
    UUID userId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
    UUID productId = productRequest.getProductId();
    try {
      wishlistService.addProductToWishlist(userId, productId);
      return ResponseEntity.ok("Product added to wishlist");
    } catch (Exception e) {
      return ResponseEntity
          .status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error adding product to wishlist");
    }
  }

  /**
   * Removes a specified product from the current user's wishlist.
   *
   * @param wishlistItemId The ID of the wishlist item to remove.
   * @return A {@code String} message indicating the result of the removal operation.
   *
   * @apiNote The user must be logged in to perform this operation. If the user
   *          is not authenticated, the removal will fail with UNAUTHORIZED status.
   */
  @DeleteMapping("/{wishlistItemId}")
  public ResponseEntity<String> removeFromWishlist(@PathVariable UUID wishlistItemId) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl)) {
      return ResponseEntity
          .status(HttpStatus.UNAUTHORIZED)
          .body("You must be logged in to remove items from the wishlist");
    }
    
    UUID userId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
    try {
      wishlistService.removeWishlistItem(userId, wishlistItemId);
      return ResponseEntity.ok("Product removed from wishlist");
    } catch (Exception e) {
      return ResponseEntity
          .status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error removing product from wishlist");
    }
  }

  /**
   * Adds all items from the user's wishlist to their shopping cart.
   *
   * <p>This endpoint transfers all products currently in the user's wishlist to their 
   * shopping cart, allowing for a quick conversion of wished-for items into potential 
   * purchases. The original wishlist items remain intact after the operation.</p>
   * 
   * <p>If the user's wishlist is empty, the operation succeeds with no items added to the cart.</p>
   *
   * @return ResponseEntity with a success message if the operation completes successfully,
   *         or an error message with appropriate HTTP status code if it fails
   *
   * @apiNote The user must be logged in to perform this operation. If the user
   *          is not authenticated, an {@code UNAUTHORIZED} status is returned.
   * @see is.hi.verzla_backend.services.CartService#addProductToCart(UUID, UUID)
   */
  @PostMapping("/addAllToCart")
  public ResponseEntity<String> addAllToCart() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body("User must be logged in to perform this action");
    }
    
    UUID userId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
    try {
      wishlistService.addAllToCart(userId);
      return ResponseEntity.ok("All items added to cart");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error adding items to cart");
    }
  }

  /**
   * Removes all items from the user's wishlist in a single operation.
   *
   * <p>This endpoint allows users to clear their entire wishlist at once, rather
   * than removing items individually. This is useful for users who want to start
   * a fresh wishlist or who have moved all their wishlist items to their cart.</p>
   *
   * @return ResponseEntity with a success message if the wishlist is cleared successfully,
   *         or an error message with appropriate HTTP status code if the operation fails
   *
   * @apiNote The user must be logged in to perform this operation. If the user
   *          is not authenticated, an {@code UNAUTHORIZED} status is returned.
   * @see is.hi.verzla_backend.services.WishlistService#clearWishlist(UUID)
   */
  @DeleteMapping("/clear")
  public ResponseEntity<String> clearWishlist() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body("User must be logged in to perform this action");
    }
    
    UUID userId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
    try {
      wishlistService.clearWishlist(userId);
      return ResponseEntity.ok("Wishlist cleared");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error clearing wishlist");
    }
  }

  /**
   * Inner static class representing the payload for adding a product to the
   * wishlist.
   */
  public static class ProductRequest {

    /**
     * The ID of the product to be added to the wishlist.
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
