package is.hi.verzla_backend.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import is.hi.verzla_backend.entities.Product;

/**
 * Repository interface for performing CRUD operations on {@link Product} entities.
 * 
 * <p>This repository provides methods to interact with product data in the database,
 * forming the backbone of the product catalog in the Verzla e-commerce platform. It
 * extends JpaRepository to inherit standard data access methods while adding custom
 * query methods specific to product management requirements.</p>
 * 
 * <p>Key features of this repository include:
 * <ul>
 *   <li>Retrieving all products in the catalog</li>
 *   <li>Finding products by ID for detailed product pages</li>
 *   <li>Filtering products by category for catalog browsing</li>
 *   <li>Searching for products by name</li>
 *   <li>Managing product creation, updates, and removals</li>
 * </ul>
 * </p>
 * 
 * <p>This repository is central to many of the e-commerce operations, including
 * browsing the product catalog, managing shopping carts, and processing orders.</p>
 * 
 * @see is.hi.verzla_backend.entities.Product
 * @see is.hi.verzla_backend.entities.Category
 * @see is.hi.verzla_backend.controllers.ProductController
 */
public interface ProductRepository extends JpaRepository<Product, UUID> {

  /**
   * Finds all products associated with a specific category by the category name.
   * 
   * <p>This method supports the category-based browsing feature of the e-commerce
   * platform, allowing users to view all products within a particular category.
   * It leverages the many-to-many relationship between products and categories
   * to filter the product catalog.</p>
   *
   * @param categoryName The name of the category whose products are to be fetched
   * @return A list of {@link Product} entities belonging to the specified category
   * @throws IllegalArgumentException if categoryName is null
   */
  List<Product> findByCategories_Name(String categoryName);

  /**
   * Finds a product by its name.
   * 
   * <p>This method is used in administrative functions and during data imports
   * to locate specific products by their name. It's also useful for ensuring
   * uniqueness of product names in the catalog.</p>
   * 
   * <p>Note that since product names are not guaranteed to be unique in all
   * implementations, this method may return any matching product if multiple
   * products share the same name.</p>
   *
   * @param name The name of the product to find
   * @return The {@link Product} entity associated with the specified name, or null if not found
   * @throws IllegalArgumentException if name is null
   */
  Product findByName(String name);
}
