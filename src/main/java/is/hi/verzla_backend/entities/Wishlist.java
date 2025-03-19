package is.hi.verzla_backend.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

/**
 * Represents a wishlist associated with a user, containing multiple wishlist
 * items.
 * <p>
 * Each {@code Wishlist} is linked to a single {@link User} and can contain
 * multiple
 * {@link WishlistItem} entries. This entity serves as a container for the
 * products
 * that a user wishes to purchase or save for later.
 * </p>
 *
 * <p>
 * The relationship between {@code Wishlist} and {@code WishlistItem} is
 * bidirectional,
 * where each wishlist item knows its parent wishlist.
 * </p>
 *
 * @see User
 * @see WishlistItem
 */
@Entity
@Table(name = "wishlists")
public class Wishlist {

  /**
   * The unique identifier for the wishlist.
   * <p>
   * This ID is autogenerated and serves as the primary key in the database.
   * </p>
   */
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  /**
   * The user associated with this wishlist.
   * <p>
   * Cannot be null. Represents the owner of the wishlist.
   * </p>
   */
  @NotNull(message = "User cannot be null")
  @OneToOne
  @JoinColumn(name = "user_id")
  @JsonBackReference
  private User user;

  /**
   * The list of items in this wishlist.
   * <p>
   * Represents the products that the user has added to their wishlist.
   * Cascade operations ensure that wishlist items are persisted and removed
   * alongside their parent wishlist.
   * </p>
   */
  @OneToMany(mappedBy = "wishlist", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonIgnore
  private List<WishlistItem> wishlistItems = new ArrayList<>();

  /**
   * Default constructor for JPA.
   */
  public Wishlist() {
  }

  /**
   * Retrieves the unique identifier of this wishlist.
   *
   * @return the ID of the wishlist
   */
  public UUID getId() {
    return id;
  }

  /**
   * Sets the unique identifier of this wishlist.
   *
   * @param id the ID to set
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Retrieves the user associated with this wishlist.
   *
   * @return the user associated with the wishlist
   */
  public User getUser() {
    return user;
  }

  /**
   * Associates a user with this wishlist.
   *
   * @param user the user to associate
   */
  public void setUser(User user) {
    this.user = user;
  }

  /**
   * Retrieves the list of items in this wishlist.
   *
   * @return a list of {@link WishlistItem} objects
   */
  public List<WishlistItem> getWishlistItems() {
    return wishlistItems;
  }

  /**
   * Sets the list of items in this wishlist.
   *
   * @param wishlistItems the list of {@link WishlistItem} objects to set
   */
  public void setWishlistItems(List<WishlistItem> wishlistItems) {
    this.wishlistItems = wishlistItems;
  }
}
