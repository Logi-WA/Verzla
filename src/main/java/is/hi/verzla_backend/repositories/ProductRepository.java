package is.hi.verzla_backend.repositories;

import is.hi.verzla_backend.entities.Product;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for performing CRUD operations on {@link Product} entities.
 * Provides methods to interact with the product data in the database.
 */
public interface ProductRepository extends JpaRepository<Product, UUID> {

  /**
   * Finds all products associated with a specific category by the category name.
   *
   * @param categoryName The name of the category whose products are to be fetched.
   * @return A list of {@link Product} entities belonging to the specified category.
   */
  List<Product> findByCategories_Name(String categoryName);
}
