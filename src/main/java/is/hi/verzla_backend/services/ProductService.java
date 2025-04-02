package is.hi.verzla_backend.services;

import java.util.List;
import java.util.UUID;

import is.hi.verzla_backend.entities.Product;

/**
 * Service interface for managing products.
 * Provides methods to retrieve, create, and update product details.
 */
public interface ProductService {

    /**
     * Retrieves a list of products, optionally filtered by category.
     *
     * @param category The name of the category to filter by (optional).
     *                 If null, all products are retrieved.
     * @return A list of {@link Product} objects.
     */
    List<Product> getProducts(String category);

    /**
     * Retrieves a product by its ID.
     *
     * @param id The ID of the product to retrieve.
     * @return The {@link Product} object with the specified ID.
     */
    Product getProductById(UUID id);

    /**
     * Creates a new product.
     *
     * @param product The {@link Product} object to be created.
     * @return The created {@link Product} object.
     */
    Product createProduct(Product product);

    /**
     * Updates both the name and description of a specific product by its ID.
     *
     * @param id             The ID of the product to update.
     * @param newName        The new name for the product.
     * @param newDescription The new description for the product.
     * @return The updated {@link Product} object.
     */
    Product updateProduct(UUID id, String newName, String newDescription);

    /**
     * Updates the name of a specific product by its ID.
     *
     * @param id      The ID of the product to update.
     * @param newName The new name for the product.
     * @return The updated {@link Product} object.
     */
    Product updateProductName(UUID id, String newName);

    /**
     * Updates the description of a specific product by its ID.
     *
     * @param id             The ID of the product to update.
     * @param newDescription The new description for the product.
     * @return The updated {@link Product} object.
     */
    Product updateProductDescription(UUID id, String newDescription);

    /**
     * Calculates and updates the average rating for a given product based on its reviews.
     * Should be called whenever a review for the product is added, updated (rating changed), or deleted.
     *
     * @param productId The ID of the product whose rating needs updating.
     * @throws RuntimeException if the product is not found.
     */
    void updateProductRating(UUID productId);
}
