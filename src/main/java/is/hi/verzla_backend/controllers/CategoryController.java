package is.hi.verzla_backend.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import is.hi.verzla_backend.dto.ApiResponse;
import is.hi.verzla_backend.entities.Category;
import is.hi.verzla_backend.services.CategoryService;

/**
 * REST controller for managing product category operations in the Verzla e-commerce system.
 * <p>
 * This controller handles HTTP requests mapped to {@code /api/categories} and provides
 * endpoints for retrieving product category information. It serves as the API interface
 * for accessing the product categorization system, which is essential for organizing
 * products and enabling users to browse the product catalog efficiently.
 * </p>
 * 
 * <p>
 * Product categories are a fundamental aspect of any e-commerce platform as they:
 * <ul>
 *   <li>Allow users to navigate and discover products based on their interests</li>
 *   <li>Help organize the product catalog in a structured manner</li>
 *   <li>Support filtering and refining product searches</li>
 *   <li>Enable product tagging and classification for marketing purposes</li>
 * </ul>
 * </p>
 */
@RestController
@RequestMapping("/api")
public class CategoryController {

  @Autowired
  private CategoryService categoryService;

  /**
   * Retrieves all product categories available in the system.
   * <p>
   * This endpoint returns all product categories that can be associated with products.
   * The categories are used throughout the application for:
   * <ul>
   *   <li>Building navigation menus in the storefront</li>
   *   <li>Filtering product listings by category</li>
   *   <li>Categorizing new and existing products</li>
   *   <li>Organizing the product catalog</li>
   * </ul>
   * </p>
   *
   * @return ResponseEntity containing a list of Category objects representing all
   *         available product categories in the system
   */
  @GetMapping("/categories")
  public ResponseEntity<List<Category>> getCategories() {
    List<Category> categories = categoryService.getAllCategoriesSorted();
    return ResponseEntity.ok(categories);
  }
  
  /**
   * Retrieves a specific category by its ID.
   *
   * @param categoryId The UUID of the category to retrieve
   * @return ResponseEntity containing the category if found, or not found status
   */
  @GetMapping("/categories/{categoryId}")
  public ResponseEntity<?> getCategoryById(@PathVariable UUID categoryId) {
    Optional<Category> category = categoryService.getCategoryById(categoryId);
    return category.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
  }
  
  /**
   * Retrieves a category by name.
   *
   * @param name The name of the category to retrieve
   * @return ResponseEntity containing the category if found, or not found status
   */
  @GetMapping("/categories/name/{name}")
  public ResponseEntity<?> getCategoryByName(@PathVariable String name) {
    Category category = categoryService.getCategoryByName(name);
    if (category != null) {
      return ResponseEntity.ok(category);
    }
    return ResponseEntity.notFound().build();
  }
  
  /**
   * Creates a new category.
   *
   * @param category The category to create
   * @return ResponseEntity containing the created category
   */
  @PostMapping("/categories")
  public ResponseEntity<?> createCategory(@RequestBody Category category) {
    if (categoryService.existsByName(category.getName())) {
      return ResponseEntity
              .status(HttpStatus.CONFLICT)
              .body(new ApiResponse(false, "Category with this name already exists"));
    }
    Category createdCategory = categoryService.createCategory(category);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
  }
  
  /**
   * Updates an existing category.
   *
   * @param categoryId The UUID of the category to update
   * @param category The updated category details
   * @return ResponseEntity containing the updated category, or appropriate error status
   */
  @PutMapping("/categories/{categoryId}")
  public ResponseEntity<?> updateCategory(@PathVariable UUID categoryId, @RequestBody Category category) {
    // Check if another category already has this name
    Category existingWithName = categoryService.getCategoryByName(category.getName());
    if (existingWithName != null && !existingWithName.getId().equals(categoryId)) {
      return ResponseEntity
              .status(HttpStatus.CONFLICT)
              .body(new ApiResponse(false, "Another category with this name already exists"));
    }
    
    Category updatedCategory = categoryService.updateCategory(categoryId, category);
    if (updatedCategory == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(updatedCategory);
  }
  
  /**
   * Deletes a category.
   *
   * @param categoryId The UUID of the category to delete
   * @return ResponseEntity with success or error status
   */
  @DeleteMapping("/categories/{categoryId}")
  public ResponseEntity<?> deleteCategory(@PathVariable UUID categoryId) {
    // Check if category has products
    long productCount = categoryService.getProductCountInCategory(categoryId);
    if (productCount > 0) {
      return ResponseEntity
              .status(HttpStatus.CONFLICT)
              .body(new ApiResponse(false, "Category cannot be deleted because it contains products"));
    }
    
    boolean deleted = categoryService.deleteCategory(categoryId);
    if (!deleted) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(new ApiResponse(true, "Category deleted successfully"));
  }
  
  /**
   * Retrieves the number of products in a category.
   *
   * @param categoryId The UUID of the category
   * @return ResponseEntity containing the product count, or not found status
   */
  @GetMapping("/categories/{categoryId}/product-count")
  public ResponseEntity<?> getProductCount(@PathVariable UUID categoryId) {
    if (!categoryService.existsById(categoryId)) {
      return ResponseEntity.notFound().build();
    }
    
    long count = categoryService.getProductCountInCategory(categoryId);
    Map<String, Object> response = new HashMap<>();
    response.put("categoryId", categoryId);
    response.put("productCount", count);
    
    return ResponseEntity.ok(response);
  }
  
  /**
   * Retrieves all categories that a specific product belongs to.
   *
   * @param productId The UUID of the product
   * @return ResponseEntity containing a list of categories
   */
  @GetMapping("/products/{productId}/categories")
  public ResponseEntity<List<Category>> getCategoriesByProduct(@PathVariable UUID productId) {
    List<Category> categories = categoryService.getCategoriesByProductId(productId);
    return ResponseEntity.ok(categories);
  }
}
