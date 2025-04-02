package is.hi.verzla_backend.servicesimpl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import is.hi.verzla_backend.entities.Product;
import is.hi.verzla_backend.entities.Review;
import is.hi.verzla_backend.repositories.ProductRepository;
import is.hi.verzla_backend.repositories.ReviewRepository;
import is.hi.verzla_backend.services.ProductService;

/**
 * Implementation of the {@link ProductService} interface for managing products in the Verzla e-commerce system.
 *
 * <p>This service provides the implementation for all product-related operations including:
 * <ul>
 *   <li>Retrieving products (all or filtered by category)</li>
 *   <li>Finding specific products by ID</li>
 *   <li>Creating new products</li>
 *   <li>Updating existing product details (name, description, or both)</li>
 * </ul>
 * </p>
 *
 * <p>All operations are performed with transactional support to ensure data integrity,
 * meaning that database operations either complete fully or roll back entirely in case of errors.</p>
 *
 * @see is.hi.verzla_backend.services.ProductService
 * @see is.hi.verzla_backend.entities.Product
 * @see is.hi.verzla_backend.repositories.ProductRepository
 */
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    /**
     * Repository for product data access operations
     */
    @Autowired
    private ProductRepository productRepository;

    @Autowired(required = false) // Make it optional for now until Review parts are added
    private ReviewRepository reviewRepository;

    /**
     * Retrieves a list of products, optionally filtered by category.
     *
     * <p>This method returns either all products in the system or only those belonging
     * to a specified category, depending on whether the category parameter is provided.</p>
     *
     * <p>The filtering by category is handled at the repository level through a custom
     * query method that selects products based on their many-to-many relationship with categories.</p>
     *
     * @param category The name of the category to filter products by, or {@code null} to retrieve all products.
     * @return A list of {@link Product} objects, filtered by the specified category if provided.
     */
    @Override
    public List<Product> getProducts(String category) {
        if (category != null && !category.trim().isEmpty()) {
            return productRepository.findByCategory_NameIgnoreCase(category);
        }
        return productRepository.findAll();
    }

    /**
     * Retrieves a specific product by its unique identifier.
     *
     * <p>This method attempts to find a product with the given ID in the database.
     * If no such product exists, a runtime exception is thrown to indicate the failure.</p>
     *
     * @param id The UUID of the product to retrieve.
     * @return The {@link Product} with the specified ID.
     * @throws RuntimeException if the product with the specified ID is not found.
     */
    @Override
    public Product getProductById(UUID id) {
        return productRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + id));
    }

    /**
     * Creates a new product in the e-commerce system.
     *
     * <p>This method persists a new product entity to the database, including all its
     * associated attributes such as name, description, price, and categories.</p>
     *
     * <p>The product entity should be fully initialized with required fields before
     * being passed to this method. The method returns the saved entity with its
     * generated ID and any other default values set during persistence.</p>
     *
     * @param product The {@link Product} object to be created.
     * @return The created {@link Product} object with its generated ID.
     */
    @Override
    public Product createProduct(Product product) {
        // Basic save. Assumes categories/tags are handled correctly before calling.
        // Add validation or logic here if needed (e.g., ensure brand/tags not null if required)
        // Initial rating should be set by the populating script before calling this.
        return productRepository.save(product);
    }

    /**
     * Updates both the name and description of a specific product.
     *
     * <p>This method finds a product by its ID and updates both its name and description
     * with the provided values. If either parameter is null, that field remains unchanged.</p>
     *
     * <p>The method performs the update within a transaction, ensuring that the changes
     * are atomic and consistent.</p>
     *
     * @param id             The UUID of the product to update.
     * @param newName        The new name for the product, or null to leave unchanged.
     * @param newDescription The new description for the product, or null to leave unchanged.
     * @return The updated {@link Product} object.
     * @throws RuntimeException if the product with the specified ID is not found.
     */
    @Override
    public Product updateProduct(UUID id, String newName, String newDescription) {
        Product product = getProductById(id);
        if (newName != null) {
            product.setName(newName);
        }
        if (newDescription != null) {
            product.setDescription(newDescription);
        }
        return productRepository.save(product);
    }

    /**
     * Updates only the name of a specific product.
     *
     * <p>This method finds a product by its ID and updates its name with the provided value.
     * If the name parameter is null, no change is made to the product.</p>
     *
     * @param id      The UUID of the product to update.
     * @param newName The new name for the product, or null to leave unchanged.
     * @return The updated {@link Product} object.
     * @throws RuntimeException if the product with the specified ID is not found.
     */
    @Override
    public Product updateProductName(UUID id, String newName) {
        Product product = getProductById(id);
        if (newName != null) {
            product.setName(newName);
        }
        return productRepository.save(product);
    }

    /**
     * Updates only the description of a specific product.
     *
     * <p>This method finds a product by its ID and updates its description with the provided value.
     * If the description parameter is null, no change is made to the product.</p>
     *
     * @param id             The UUID of the product to update.
     * @param newDescription The new description for the product, or null to leave unchanged.
     * @return The updated {@link Product} object.
     * @throws RuntimeException if the product with the specified ID is not found.
     */
    @Override
    public Product updateProductDescription(UUID id, String newDescription) {
        Product product = getProductById(id);
        if (newDescription != null) {
            product.setDescription(newDescription);
        }
        return productRepository.save(product);
    }

    /**
     * Calculates and updates the average rating for the specified product.
     */
    @Override
    public void updateProductRating(UUID productId) {
        // Ensure ReviewRepository is available
        if (reviewRepository == null) {
            System.err.println("WARN: ReviewRepository not injected. Cannot update rating for product " + productId);
            return;
        }

        Product product = getProductById(productId); // Find the product or throw exception

        // Fetch all reviews for this product
        List<Review> reviews = reviewRepository.findByProduct_Id(productId);

        // Calculate the average rating
        double averageRating = reviews.stream()
                .filter(review -> review.getRating() != null) // Filter out null ratings
                .mapToInt(Review::getRating) // Map to integer ratings
                .average() // Calculate average
                .orElse(0.0); // Default to 0.0 if no reviews or all ratings are null

        // Round to 2 decimal places (optional but good practice)
        double roundedRating = Math.round(averageRating * 100.0) / 100.0;

        // Update the product's rating
        product.setRating(roundedRating);

        // Save the updated product
        productRepository.save(product);
        System.out.println("INFO: Updated rating for product " + productId + " to " + roundedRating);
    }
}
