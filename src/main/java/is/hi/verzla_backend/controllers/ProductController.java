package is.hi.verzla_backend.controllers;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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

import is.hi.verzla_backend.dto.ApiResponse;
import is.hi.verzla_backend.dto.ProductDto;
import is.hi.verzla_backend.entities.Product;
import is.hi.verzla_backend.services.ProductService;

/**
 * REST controller for managing products.
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * Retrieves a list of all products, with optional filtering by category.
     *
     * @param category Optional category filter to retrieve products by specific category.
     * @return A list of products, filtered by category if provided.
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
     * Retrieves a specific product by its ID.
     *
     * @param id The ID of the product to retrieve.
     * @return The Product object with the specified ID.
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
     * Creates a new product.
     *
     * @param product The Product object to be created.
     * @return The created Product object.
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
        dto.setImageUrl(product.getImageUrl());
        dto.setDescription(product.getDescription());
        
        // Map categories to just their names
        Set<String> categoryNames = product.getCategories().stream()
            .map(category -> category.getName())
            .collect(Collectors.toSet());
        dto.setCategories(categoryNames);
        
        return dto;
    }

    // Inner classes for request bodies (if needed)
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
