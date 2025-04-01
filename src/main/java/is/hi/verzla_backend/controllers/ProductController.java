package is.hi.verzla_backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import is.hi.verzla_backend.dto.ApiResponse;
import is.hi.verzla_backend.dto.ProductDto;
import is.hi.verzla_backend.entities.Product;
import is.hi.verzla_backend.services.ProductService;

/**
 * REST controller for managing products in the Verzla e-commerce application.
 *
 * <p>This controller provides endpoints for product management operations including:
 * <ul>
 *   <li>Retrieving all products or filtering by category</li>
 *   <li>Retrieving specific products by ID</li>
 *   <li>Creating new products</li>
 *   <li>Updating product details (name, description, or both)</li>
 * </ul>
 * </p>
 *
 * <p>The controller converts between Product entities and ProductDto objects
 * to provide a consistent API response format and protect internal implementation
 * details.</p>
 *
 * @see is.hi.verzla_backend.entities.Product
 * @see is.hi.verzla_backend.dto.ProductDto
 * @see is.hi.verzla_backend.services.ProductService
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    /**
     * Service responsible for product business logic
     */
    @Autowired
    private ProductService productService;

    /**
     * Retrieves a list of all products, with optional filtering by category.
     *
     * <p>This endpoint returns all products in the system, optionally filtered
     * by a specific category. The response includes product details like ID,
     * name, price, description, image URL, and categories.</p>
     *
     * @param category Optional category filter to retrieve products by specific category.
     * @return A ResponseEntity containing a list of products, filtered by category if provided.
     * @apiNote Example usage: GET /api/products?category=Books
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductDto>>> getProducts(
            @RequestParam(required = false) String category
    ) {
        List<Product> products = productService.getProducts(category);
        List<ProductDto> productDtos = products.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success(productDtos));
    }

    /**
     * Retrieves a specific product by its unique identifier.
     *
     * <p>This endpoint fetches detailed information about a specific product
     * identified by its UUID. If the product is found, its complete details are
     * returned. If not found, a 404 Not Found response is returned.</p>
     *
     * @param id The UUID of the product to retrieve.
     * @return A ResponseEntity containing the product details or a 404 if not found.
     * @apiNote Example usage: GET /api/products/550e8400-e29b-41d4-a716-446655440000
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDto>> getProductById(@PathVariable UUID id) {
        try {
            Product product = productService.getProductById(id);
            return ResponseEntity.ok(ApiResponse.success(convertToDto(product)));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Creates a new product in the system.
     *
     * <p>This endpoint accepts a product object with details such as name,
     * price, description, and categories, and persists it in the database.
     * The created product is returned with its generated ID.</p>
     *
     * @param product The Product object to be created with all required fields.
     * @return The created Product object with its generated ID.
     * @apiNote This endpoint typically requires administrative privileges.
     */
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    /**
     * Updates both the name and description of a specific product by its ID.
     *
     * @param id      The ID of the product to update.
     * @param updates A map containing the new name and description.
     * @return Response entity with the updated product or error message.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable UUID id,
            @RequestBody Map<String, String> updates
    ) {
        try {
            String newName = updates.get("name");
            String newDescription = updates.get("description");
            Product updatedProduct = productService.updateProduct(id, newName, newDescription);
            return ResponseEntity.ok(updatedProduct);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update product.");
        }
    }

    /**
     * Updates the name of a specific product by its ID.
     *
     * @param id      The ID of the product to update.
     * @param updates A map containing the new name.
     * @return Response entity with the updated product or error message.
     */
    @PatchMapping("/{id}/name")
    public ResponseEntity<?> updateProductName(
            @PathVariable UUID id,
            @RequestBody Map<String, String> updates
    ) {
        try {
            String newName = updates.get("name");
            Product updatedProduct = productService.updateProductName(id, newName);
            return ResponseEntity.ok(updatedProduct);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update product name.");
        }
    }

    /**
     * Updates the description of a specific product by its ID.
     *
     * @param id      The ID of the product to update.
     * @param updates A map containing the new description.
     * @return Response entity with the updated product or error message.
     */
    @PatchMapping("/{id}/description")
    public ResponseEntity<?> updateProductDescription(
            @PathVariable UUID id,
            @RequestBody Map<String, String> updates
    ) {
        try {
            String newDescription = updates.get("description");
            Product updatedProduct = productService.updateProductDescription(id, newDescription);
            return ResponseEntity.ok(updatedProduct);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update product description.");
        }
    }

    // Helper method to convert Product entity to ProductDto
    private ProductDto convertToDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setDescription(product.getDescription());
        dto.setRating(product.getRating());
        dto.setBrand(product.getBrand());
        dto.setTags(product.getTags());

        // Map categories to just their names
        if (product.getCategories() != null) {
            Set<String> categoryNames = product.getCategories().stream()
                    .map(category -> category.getName())
                    .collect(Collectors.toSet());
            dto.setCategories(categoryNames);
        } else {
            dto.setCategories(new HashSet<>());
        }

        return dto;
    }

    // Inner classes for request bodies
    public static class UpdateNameRequest {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class UpdateDescriptionRequest {
        private String description;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
