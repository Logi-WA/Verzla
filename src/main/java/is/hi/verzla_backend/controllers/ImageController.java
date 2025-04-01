package is.hi.verzla_backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import is.hi.verzla_backend.dto.ApiResponse;
import is.hi.verzla_backend.services.S3ImageService;

/**
 * REST controller for handling image-related operations in the Verzla e-commerce platform.
 * <p>
 * This controller handles HTTP requests mapped to {@code /api/images} and provides endpoints
 * for uploading, retrieving, and managing product images. It interacts with the
 * {@link S3ImageService} to store and retrieve images from Amazon S3 or similar cloud storage.
 * </p>
 *
 * <p>
 * Image management is a critical aspect of the e-commerce platform, as high-quality product
 * images significantly impact the user experience and conversion rates. This controller
 * facilitates the following use cases:
 * <ul>
 *   <li>Uploading product images during product creation or updates</li>
 *   <li>Managing image storage and retrieval from cloud storage</li>
 *   <li>Generating and returning public URLs for product images</li>
 * </ul>
 * </p>
 *
 * <p>
 * The controller uses Spring's multipart file handling capabilities to process
 * image uploads and interacts with cloud storage services to persist the images.
 * </p>
 *
 * @see S3ImageService
 * @see is.hi.verzla_backend.entities.Product
 * @see is.hi.verzla_backend.controllers.ProductController
 */
@RestController
@RequestMapping("/api/images")
public class ImageController {

    /**
     * Service responsible for uploading and managing images in Amazon S3 or similar cloud storage.
     * <p>
     * This service encapsulates the logic for:
     * <ul>
     *   <li>Uploading image files to cloud storage</li>
     *   <li>Generating publicly accessible URLs for the uploaded images</li>
     *   <li>Managing image metadata and storage paths</li>
     *   <li>Handling potential storage service errors and exceptions</li>
     * </ul>
     * </p>
     */
    @Autowired
    private S3ImageService imageService;

    /**
     * Handles the upload of product images to cloud storage.
     * <p>
     * This endpoint accepts multipart file uploads, processes the image data, and stores
     * the image in the configured cloud storage service. Upon successful upload, it returns
     * a public URL that can be associated with a product for display purposes.
     * </p>
     *
     * <p>
     * The endpoint is typically used during:
     * <ul>
     *   <li>Product creation to add initial product images</li>
     *   <li>Product updates to modify or add additional product images</li>
     *   <li>Content management operations by administrators</li>
     * </ul>
     * </p>
     *
     * @param file The multipart file containing the image data to be uploaded
     * @return A ResponseEntity containing an ApiResponse with either:
     * <ul>
     *   <li>A success message and the URL of the uploaded image</li>
     *   <li>An error message if the upload fails</li>
     * </ul>
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
