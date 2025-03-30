package is.hi.verzla_backend.servicesimpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import is.hi.verzla_backend.entities.Category;
import is.hi.verzla_backend.repositories.CategoryRepository;
import is.hi.verzla_backend.services.CategoryService;

/**
 * Implementation of the {@link CategoryService} interface. Provides methods for managing
 * categories, including retrieving, creating, updating, and deleting categories.
 */
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

  @Autowired
  private CategoryRepository categoryRepository;

  /**
   * Retrieves a list of all categories.
   *
   * @return A list of {@link Category} objects.
   */
  @Override
  @Transactional(readOnly = true)
  public List<Category> getAllCategories() {
    return categoryRepository.findAll();
  }
  
  /**
   * Retrieves a list of all categories sorted by name.
   *
   * @return A list of {@link Category} objects sorted alphabetically.
   */
  @Override
  @Transactional(readOnly = true)
  public List<Category> getAllCategoriesSorted() {
    return categoryRepository.findAllByOrderByNameAsc();
  }
  
  /**
   * Retrieves a specific category by its ID.
   *
   * @param categoryId The UUID of the category to retrieve
   * @return An Optional containing the category if found, or empty if not found
   */
  @Override
  @Transactional(readOnly = true)
  public Optional<Category> getCategoryById(UUID categoryId) {
    return categoryRepository.findById(categoryId);
  }
  
  /**
   * Retrieves a specific category by its name.
   *
   * @param name The name of the category to retrieve
   * @return The category if found, or null if not found
   */
  @Override
  @Transactional(readOnly = true)
  public Category getCategoryByName(String name) {
    return categoryRepository.findByName(name);
  }
  
  /**
   * Creates a new product category.
   *
   * @param category The category to create
   * @return The created category with its generated ID
   */
  @Override
  public Category createCategory(Category category) {
    return categoryRepository.save(category);
  }
  
  /**
   * Updates an existing product category.
   *
   * @param categoryId The UUID of the category to update
   * @param categoryDetails The updated category details
   * @return The updated category, or null if the category was not found
   */
  @Override
  public Category updateCategory(UUID categoryId, Category categoryDetails) {
    Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
    
    if (categoryOptional.isPresent()) {
      Category existingCategory = categoryOptional.get();
      existingCategory.setName(categoryDetails.getName());
      return categoryRepository.save(existingCategory);
    }
    
    return null;
  }
  
  /**
   * Deletes a product category.
   *
   * @param categoryId The UUID of the category to delete
   * @return true if deletion was successful, false otherwise
   */
  @Override
  public boolean deleteCategory(UUID categoryId) {
    if (categoryRepository.existsById(categoryId)) {
      categoryRepository.deleteById(categoryId);
      return true;
    }
    return false;
  }
  
  /**
   * Checks if a category exists by ID.
   *
   * @param categoryId The UUID of the category to check
   * @return true if the category exists, false otherwise
   */
  @Override
  @Transactional(readOnly = true)
  public boolean existsById(UUID categoryId) {
    return categoryRepository.existsById(categoryId);
  }
  
  /**
   * Checks if a category exists by name.
   *
   * @param name The name of the category to check
   * @return true if the category exists, false otherwise
   */
  @Override
  @Transactional(readOnly = true)
  public boolean existsByName(String name) {
    return categoryRepository.existsByName(name);
  }
  
  /**
   * Gets the number of products in a category.
   *
   * @param categoryId The UUID of the category
   * @return The number of products in the category
   */
  @Override
  @Transactional(readOnly = true)
  public long getProductCountInCategory(UUID categoryId) {
    return categoryRepository.countProductsByCategoryId(categoryId);
  }
  
  /**
   * Gets all categories associated with a specific product.
   *
   * @param productId The UUID of the product
   * @return A list of categories the product belongs to
   */
  @Override
  @Transactional(readOnly = true)
  public List<Category> getCategoriesByProductId(UUID productId) {
    return categoryRepository.findCategoriesByProductId(productId);
  }
}
