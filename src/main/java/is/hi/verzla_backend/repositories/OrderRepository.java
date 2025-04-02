package is.hi.verzla_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

import is.hi.verzla_backend.entities.Order;

/**
 * Repository interface for performing CRUD operations on {@link Order} entities.
 *
 * <p>This repository provides methods to interact with order data in the database,
 * supporting the order management functionality of the Verzla e-commerce platform.
 * It extends JpaRepository to inherit standard data access methods while adding
 * custom query methods tailored to order processing needs.</p>
 *
 * <p>Key functionality includes:
 * <ul>
 *   <li>Creating new orders when users complete checkout</li>
 *   <li>Retrieving order details for order confirmation and history</li>
 *   <li>Fetching a user's order history</li>
 *   <li>Updating order status as orders are processed</li>
 * </ul>
 * </p>
 *
 * <p>Orders represent completed transactions in the e-commerce system and contain
 * vital information about purchases, including the items ordered, the purchasing user,
 * order date, and current status.</p>
 *
 * @see is.hi.verzla_backend.entities.Order
 * @see is.hi.verzla_backend.entities.OrderItem
 * @see is.hi.verzla_backend.controllers.OrderController
 */
public interface OrderRepository extends JpaRepository<Order, UUID> {

    /**
     * Finds all orders associated with a specific user by their user ID.
     *
     * <p>This method is crucial for displaying order history to users, allowing them
     * to view their past purchases. It's used in the user dashboard to present a
     * chronological list of orders placed by the user.</p>
     *
     * @param userId The ID of the user whose orders are to be fetched
     * @return A list of {@link Order} entities belonging to the specified user,
     * typically sorted by order date with the most recent orders first
     * @throws IllegalArgumentException if userId is null
     */
    List<Order> findByUserId(UUID userId);
}
