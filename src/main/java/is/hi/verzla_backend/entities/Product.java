package is.hi.verzla_backend.entities;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
public class Product {

  /**
   * The unique identifier for the product.
   * <p>
   * Uses UUID for globally unique identification across systems.
   * Generated automatically during entity creation.
   * </p>
   */
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  /**
   * The name of the product.
   * <p>
   * Required field that cannot be empty. Used for display and search.
   * </p>
   */
  @NotEmpty(message = "Product name cannot be empty")
  private String name;

  /**
   * The price of the product in the application's currency.
   * <p>
   * Must be zero or positive value.
   * </p>
   */
  @PositiveOrZero(message = "Price must be zero or positive")
  private double price;
  
  /**
   * URL pointing to the product's image.
   * <p>
   * Can reference local or cloud-hosted images.
   * </p>
   */
  private String imageUrl;
  
  /**
   * Detailed description of the product.
   * <p>
   * Contains product features, specifications, and other details.
   * </p>
   */
  private String description;

  /**
   * Categories associated with this product.
   * <p>
   * Establishes a many-to-many relationship with Category entities.
   * Uses a join table named "product_categories" to represent the relationship.
   * </p>
   */
  @ManyToMany
  @JoinTable(
    name = "product_categories",
    joinColumns = @JoinColumn(name = "product_id"),
    inverseJoinColumns = @JoinColumn(name = "category_id")
  )
  private Set<Category> categories = new HashSet<>();

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
   * Gets the URL of the product image.
   *
   * @return the URL of the product image.
   */
  public String getImageUrl() {
    return imageUrl;
  }

  /**
   * Sets the URL of the product image.
   *
   * @param imageUrl the image URL to set.
   */
  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
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

  /**
   * Gets the categories associated with the product.
   *
   * @return the categories associated with the product.
   */
  public Set<Category> getCategories() {
    return categories;
  }

  /**
   * Sets the categories associated with the product.
   *
   * @param categories the categories to set.
   */
  public void setCategories(Set<Category> categories) {
    this.categories = categories;
  }
}
