package is.hi.verzla.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

/**
 * Represents an item within a user's wishlist, linking a specific product to a
 * particular wishlist and associating it with a user.
 * <p>
 * This entity serves as a join table between {@link Product}, {@link Wishlist},
 * and {@link User}, enabling users to add products to their wishlists.
 * </p>
 *
 * <p>
 * Each {@code WishlistItem} must be associated with exactly one
 * {@code Product}, one {@code Wishlist}, and one {@code User}. The combination
 * ensures that the product is uniquely identified within the context of a
 * user's wishlist.
 * </p>
 *
 * @see Product
 * @see Wishlist
 * @see User
 */
@Entity
@Table(name = "wishlist_items")
public class WishlistItem {

  /**
   * The unique identifier for the wishlist item.
   * <p>
   * This ID is autogenerated and serves as the primary key in the database.
   * </p>
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * The product associated with this wishlist item.
   * <p>
   * Cannot be null. Represents the specific product that the user has added
   * to their wishlist.
   * </p>
   */
  @NotNull(message = "Product cannot be null")
  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;

  /**
   * The wishlist to which this item belongs.
   * <p>
   * Cannot be null. Represents the specific wishlist that the user has created.
   * </p>
   */
  @NotNull(message = "Wishlist cannot be null")
  @ManyToOne
  @JoinColumn(name = "wishlist_id")
  private Wishlist wishlist;

  /**
   * The user who owns this wishlist item.
   * <p>
   * Cannot be null. Links the wishlist item to the user who added the product
   * to their wishlist.
   * </p>
   */
  @NotNull(message = "User cannot be null")
  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  /**
   * Default constructor for JPA.
   */
  public WishlistItem() {
  }

  /**
   * Retrieves the unique identifier of this wishlist item.
   *
   * @return the ID of the wishlist item
   */
  public Long getId() {
    return id;
  }

  /**
   * Sets the unique identifier of this wishlist item.
   *
   * @param id the ID to set
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Retrieves the product associated with this wishlist item.
   *
   * @return the product associated with this wishlist item
   */
  public Product getProduct() {
    return product;
  }

  /**
   * Associates a product with this wishlist item.
   *
   * @param product the product to associate
   */
  public void setProduct(Product product) {
    this.product = product;
  }

  /**
   * Retrieves the wishlist to which this item belongs.
   *
   * @return the wishlist associated with this item
   */
  public Wishlist getWishlist() {
    return wishlist;
  }

  /**
   * Associates this item with a specific wishlist.
   *
   * @param wishlist the wishlist to associate
   */
  public void setWishlist(Wishlist wishlist) {
    this.wishlist = wishlist;
  }

  /**
   * Retrieves the user who owns this wishlist item.
   *
   * @return the user who owns this wishlist item
   */
  public User getUser() {
    return user;
  }

  /**
   * Associates this wishlist item with a specific user.
   *
   * @param user the user to associate
   */
  public void setUser(User user) {
    this.user = user;
  }
}
