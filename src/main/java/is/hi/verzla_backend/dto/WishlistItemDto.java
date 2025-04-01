package is.hi.verzla_backend.dto;

import java.util.UUID;

/**
 * Data Transfer Object for WishlistItem entities in the Verzla e-commerce platform.
 *
 * <p>This DTO provides a simplified, flattened representation of {@link is.hi.verzla_backend.entities.WishlistItem}
 * entities for API responses. It transforms the complex entity relationships into a structure
 * that's easier for client applications to consume, containing only the essential information
 * needed for displaying wishlist contents to users.</p>
 *
 * <p>Key features of this DTO:
 * <ul>
 *   <li>Flattens the nested Product relationship by including essential product details directly</li>
 *   <li>Omits unnecessary Wishlist relationship information that isn't needed by clients</li>
 *   <li>Includes product details needed for display such as name, price, and image URL</li>
 *   <li>Provides product IDs allowing for easy navigation to product detail pages</li>
 * </ul>
 * </p>
 *
 * <p>This DTO is primarily used in:
 * <ul>
 *   <li>Wishlist display pages</li>
 *   <li>Wishlist management interfaces</li>
 *   <li>Wishlist-to-cart conversion operations</li>
 * </ul>
 * </p>
 *
 * @see is.hi.verzla_backend.entities.WishlistItem
 * @see is.hi.verzla_backend.entities.Product
 * @see is.hi.verzla_backend.controllers.WishlistController
 */
public class WishlistItemDto {
    /**
     * The unique identifier of the wishlist item.
     */
    private UUID id;

    /**
     * The unique identifier of the product in this wishlist item.
     * <p>
     * This allows clients to link to product detail pages or perform
     * product-specific operations like adding to cart.
     * </p>
     */
    private UUID productId;

    /**
     * The name of the product in this wishlist item.
     * <p>
     * Included directly for display purposes without needing to query
     * the product separately.
     * </p>
     */
    private String productName;

    /**
     * URL pointing to the product's image.
     * <p>
     * Included for displaying thumbnail images in the wishlist view.
     * </p>
     */
    private String productImageUrl;

    /**
     * The price of the product in the application's currency.
     * <p>
     * Used for displaying current prices in the wishlist.
     * </p>
     */
    private double productPrice;

    /**
     * Detailed description of the product.
     * <p>
     * Included to provide additional product information in wishlist displays.
     * </p>
     */
    private String productDescription;

    /**
     * Default constructor for serialization frameworks.
     */
    public WishlistItemDto() {
    }

    /**
     * Constructs a WishlistItemDto with all required fields.
     *
     * @param id                 The unique identifier of the wishlist item
     * @param productId          The unique identifier of the product
     * @param productName        The name of the product
     * @param productPrice       The price of the product
     * @param productDescription The description of the product
     */
    public WishlistItemDto(UUID id, UUID productId, String productName, double productPrice,
                           String productDescription) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productDescription = productDescription;
    }

    /**
     * Gets the unique identifier of the wishlist item.
     *
     * @return The wishlist item's UUID
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the wishlist item.
     *
     * @param id The UUID to set
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Gets the unique identifier of the product in this wishlist item.
     *
     * @return The product's UUID
     */
    public UUID getProductId() {
        return productId;
    }

    /**
     * Sets the unique identifier of the product in this wishlist item.
     *
     * @param productId The product UUID to set
     */
    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    /**
     * Gets the name of the product in this wishlist item.
     *
     * @return The product name
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Sets the name of the product in this wishlist item.
     *
     * @param productName The product name to set
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * Gets the URL of the product's image.
     *
     * @return The product image URL
     */
    public String getProductImageUrl() {
        return productImageUrl;
    }

    /**
     * Sets the URL of the product's image.
     *
     * @param productImageUrl The product image URL to set
     */
    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    /**
     * Gets the price of the product in this wishlist item.
     *
     * @return The product price
     */
    public double getProductPrice() {
        return productPrice;
    }

    /**
     * Sets the price of the product in this wishlist item.
     *
     * @param productPrice The product price to set
     */
    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    /**
     * Gets the description of the product in this wishlist item.
     *
     * @return The product description
     */
    public String getProductDescription() {
        return productDescription;
    }

    /**
     * Sets the description of the product in this wishlist item.
     *
     * @param productDescription The product description to set
     */
    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }
}
