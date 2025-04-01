package is.hi.verzla_backend.services;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import jakarta.annotation.PostConstruct;

/**
 * Service for handling image uploads and retrievals to/from Amazon S3 cloud storage.
 *
 * <p>This service provides functionality to:
 * <ul>
 *   <li>Upload product images to an S3 bucket</li>
 *   <li>Generate URLs for accessing stored images</li>
 *   <li>Gracefully fall back to placeholder images when S3 is not available or configured</li>
 * </ul>
 * </p>
 *
 * <p>The service uses AWS SDK to interact with the S3 service and handles the credentials,
 * bucket names, and region configurations via application properties. This allows
 * for different configurations in development and production environments.</p>
 *
 * <p>In development mode (when S3 is disabled), the service automatically provides
 * placeholder image URLs instead of attempting actual S3 operations.</p>
 *
 * @see com.amazonaws.services.s3.AmazonS3
 * @see org.springframework.web.multipart.MultipartFile
 */
@Service
public class S3ImageService {

    /**
     * AWS S3 access key injected from application properties
     */
    @Value("${aws.s3.accessKey}")
    private String accessKey;

    /**
     * AWS S3 secret key injected from application properties
     */
    @Value("${aws.s3.secretKey}")
    private String secretKey;

    /**
     * S3 bucket name where images will be stored
     */
    @Value("${aws.s3.bucket}")
    private String bucketName;

    /**
     * AWS region where the S3 bucket is located
     */
    @Value("${aws.s3.region}")
    private String region;

    /**
     * Prefix for product images in the S3 bucket, used for organization
     */
    @Value("${aws.s3.productImagesPrefix}")
    private String productImagesPrefix;

    /**
     * Flag to enable/disable S3 integration, useful for development environments
     */
    @Value("${aws.s3.enabled:true}")
    private boolean enabled;

    /**
     * AWS S3 client used to interact with the S3 service
     */
    private AmazonS3 s3client;

    /**
     * Initializes the Amazon S3 client with credentials and region.
     *
     * <p>This method runs after dependency injection is complete, configuring
     * the S3 client with the provided AWS credentials if S3 integration is enabled.</p>
     */
    @PostConstruct
    private void initializeAmazon() {
        if (enabled) {
            AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
            this.s3client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withRegion(Regions.valueOf(region))
                    .build();
        }
    }

    /**
     * Uploads an image file to the S3 bucket.
     *
     * <p>In development mode (when S3 is disabled), this method will return a
     * placeholder URL without attempting to upload the file.</p>
     *
     * @param file   The image file to upload
     * @param prefix Optional prefix to add to the file path in the bucket
     * @return The URL of the uploaded image, or a placeholder URL if S3 is disabled
     * @throws IOException If there is an error reading the file
     */
    public String uploadImage(MultipartFile file, String prefix) throws IOException {
        if (!enabled) {
            return "https://placeholder.com/product-image";
        }

        String fileKey = generateFileKey(file.getOriginalFilename(), prefix);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        try (InputStream inputStream = file.getInputStream()) {
            s3client.putObject(new PutObjectRequest(bucketName, fileKey, inputStream, metadata));
            return s3client.getUrl(bucketName, fileKey).toString();
        }
    }

    /**
     * Uploads a product image to the S3 bucket.
     *
     * <p>Uses the configured product images prefix for organizing product images
     * in the bucket.</p>
     *
     * @param file The product image file to upload
     * @return The URL of the uploaded image, or a placeholder URL if S3 is disabled
     * @throws IOException If there is an error reading the file
     */
    public String uploadProductImage(MultipartFile file) throws IOException {
        return uploadImage(file, productImagesPrefix);
    }

    /**
     * Generates a unique file key for storing the image in S3.
     *
     * <p>This method ensures that each uploaded file has a unique name by
     * appending a timestamp to the original filename.</p>
     *
     * @param filename The original filename
     * @param prefix   Optional prefix to add to the file path
     * @return A unique file key for the S3 bucket
     */
    private String generateFileKey(String filename, String prefix) {
        String timestamp = String.valueOf(new Date().getTime());
        String fileKey = (prefix != null ? prefix + "/" : "") + timestamp + "_" + filename;
        return fileKey.replace(" ", "_");
    }

    /**
     * Gets the URL for an image stored in the S3 bucket.
     *
     * <p>In development mode (when S3 is disabled), this method will return a
     * placeholder URL.</p>
     *
     * @param fileKey The key of the file in the S3 bucket
     * @return The URL to access the image, or a placeholder URL if S3 is disabled
     */
    public String getImageUrl(String fileKey) {
        if (!enabled || fileKey == null || fileKey.isEmpty()) {
            return "https://placeholder.com/product-image";
        }

        return s3client.getUrl(bucketName, fileKey).toString();
    }

    /**
     * Deletes an image from the S3 bucket.
     *
     * <p>In development mode (when S3 is disabled), this method will do nothing.</p>
     *
     * @param fileKey The key of the file to delete from the S3 bucket
     * @return true if the file was deleted, false if S3 is disabled
     */
    public boolean deleteImage(String fileKey) {
        if (!enabled || fileKey == null || fileKey.isEmpty()) {
            return false;
        }

        s3client.deleteObject(bucketName, fileKey);
        return true;
    }
}