package is.hi.verzla_backend.dto;

import java.util.UUID;

/**
 * Data Transfer Object for CartItem entities.
 * Provides a flat structure for use in API responses.
 */
public class CartItemDto {
    private UUID id;
    private UUID productId;
    private String productName;
    private String productImageUrl;
    private double productPrice;
    private int quantity;
    
    public CartItemDto() {
    }
    
    public CartItemDto(UUID id, UUID productId, String productName, String productImageUrl, 
                      double productPrice, int quantity) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.productImageUrl = productImageUrl;
        this.productPrice = productPrice;
        this.quantity = quantity;
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
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
