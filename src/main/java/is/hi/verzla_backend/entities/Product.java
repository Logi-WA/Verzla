package is.hi.verzla_backend.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * Represents a product in the Verzla e-commerce system.
 *
 * <p>This entity contains all the details of a product available for purchase, including:
 * <ul>
 *   <li>Basic information such as name and description</li>
 *   <li>Pricing information</li>
 *   <li>Image URL for product display</li>
 *   <li>Product categorization through many-to-many relationship with categories</li>
 * </ul>
 * </p>
 *
 * <p>Products are central to the e-commerce functionality, being referenced by:
 * <ul>
 *   <li>Cart items when users add products to their shopping carts</li>
 *   <li>Wishlist items when users add products to their wishlists</li>
 *   <li>Order items when products are purchased</li>
 * </ul>
 * </p>
 *
 * <p>The entity uses Jakarta Persistence API annotations for ORM mapping
 * and Jakarta Validation annotations for ensuring data integrity.</p>
 *
 * @see is.hi.verzla_backend.entities.Category
 * @see is.hi.verzla_backend.entities.CartItem
 * @see is.hi.verzla_backend.entities.WishlistItem
 * @see is.hi.verzla_backend.entities.OrderItem
 */
@Entity
@Table(name = "product")
public class Product {

    /**
     * The unique identifier for the product.
     * <p>
     * Uses UUID for globally unique identification across systems.
     * Generated automatically during entity creation.
     * </p>
     */
    @Id
    // @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /**
     * The name of the product.
     * <p>
     * Required field that cannot be empty. Used for display and search.
     * </p>
     */
    @NotEmpty(message = "Product name cannot be empty")
    @Column(nullable = false)
    private String name;

    /**
     * The price of the product in the application's currency.
     * <p>
     * Must be zero or positive value.
     * </p>
     */
    @PositiveOrZero(message = "Price must be zero or positive")
    @Column(nullable = false)
    private double price;

    /**
     * Detailed description of the product.
     * <p>
     * Contains product features, specifications, and other details.
     * </p>
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    @Min(value = 0, message = "Rating cannot be negative")
    private Double rating;

    private String brand;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "product_tags", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "tag")
    private List<String> tags = new ArrayList<>();

    /**
     * Categories associated with this product.
     * <p>
     * Establishes a many-to-many relationship with Category entities.
     * Uses a join table named "product_categories" to represent the relationship.
     * </p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id") // Foreign key column in the product table
    private Category category;

    /**
     * Gets the ID of the product.
     *
     * @return the ID of the product.
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets the ID of the product.
     *
     * @param id the ID to set.
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Gets the name of the product.
     *
     * @return the name of the product.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the product.
     *
     * @param name the name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the price of the product.
     *
     * @return the price of the product.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the price of the product.
     *
     * @param price the price to set.
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Gets the rating of the product.
     *
     * @return the rating of the product.
     */
    public Double getRating() {
        return rating;
    }

    /**
     * Sets the rating of the product.
     *
     * @param rating the rating to set.
     */
    public void setRating(Double rating) {
        this.rating = rating;
    }

    /**
     * Gets the brand of the product.
     *
     * @return the brand of the product.
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Sets the brand of the product.
     *
     * @param brand the brand to set.
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * Gets the tags associated with the product.
     *
     * @return the tags associated with the product.
     */
    public List<String> getTags() {
        return tags;
    }

    /**
     * Sets the tags associated with the product.
     *
     * @param tags the tags to set.
     */
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    /**
     * Gets the description of the product.
     *
     * @return the description of the product.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the product.
     *
     * @param description the description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
