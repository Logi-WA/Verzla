package is.hi.verzla_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import is.hi.verzla_backend.entities.Category;

/**
 * Repository interface for performing CRUD operations on {@link Category} entities.
 *
 * <p>This repository provides methods to interact with product categories in the database,
 * supporting the product categorization and browsing functionality of the Verzla e-commerce
 * platform. It extends JpaRepository to inherit standard data access methods while adding
 * custom query methods tailored to category management requirements.</p>
 *
 * <p>Categories are a fundamental component of the product organization system, allowing:
 * <ul>
 *   <li>Logical grouping of related products</li>
 *   <li>Hierarchical navigation of the product catalog</li>
 *   <li>Filtering products during search and browse operations</li>
 *   <li>Merchandising and promotional activities based on product types</li>
 * </ul>
 * </p>
 *
 * <p>This repository works in conjunction with {@link ProductRepository} to support
 * the many-to-many relationship between products and categories, allowing products
 * to belong to multiple categories simultaneously.</p>
 *
 * @see is.hi.verzla_backend.entities.Category
 * @see is.hi.verzla_backend.entities.Product
 * @see is.hi.verzla_backend.controllers.CategoryController
 */
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    /**
     * Finds a category by its name.
     *
     * <p>This method supports several important e-commerce operations:
     * <ul>
     *   <li>Retrieving a specific category for product filtering</li>
     *   <li>Validating category names during product creation and updates</li>
     *   <li>Finding or creating categories during product imports</li>
     *   <li>Building navigation menus of available categories</li>
     * </ul>
     * </p>
     *
     * @param name The name of the category to find
     * @return The {@link Category} entity associated with the specified name, or null if not found
     * @throws IllegalArgumentException if name is null
     */
    Optional<Category> findByName(String name);

    /**
     * Checks if a category with the given name exists.
     *
     * @param name The name to check
     * @return true if a category with the name exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Retrieves all categories ordered by name alphabetically.
     *
     * @return List of categories ordered by name
     */
    List<Category> findAllByOrderByNameAsc();

    @Query("SELECT COUNT(p) FROM Product p WHERE p.category.id = :categoryId")
    long countProductsByCategoryId(@Param("categoryId") UUID categoryId);
}
