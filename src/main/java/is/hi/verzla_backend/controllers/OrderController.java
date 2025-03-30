package is.hi.verzla_backend.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import is.hi.verzla_backend.entities.Order;
import is.hi.verzla_backend.services.OrderService;

/**
 * REST controller for managing order-related operations within the Verzla e-commerce system.
 * <p>
 * This controller handles HTTP requests mapped to {@code /api/orders} and provides
 * endpoints for retrieving order information. It interacts with the {@link OrderService}
 * to access and manipulate {@link Order} data.
 * </p>
 * 
 * <p>
 * Currently supported operations include:
 * <ul>
 *   <li>Retrieving order details by ID</li>
 * </ul>
 * </p>
 * 
 * <p>
 * This controller is part of the order management subsystem and serves as the API
 * interface for clients needing to access order information. It is typically used
 * by the frontend to display order details, order history, and order status information.
 * </p>
 * 
 * <p>
 * Future enhancements may include endpoints for:
 * <ul>
 *   <li>Listing all orders for an authenticated user</li>
 *   <li>Canceling orders</li>
 *   <li>Updating order status</li>
 *   <li>Generating order reports</li>
 * </ul>
 * </p>
 * 
 * @see OrderService
 * @see Order
 * @see is.hi.verzla_backend.entities.OrderItem
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

  /**
   * Service layer for handling order-related business logic.
   * <p>
   * This service is responsible for retrieving, creating, and modifying orders
   * and their associated items. It encapsulates the business rules and database
   * interactions required for order management.
   * </p>
   */
  @Autowired
  private OrderService orderService;

  /**
   * Retrieves the details of an order by its ID.
   * <p>
   * This endpoint fetches a complete order record, including all order items,
   * the customer information, order date, status, and other related details.
   * It is typically used in order detail pages and order history displays.
   * </p>
   *
   * @param id The unique identifier of the order to retrieve
   * @return The {@link Order} object with the specified ID, including all associated 
   *         order items and customer information
   * 
   * @throws is.hi.verzla_backend.exceptions.ResourceNotFoundException if no order with the given ID exists
   */
  @GetMapping("/{id}")
  public Order getOrderDetails(@PathVariable UUID id) {
    return orderService.getOrderById(id);
  }
}
