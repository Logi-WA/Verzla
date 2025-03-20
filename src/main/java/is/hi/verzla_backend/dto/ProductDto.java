package is.hi.verzla_backend.dto;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Data Transfer Object for Product entities.
 * Provides a simplified structure for API responses.
 */
public class ProductDto {
    private UUID id;
    private String name;
    private double price;  
    private String imageUrl;
    private String description;
    private Set<String> categories = new HashSet<>();
    
    public ProductDto() {
    }
    
    // Getters and setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Set<String> getCategories() {
        return categories;
    }
    
    public void setCategories(Set<String> categories) {
        this.categories = categories;
    }
}
