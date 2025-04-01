package is.hi.verzla_backend.dto;

import java.util.UUID;

/**
 * Data Transfer Object for CartItem entities in the Verzla e-commerce platform.
 *
 * <p>This DTO provides a simplified, flattened representation of {@link is.hi.verzla_backend.entities.CartItem}
 * entities for API responses. It transforms the complex entity relationships into a structure
 * that's easier for client applications to consume, containing only the essential information
 * needed for displaying cart contents to users.</p>
 *
 * <p>Key features of this DTO:
 * <ul>
 *   <li>Flattens the nested Product relationship by including essential product details directly</li>
 *   <li>Includes quantity information for inventory and pricing calculations</li>
 *   <li>Omits unnecessary Cart relationship information that isn't needed by clients</li>
 *   <li>Provides all the data needed to render shopping cart line items in the UI</li>
 * </ul>
 * </p>
 *
 * <p>This DTO is primarily used in:
 * <ul>
 *   <li>Shopping cart display pages</li>
 *   <li>Checkout summary screens</li>
 *   <li>Cart modification responses (add/remove/update items)</li>
 * </ul>
 * </p>
 *
 * @see is.hi.verzla_backend.entities.CartItem
 * @see is.hi.verzla_backend.entities.Product
 * @see is.hi.verzla_backend.controllers.CartController
 */
public class CartItemDto {
    /**
     * The unique identifier of the cart item.
     */
    private UUID id;

    /**
     * The unique identifier of the product in this cart item.
     * <p>
     * This allows clients to link to product detail pages or perform
     * product-specific operations without loading full product details.
     * </p>
     */
    private UUID productId;

    /**
     * The name of the product in this cart item.
     * <p>
     * Included directly for display purposes without needing to query
     * the product separately.
     * </p>
     */
    private String productName;

    /**
     * URL pointing to the product's image.
     * <p>
     * Included for displaying thumbnail images in the cart view.
     * </p>
     */
    private String productImageUrl;

    /**
     * The price of the product in the application's currency.
     * <p>
     * Used for displaying line item prices and calculating subtotals.
     * </p>
     */
    private double productPrice;

    /**
     * The quantity of the product in the cart.
     * <p>
     * Represents how many units of this product the user intends to purchase.
     * Used for quantity selection controls and price calculations.
     * </p>
     */
    private int quantity;

    /**
     * Default constructor for serialization frameworks.
     */
    public CartItemDto() {
    }

    /**
     * Constructs a CartItemDto with all required fields.
     *
     * @param id           The unique identifier of the cart item
     * @param productId    The unique identifier of the product
     * @param productName  The name of the product
     * @param productPrice The price of the product
     * @param quantity     The quantity of the product in the cart
     */
    public CartItemDto(UUID id, UUID productId, String productName, double productPrice, int quantity) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
    }

    /**
     * Gets the unique identifier of the cart item.
     *
     * @return The cart item's UUID
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the cart item.
     *
     * @param id The UUID to set
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Gets the unique identifier of the product in this cart item.
     *
     * @return The product's UUID
     */
    public UUID getProductId() {
        return productId;
    }

    /**
     * Sets the unique identifier of the product in this cart item.
     *
     * @param productId The product UUID to set
     */
    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    /**
     * Gets the name of the product in this cart item.
     *
     * @return The product name
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Sets the name of the product in this cart item.
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
     * Gets the price of the product in this cart item.
     *
     * @return The product price
     */
    public double getProductPrice() {
        return productPrice;
    }

    /**
     * Sets the price of the product in this cart item.
     *
     * @param productPrice The product price to set
     */
    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    /**
     * Gets the quantity of the product in the cart.
     *
     * @return The quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of the product in the cart.
     *
     * @param quantity The quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
