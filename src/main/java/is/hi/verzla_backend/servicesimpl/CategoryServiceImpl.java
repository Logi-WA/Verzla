package is.hi.verzla_backend.servicesimpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import is.hi.verzla_backend.entities.Category;
import is.hi.verzla_backend.exceptions.ResourceNotFoundException;
import is.hi.verzla_backend.repositories.CategoryRepository;
import is.hi.verzla_backend.repositories.ProductRepository;
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

    @Autowired
    private ProductRepository productRepository;

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

    @Override
    @Transactional(readOnly = true)
    public Optional<Category> getCategoryByName(String name) { // Return Optional
        return categoryRepository.findByName(name); // Assumes repo method returns Optional
    }

    @Override
    public Category createCategory(Category category) {
        categoryRepository.findByName(category.getName()).ifPresent(existing -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Category with name '" + category.getName() + "' already exists.");
        });
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(UUID categoryId, Category categoryDetails) {
        categoryRepository.findByName(categoryDetails.getName()).ifPresent(existing -> {
            if (!existing.getId().equals(categoryId)) { // If the found category is not the one we are updating
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Another category with name '" + categoryDetails.getName() + "' already exists.");
            }
        });

        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));

        existingCategory.setName(categoryDetails.getName());
        return categoryRepository.save(existingCategory);
    }

    @Override
    public boolean deleteCategory(UUID categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));

        // Check if category has products
        long productCount = categoryRepository.countProductsByCategoryId(categoryId);
        if (productCount > 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Category cannot be deleted because it contains products.");
        }

        categoryRepository.delete(category);
        return true; // Return void or true, void is often better for delete operations
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

}
