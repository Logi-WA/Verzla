package is.hi.verzla_backend.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import is.hi.verzla_backend.dto.ApiResponse;
import is.hi.verzla_backend.services.S3ImageService;

/**
 * Controller for handling image uploads.
 */
@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Autowired
    private S3ImageService imageService;
    
    /**
     * Uploads a product image and returns its URL.
     * 
     * @param file The image file to upload
     * @return The URL of the uploaded image
     */
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<String>> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String imageUrl = imageService.uploadProductImage(file);
            return ResponseEntity.ok(ApiResponse.success("Image uploaded successfully", imageUrl));
        } catch (IOException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to upload image: " + e.getMessage()));
        }
    }
}
