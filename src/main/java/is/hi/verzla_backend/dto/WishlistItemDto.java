package is.hi.verzla_backend.dto;

import java.util.UUID;

/**
 * Data Transfer Object for WishlistItem entities.
 * Provides a flat structure for use in API responses.
 */
public class WishlistItemDto {
    private UUID id;
    private UUID productId;
    private String productName;
    private String productImageUrl;
    private double productPrice;
    private String productDescription;
    
    public WishlistItemDto() {
    }
    
    public WishlistItemDto(UUID id, UUID productId, String productName, 
                          String productImageUrl, double productPrice, 
                          String productDescription) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.productImageUrl = productImageUrl;
        this.productPrice = productPrice;
        this.productDescription = productDescription;
    }
    
    // Getters and setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public UUID getProductId() {
        return productId;
    }
    
    public void setProductId(UUID productId) {
        this.productId = productId;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    public String getProductImageUrl() {
        return productImageUrl;
    }
    
    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }
    
    public double getProductPrice() {
        return productPrice;
    }
    
    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }
    
    public String getProductDescription() {
        return productDescription;
    }
    
    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }
}
