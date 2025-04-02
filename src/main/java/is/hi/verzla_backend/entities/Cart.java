package is.hi.verzla_backend.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
 * Represents a shopping cart associated with a specific user.
 * <p>
 * Each {@code Cart} is linked to a single {@link User} and can contain multiple
 * {@link CartItem} entries. This entity serves as a container for the products
 * that a user intends to purchase.
 * </p>
 *
 * <p>
 * The relationship between {@code Cart} and {@code CartItem} is bidirectional,
 * where each cart item knows its parent cart.
 * </p>
 *
 * @see User
 * @see CartItem
 */
@Entity
@Table(name = "carts")
public class Cart {

    /**
     * The unique identifier for the cart.
     * <p>
     * This ID is autogenerated and serves as the primary key in the database.
     * </p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /**
     * The user associated with this cart.
     * <p>
     * Cannot be null. Represents the owner of the cart.
     * </p>
     */
    @NotNull(message = "User cannot be null")
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    /**
     * The list of items in this cart.
     * <p>
     * Represents the products that the user has added to their cart.
     * Cascade operations ensure that cart items are persisted and removed
     * alongside their parent cart.
     * </p>
     */
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<CartItem> cartItems = new ArrayList<>();

    /**
     * Default constructor for creating a new Cart instance.
     */
    public Cart() {
    }

    /**
     * Retrieves the unique identifier of this cart.
     *
     * @return the ID of the cart
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets the unique identifier of this cart.
     *
     * @param id the ID to set
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Retrieves the user associated with this cart.
     *
     * @return the user associated with the cart
     */
    public User getUser() {
        return user;
    }

    /**
     * Associates a user with this cart.
     *
     * @param user the user to associate
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Retrieves the list of items in this cart.
     *
     * @return a list of {@link CartItem} objects
     */
    public List<CartItem> getCartItems() {
        return cartItems;
    }

    /**
     * Sets the list of items in this cart.
     *
     * @param cartItems the list of {@link CartItem} objects to set
     */
    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }
}
