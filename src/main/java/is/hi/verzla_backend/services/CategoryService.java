package is.hi.verzla_backend.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import is.hi.verzla_backend.entities.Category;

/**
 * Service interface for handling operations related to product categories.
 * Provides methods to retrieve, create, update, and delete categories,
 * as well as manage their relationships with products.
 */
public interface CategoryService {

  /**
   * Retrieves a list of all available product categories.
   *
   * @return A list of {@link Category} objects.
   */
  List<Category> getAllCategories();
  
  /**
   * Retrieves a list of all available product categories sorted by name.
   *
   * @return A list of {@link Category} objects in alphabetical order.
   */
  List<Category> getAllCategoriesSorted();
  
  /**
   * Retrieves a specific category by its ID.
   *
   * @param categoryId The UUID of the category to retrieve
   * @return An Optional containing the category if found, or empty if not found
   */
  Optional<Category> getCategoryById(UUID categoryId);
  
  /**
   * Retrieves a specific category by its name.
   *
   * @param name The name of the category to retrieve
   * @return The category if found, or null if not found
   */
  Category getCategoryByName(String name);
  
  /**
   * Creates a new product category.
   *
   * @param category The category to create
   * @return The created category with its generated ID
   */
  Category createCategory(Category category);
  
  /**
   * Updates an existing product category.
   *
   * @param categoryId The UUID of the category to update
   * @param categoryDetails The updated category details
   * @return The updated category, or null if the category was not found
   */
  Category updateCategory(UUID categoryId, Category categoryDetails);
  
  /**
   * Deletes a product category.
   *
   * @param categoryId The UUID of the category to delete
   * @return true if deletion was successful, false otherwise
   */
  boolean deleteCategory(UUID categoryId);
  
  /**
   * Checks if a category exists by ID.
   *
   * @param categoryId The UUID of the category to check
   * @return true if the category exists, false otherwise
   */
  boolean existsById(UUID categoryId);
  
  /**
   * Checks if a category exists by name.
   *
   * @param name The name of the category to check
   * @return true if the category exists, false otherwise
   */
  boolean existsByName(String name);
  
  /**
   * Gets the number of products in a category.
   *
   * @param categoryId The UUID of the category
   * @return The number of products in the category
   */
  long getProductCountInCategory(UUID categoryId);
  
  /**
   * Gets all categories associated with a specific product.
   *
   * @param productId The UUID of the product
   * @return A list of categories the product belongs to
   */
  List<Category> getCategoriesByProductId(UUID productId);
}
