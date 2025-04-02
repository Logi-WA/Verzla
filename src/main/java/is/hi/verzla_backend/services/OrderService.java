package is.hi.verzla_backend.services;

import java.util.List;
import java.util.UUID;

import is.hi.verzla_backend.entities.Order;

/**
 * Service interface for managing orders.
 * Provides methods for retrieving, creating, and deleting orders.
 */
public interface OrderService {

    /**
     * Retrieves an order by its ID.
     *
     * @param id The ID of the order to retrieve.
     * @return The {@link Order} object with the specified ID.
     */
    Order getOrderById(UUID id);

    /**
     * Retrieves a list of orders associated with a specific user.
     *
     * @param userId The ID of the user whose orders are to be retrieved.
     * @return A list of {@link Order} objects associated with the user.
     */
    List<Order> getOrdersByUserId(UUID userId);

    /**
     * Creates a new order.
     *
     * @param order The {@link Order} object to be created.
     * @return The created {@link Order} object.
     */
    Order createOrder(Order order);

    /**
     * Deletes an order by its ID.
     *
     * @param id The ID of the order to be deleted.
     */
    void deleteOrder(UUID id);
}
