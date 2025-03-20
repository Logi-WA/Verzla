package is.hi.verzla_backend.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import jakarta.annotation.PostConstruct;

/**
 * Service for handling image uploads and retrievals to/from AWS S3.
 */
@Service
public class S3ImageService {

    @Value("${aws.s3.accessKey}")
    private String accessKey;

    @Value("${aws.s3.secretKey}")
    private String secretKey;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Value("${aws.s3.region}")
    private String region;

    @Value("${aws.s3.productImagesPrefix}")
    private String productImagesPrefix;

    @Value("${aws.s3.enabled:true}")
    private boolean enabled;

    private AmazonS3 s3client;

    @PostConstruct
    private void initializeAmazon() {
        if (enabled) {
            AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
            this.s3client = AmazonS3ClientBuilder
                    .standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withRegion(Regions.fromName(region))
                    .build();
        }
    }

    /**
     * Gets the full URL for a product image.
     * 
     * @param imageName The name of the image file
     * @return The full URL to the image
     */
    public String getImageUrl(String imageName) {
        if (!enabled) {
            // Use fallback URLs for development when S3 is not configured
            return "https://via.placeholder.com/300?text=" + imageName;
        }

        // Check if image already exists in S3
        String imageKey = productImagesPrefix + imageName;
        if (!s3client.doesObjectExist(bucketName, imageKey)) {
            return "https://" + bucketName + ".s3." + region + ".amazonaws.com/placeholder.jpg";
        }

        return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + imageKey;
    }

    /**
     * Uploads a product image to S3.
     * 
     * @param file The image file to upload
     * @return The URL of the uploaded image
     * @throws IOException if the file cannot be read
     */
    public String uploadProductImage(MultipartFile file) throws IOException {
        if (!enabled) {
            return "https://via.placeholder.com/300?text=local-dev-" + file.getOriginalFilename();
        }

        // Generate a unique file name
        String fileName = new Date().getTime() + "-" + file.getOriginalFilename().replace(" ", "_");
        String imageKey = productImagesPrefix + fileName;

        // Set metadata
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        // Upload file
        try (InputStream inputStream = file.getInputStream()) {
            s3client.putObject(new PutObjectRequest(
                    bucketName, imageKey, inputStream, metadata));
        }

        return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + imageKey;
    }
}