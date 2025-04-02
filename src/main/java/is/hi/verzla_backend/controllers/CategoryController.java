package is.hi.verzla_backend.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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
import is.hi.verzla_backend.dto.CategoryDto;
import is.hi.verzla_backend.dto.CreateUpdateCategoryDto;
import is.hi.verzla_backend.entities.Category;
import is.hi.verzla_backend.exceptions.ResourceNotFoundException;
import is.hi.verzla_backend.services.CategoryService;
import jakarta.validation.Valid;

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
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // Helper to convert Category Entity -> CategoryDto
    private CategoryDto convertToDto(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        // dto.setProductCount(categoryService.getProductCountInCategory(category.getId())); // add count?
        return dto;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryDto>>> getCategories() {
        List<Category> categories = categoryService.getAllCategoriesSorted();
        List<CategoryDto> dtos = categories.stream().map(this::convertToDto).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(dtos));
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryDto>> getCategoryById(@PathVariable UUID categoryId) {
        Category category = categoryService.getCategoryById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
        return ResponseEntity.ok(ApiResponse.success(convertToDto(category)));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse<CategoryDto>> getCategoryByName(@PathVariable String name) {
        Category category = categoryService.getCategoryByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with name: " + name));
        return ResponseEntity.ok(ApiResponse.success(convertToDto(category)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryDto>> createCategory(
            @Valid @RequestBody CreateUpdateCategoryDto categoryDto) { // Use DTO and @Valid
        // Map DTO to Entity
        Category newCategory = new Category();
        newCategory.setName(categoryDto.getName());

        Category createdCategory = categoryService.createCategory(newCategory); // Service handles conflict check
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(convertToDto(createdCategory)));
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryDto>> updateCategory(
            @PathVariable UUID categoryId,
            @Valid @RequestBody CreateUpdateCategoryDto categoryDto) { // Use DTO and @Valid

        // Map DTO details to an entity structure for the service update method
        Category categoryDetails = new Category();
        categoryDetails.setName(categoryDto.getName()); // Only pass name

        Category updatedCategory = categoryService.updateCategory(categoryId, categoryDetails); // Service handles conflict & not found
        return ResponseEntity.ok(ApiResponse.success(convertToDto(updatedCategory)));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<String>> deleteCategory(@PathVariable UUID categoryId) {
        // Service now throws exceptions for not found or conflict
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.success("Category deleted successfully"));
        // Use GlobalExceptionHandler to catch ResourceNotFoundException (404)
        // and ResponseStatusException (409 Conflict) from the service
    }

    @GetMapping("/{categoryId}/product-count")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getProductCount(@PathVariable UUID categoryId) {
        // Check existence first or let service handle it
        if (!categoryService.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category not found with id: " + categoryId);
        }
        long count = categoryService.getProductCountInCategory(categoryId);
        Map<String, Object> response = new HashMap<>();
        response.put("categoryId", categoryId);
        response.put("productCount", count);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}
