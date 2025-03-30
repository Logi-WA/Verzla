package is.hi.verzla_backend.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * Represents an order placed by a user in the Verzla e-commerce system.
 * 
 * <p>This entity captures the details of a completed purchase transaction, including:
 * <ul>
 *   <li>The purchasing user's information</li>
 *   <li>When the order was placed</li>
 *   <li>The current processing status of the order</li>
 *   <li>All products purchased as part of this order</li>
 * </ul>
 * </p>
 * 
 * <p>Orders are created during checkout when the contents of a user's shopping cart
 * are converted into order items. The Order entity maintains a one-to-many relationship
 * with OrderItem entities, with each OrderItem representing a specific product purchased
 * and its quantity.</p>
 * 
 * <p>The status field tracks the order's progress through the fulfillment workflow,
 * with typical values including "Pending", "Processing", "Shipped", and "Delivered".</p>
 * 
 * @see is.hi.verzla_backend.entities.OrderItem
 * @see is.hi.verzla_backend.entities.User
 * @see is.hi.verzla_backend.controllers.OrderController
 */
@Entity
@Table(name = "orders")
public class Order {

  /**
   * The unique identifier of the order.
   * <p>
   * This ID is autogenerated as a UUID and serves as the primary key in the database.
   * </p>
   */
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  /**
   * The user who placed the order.
   * <p>
   * Cannot be null. Represents the customer who made the purchase.
   * </p>
   */
  @NotNull(message = "User cannot be null")
  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  /**
   * The date and time when the order was placed.
   * <p>
   * Cannot be null. Used for order history and fulfillment processing.
   * </p>
   */
  @NotNull(message = "Order date cannot be null")
  private Date orderDate;

  /**
   * The current status of the order in the fulfillment process.
   * <p>
   * Cannot be empty. Typical values include "Pending", "Processing", "Shipped", and "Delivered".
   * </p>
   */
  @NotEmpty(message = "Status cannot be empty")
  private String status;

  /**
   * The list of items included in this order.
   * <p>
   * Each order item represents a specific product, its quantity, and the price at the time of purchase.
   * This establishes a one-to-many relationship between orders and order items, with cascade operations
   * ensuring that order items are properly managed alongside their parent order.
   * </p>
   */
  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
  @JsonManagedReference
  private List<OrderItem> orderItems = new ArrayList<>();

  /**
   * Default constructor for JPA.
   */
  public Order() {}

  /**
   * Gets the ID of the order.
   *
   * @return the ID of the order.
   */
  public UUID getId() {
    return id;
  }

  /**
   * Sets the ID of the order.
   *
   * @param id the ID to set.
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Gets the user who placed the order.
   *
   * @return the user associated with the order.
   */
  public User getUser() {
    return user;
  }

  /**
   * Sets the user who placed the order.
   *
   * @param user the user to associate with the order.
   */
  public void setUser(User user) {
    this.user = user;
  }

  /**
   * Gets the date when the order was placed.
   *
   * @return the order date.
   */
  public Date getOrderDate() {
    return orderDate;
  }

  /**
   * Sets the date when the order was placed.
   *
   * @param orderDate the date to set.
   */
  public void setOrderDate(Date orderDate) {
    this.orderDate = orderDate;
  }

  /**
   * Gets the status of the order (e.g., "Pending", "Shipped", "Delivered").
   *
   * @return the status of the order.
   */
  public String getStatus() {
    return status;
  }

  /**
   * Sets the status of the order.
   *
   * @param status the status to set.
   */
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * Gets the list of items associated with the order.
   *
   * @return the list of order items.
   */
  public List<OrderItem> getOrderItems() {
    return orderItems;
  }

  /**
   * Sets the list of items associated with the order.
   * Automatically sets the reference to this order in each OrderItem.
   *
   * @param orderItems the list of order items to associate with this order.
   */
  public void setOrderItems(List<OrderItem> orderItems) {
    this.orderItems = orderItems;
    for (OrderItem item : orderItems) {
      item.setOrder(this);
    }
  }
}
