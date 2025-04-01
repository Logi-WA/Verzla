package is.hi.verzla_backend.dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Data Transfer Object for Product entities in the Verzla e-commerce platform.
 *
 * <p>This DTO provides a simplified representation of {@link is.hi.verzla_backend.entities.Product}
 * entities for API responses, containing only the essential information needed by client
 * applications. It converts the database entity's complex relationships into a flat,
 * serializable structure that's easier to process on the client side.</p>
 *
 * <p>Key differences from the Product entity:
 * <ul>
 *   <li>Category relationships are simplified to a set of category names</li>
 *   <li>Unnecessary fields for client display are omitted</li>
 *   <li>Bidirectional relationships are removed to prevent circular references during serialization</li>
 * </ul>
 * </p>
 *
 * <p>This DTO is used in several API endpoints:
 * <ul>
 *   <li>Product listing pages that display multiple products</li>
 *   <li>Product detail pages that show complete information about a specific product</li>
 *   <li>Search results that include product information</li>
 *   <li>Category pages that show products within a specific category</li>
 * </ul>
 * </p>
 *
 * @see is.hi.verzla_backend.entities.Product
 * @see is.hi.verzla_backend.controllers.ProductController
 */
public class ProductDto {
    /**
     * The unique identifier of the product.
     */
    private UUID id;

    /**
     * The name of the product.
     */
    private String name;

    /**
     * The price of the product in the application's currency.
     */
    private double price;

    /**
     * Detailed description of the product.
     */
    private String description;

    private Double rating;

    private String brand;

    private List<String> tags = new ArrayList<>();

    /**
     * Set of category names that this product belongs to.
     * <p>
     * This simplifies the many-to-many relationship with categories in the entity model,
     * providing just the category names which are typically all that's needed for display.
     * </p>
     */
    private Set<String> categories = new HashSet<>();

    /**
     * Default constructor for serialization frameworks.
     */
    public ProductDto() {
    }

    // Getters and setters

    /**
     * Gets the unique identifier of the product.
     *
     * @return The product's UUID
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the product.
     *
     * @param id The UUID to set
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Gets the name of the product.
     *
     * @return The product's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the product.
     *
     * @param name The name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the price of the product.
     *
     * @return The product's price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the price of the product.
     *
     * @param price The price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Gets the detailed description of the product.
     *
     * @return The product's description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the detailed description of the product.
     *
     * @param description The description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    /**
     * Gets the set of category names associated with the product.
     *
     * @return A set of category name strings
     */
    public Set<String> getCategories() {
        return categories;
    }

    /**
     * Sets the categories associated with the product.
     *
     * @param categories The set of category names to associate with this product
     */
    public void setCategories(Set<String> categories) {
        this.categories = categories;
    }
}
