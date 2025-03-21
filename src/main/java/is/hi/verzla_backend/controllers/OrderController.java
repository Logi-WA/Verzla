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
 * REST controller for managing orders.
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

  @Autowired
  private OrderService orderService;

  /**
   * Retrieves the details of an order by its ID.
   *
   * @param id The ID of the order to retrieve.
   * @return The Order object with the specified ID.
   */
  @GetMapping("/{id}")
  public Order getOrderDetails(@PathVariable UUID id) {
    return orderService.getOrderById(id);
  }
}
