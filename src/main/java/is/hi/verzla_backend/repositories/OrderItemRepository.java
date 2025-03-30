package is.hi.verzla_backend.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import is.hi.verzla_backend.entities.Order;
import is.hi.verzla_backend.entities.OrderItem;

/**
 * Repository interface for performing CRUD operations on {@link OrderItem} entities.
 * 
 * <p>This repository provides methods to interact with order items in the database,
 * supporting the order management functionality of the Verzla e-commerce platform.
 * It extends JpaRepository to inherit standard data access methods while adding
 * custom query methods tailored to order item requirements.</p>
 * 
 * <p>OrderItems represent individual product line items within an order, capturing
 * crucial information such as:
 * <ul>
 *   <li>Which product was purchased</li>
 *   <li>The quantity of the product</li>
 *   <li>The price at the time of purchase (which may differ from current product price)</li>
 *   <li>The parent order containing this item</li>
 * </ul>
 * </p>
 * 
 * @see is.hi.verzla_backend.entities.OrderItem
 * @see is.hi.verzla_backend.entities.Order
 * @see is.hi.verzla_backend.entities.Product
 */
public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
  
  /**
   * Finds all order items that belong to a specific order.
   * 
   * <p>This method retrieves the complete list of items contained within a given order,
   * allowing for detailed order information display and order history functionality.
   * The items are typically sorted in the order they were added to the order.</p>
   *
   * @param order The {@link Order} whose items should be retrieved
   * @return A list of {@link OrderItem} entities belonging to the specified order
   * @throws IllegalArgumentException if order is null
   */
  List<OrderItem> findByOrder(Order order);
}