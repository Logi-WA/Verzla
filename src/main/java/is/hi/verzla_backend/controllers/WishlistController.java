package is.hi.verzla_backend.controllers;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import is.hi.verzla_backend.services.WishlistService;
import jakarta.servlet.http.HttpSession;

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
   * @param session The current HTTP session used to obtain the user ID.
   * @return A {@code List} of {@link WishlistItemDto} objects belonging to the user.
   */
  @GetMapping
  public ResponseEntity<ApiResponse<List<WishlistItemDto>>> getWishlist(HttpSession session) {
    UUID userId = (UUID) session.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity
          .status(HttpStatus.UNAUTHORIZED)
          .body(ApiResponse.error("User must be logged in to view wishlist"));
    }
    
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
  public ResponseEntity<String> addToWishlist(
      @RequestBody ProductRequest productRequest,
      HttpSession session) {
    UUID userId = (UUID) session.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity
          .status(HttpStatus.UNAUTHORIZED)
          .body("User must be logged in to add items to wishlist");
    }
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
   * @param productId The ID of the product to remove from the wishlist.
   * @param session   The current HTTP session used to obtain the user ID.
   * @return A {@code String} message indicating the result of the removal
   *         operation.
   *
   * @apiNote The user must be logged in to perform this operation. If the user
   *          is not authenticated, the removal will silently fail or could be
   *          handled differently based on implementation.
   */
  @DeleteMapping("/{wishlistItemId}")
  public ResponseEntity<String> removeFromWishlist(
      @PathVariable UUID wishlistItemId,
      HttpSession session) {
    UUID userId = (UUID) session.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity
          .status(HttpStatus.UNAUTHORIZED)
          .body("You must be logged in to remove items from the wishlist");
    }
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

  @PostMapping("/addAllToCart")
  public ResponseEntity<String> addAllToCart(HttpSession session) {
    UUID userId = (UUID) session.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body("User must be logged in to perform this action");
    }
    try {
      wishlistService.addAllToCart(userId);
      return ResponseEntity.ok("All items added to cart");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error adding items to cart");
    }
  }

  @DeleteMapping("/clear")
  public ResponseEntity<String> clearWishlist(HttpSession session) {
    UUID userId = (UUID) session.getAttribute("userId");
    if (userId == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body("User must be logged in to perform this action");
    }
    try {
      wishlistService.clearWishlist(userId);
      return ResponseEntity.ok("Wishlist cleared");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error clearing wishlist");
    }
  }
}
