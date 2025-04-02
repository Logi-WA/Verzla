package is.hi.verzla_backend.dataloader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import is.hi.verzla_backend.entities.Category;
import is.hi.verzla_backend.entities.Product;
import is.hi.verzla_backend.entities.Review;
import is.hi.verzla_backend.repositories.ReviewRepository;
import is.hi.verzla_backend.services.CategoryService;
import is.hi.verzla_backend.services.ProductService;

@Component
@Profile("dataseeding") // Only run when 'dataseeding' profile is active
public class DatabaseSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseSeeder.class);

    @Autowired
    private ResourceLoader resourceLoader; // load files from classpath

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    // Inject Repository directly to bypass service logic during bulk load
    @Autowired
    private ReviewRepository reviewRepository;

    private ObjectMapper objectMapper; // For parsing JSON

    public DatabaseSeeder() {
        // Configure ObjectMapper to handle Java 8 dates
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    @Transactional // Wrap the whole process in a transaction
    public void run(String... args) throws Exception {
        logger.info("Starting database seeding...");

        // Define file paths relative to classpath
        String productsJsonPath = "classpath:data/products.json";
        String reviewsJsonPath = "classpath:data/reviews.json";

        try {
            // --- Load All Data ---
            logger.info("Loading products from {}", productsJsonPath);
            Resource productResource = resourceLoader.getResource(productsJsonPath);
            List<JsonProduct> jsonProducts = objectMapper.readValue(productResource.getInputStream(),
                    new TypeReference<List<JsonProduct>>() {
                    });
            logger.info("Found {} products in JSON file.", jsonProducts.size());

            logger.info("Loading reviews from {}", reviewsJsonPath);
            Resource reviewResource = resourceLoader.getResource(reviewsJsonPath);
            List<JsonReview> jsonReviews = objectMapper.readValue(reviewResource.getInputStream(),
                    new TypeReference<List<JsonReview>>() {
                    });
            logger.info("Found {} reviews in JSON file.", jsonReviews.size());

            // --- Group Reviews by Product ID ---
            logger.info("Grouping reviews by product ID...");
            Map<UUID, List<JsonReview>> reviewsByProductId = jsonReviews.stream()
                    .collect(Collectors.groupingBy(JsonReview::getProductId));
            logger.info("Grouped reviews for {} distinct products.", reviewsByProductId.size());

            // --- Process Products and Their Reviews Together ---
            int productCount = 0;
            int reviewCount = 0;
            int skippedReviewCount = 0;

            for (JsonProduct jsonProd : jsonProducts) {
                Product savedProduct = processProduct(jsonProd); // Modify processProduct to return the saved Product

                if (savedProduct != null) {
                    productCount++;

                    // Process reviews for this specific product
                    List<JsonReview> relatedReviews = reviewsByProductId.getOrDefault(savedProduct.getId(),
                            Collections.emptyList());
                    for (JsonReview jsonRev : relatedReviews) {
                        if (processReview(jsonRev, savedProduct)) { // Pass the already fetched product
                            reviewCount++;
                        } else {
                            skippedReviewCount++;
                        }
                    }
                } else {
                    logger.warn("Skipped product processing for product JSON with name: {}", jsonProd.getName());
                    // Potentially count skipped products
                }
            }

            logger.info("Successfully processed {} products.", productCount);
            logger.info("Successfully processed {} reviews.", reviewCount);
            if (skippedReviewCount > 0) {
                logger.warn("Skipped processing {} reviews due to errors.", skippedReviewCount);
            }

            logger.info("Database seeding completed successfully.");
            // Let the transaction commit here

        } catch (Exception e) {
            logger.error("Database seeding failed!", e);
            throw e; // Ensure rollback
        }
    }

    private Product processProduct(JsonProduct jsonProd) {
        try {
            String categoryName = (jsonProd.getCategories() != null && !jsonProd.getCategories().isEmpty())
                    ? jsonProd.getCategories().get(0)
                    : "Uncategorized";
            Category category = findOrCreateCategory(categoryName);

            Product product = new Product();
            product.setId(jsonProd.getId());
            product.setName(jsonProd.getName());
            product.setPrice(jsonProd.getPrice() != null ? jsonProd.getPrice() : 0.0);
            product.setDescription(jsonProd.getDescription());
            product.setBrand(jsonProd.getBrand());
            product.setCategory(category);
            product.setRating(jsonProd.getRating() != null ? jsonProd.getRating() : 0.0); // Set initial rating
            product.setTags(jsonProd.getTags() != null ? new ArrayList<>(jsonProd.getTags()) : new ArrayList<>());

            return productService.createProduct(product); // Return the saved product
        } catch (Exception e) {
            logger.error("Failed to process product JSON with name {}: {}", jsonProd.getName(), e.getMessage());
            return null; // Return null on failure
        }
    }

    private Category findOrCreateCategory(String name) {
        Optional<Category> existingCategory = categoryService.getCategoryByName(name);

        if (existingCategory.isPresent()) {
            return existingCategory.get();
        } else {
            logger.info("Creating new category: {}", name);
            Category newCategory = new Category();
            newCategory.setName(name);
            return categoryService.createCategory(newCategory); // Service handles saving
        }
    }

    private boolean processReview(JsonReview jsonRev, Product product) { // Accept Product object

        try {
            Review review = new Review();
            review.setReviewId(jsonRev.getReviewId());
            review.setProduct(product); // Set the relationship
            review.setRating(jsonRev.getRating());
            review.setComment(jsonRev.getComment());
            review.setDate(jsonRev.getDate());
            review.setReviewerName(jsonRev.getReviewerName());
            review.setReviewerEmail(jsonRev.getReviewerEmail());

            reviewRepository.save(review); // Save directly
            return true; // Indicate success
        } catch (Exception e) {
            logger.error("Failed to process review ID {}: {}", jsonRev.getReviewId(), e.getMessage());
            return false; // Indicate failure
        }
    }
}