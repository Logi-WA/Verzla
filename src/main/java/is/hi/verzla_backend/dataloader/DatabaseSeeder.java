package is.hi.verzla_backend.dataloader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import is.hi.verzla_backend.entities.Category;
import is.hi.verzla_backend.entities.Product;
import is.hi.verzla_backend.entities.Review;
import is.hi.verzla_backend.exceptions.ResourceNotFoundException;
import is.hi.verzla_backend.repositories.ReviewRepository;
import is.hi.verzla_backend.services.CategoryService;
import is.hi.verzla_backend.services.ProductService;
// import is.hi.verzla_backend.services.ReviewService; // Don't use ReviewService for create

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
      // --- Load Products ---
      logger.info("Loading products from {}", productsJsonPath);
      Resource productResource = resourceLoader.getResource(productsJsonPath);
      InputStream productInputStream = productResource.getInputStream();
      List<JsonProduct> jsonProducts = objectMapper.readValue(productInputStream,
          new TypeReference<List<JsonProduct>>() {
          });
      logger.info("Found {} products in JSON file.", jsonProducts.size());

      int productCount = 0;
      for (JsonProduct jsonProd : jsonProducts) {
        processProduct(jsonProd);
        productCount++;
      }
      logger.info("Successfully processed {} products.", productCount);

      // --- Load Reviews ---
      logger.info("Loading reviews from {}", reviewsJsonPath);
      Resource reviewResource = resourceLoader.getResource(reviewsJsonPath);
      InputStream reviewInputStream = reviewResource.getInputStream();
      List<JsonReview> jsonReviews = objectMapper.readValue(reviewInputStream, new TypeReference<List<JsonReview>>() {
      });
      logger.info("Found {} reviews in JSON file.", jsonReviews.size());

      int reviewCount = 0;
      for (JsonReview jsonRev : jsonReviews) {
        processReview(jsonRev);
        reviewCount++;
      }
      logger.info("Successfully processed {} reviews.", reviewCount);

      logger.info("Database seeding completed successfully.");

    } catch (Exception e) {
      logger.error("Database seeding failed!", e);
      // The @Transactional annotation should cause a rollback on exception
      throw e; // Re-throw to ensure rollback
    }
  }

  private void processProduct(JsonProduct jsonProd) {
    try {
      // 1. Find or Create Category
      String categoryName = (jsonProd.getCategories() != null && !jsonProd.getCategories().isEmpty())
          ? jsonProd.getCategories().get(0) // Assume first category is primary
          : "Uncategorized"; // Default if missing

      Category category = findOrCreateCategory(categoryName);

      // 2. Create Product Entity
      Product product = new Product();
      product.setId(jsonProd.getId()); // Use the pre-generated UUID
      product.setName(jsonProd.getName());
      product.setPrice(jsonProd.getPrice() != null ? jsonProd.getPrice() : 0.0);
      product.setDescription(jsonProd.getDescription());
      product.setBrand(jsonProd.getBrand());
      // Set the initial rating directly from JSON data
      product.setRating(jsonProd.getRating() != null ? jsonProd.getRating() : 0.0);
      // Set tags (handle null)
      product.setTags(jsonProd.getTags() != null ? new ArrayList<>(jsonProd.getTags()) : new ArrayList<>());
      product.setCategory(category); // Set the relationship

      // 3. Save using ProductService
      productService.createProduct(product);
      // logger.debug("Processed product: {}", product.getName()); // Use debug level

    } catch (Exception e) {
      logger.error("Failed to process product ID {}: {}", jsonProd.getId(), e.getMessage());
      // Continue processing other products, transaction will eventually rollback if error persists
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

  private void processReview(JsonReview jsonRev) {
    try {
      // 1. Find the associated Product (must exist from previous step)
      // Use findById directly for efficiency, handle potential not found
      Product product = productService.getProductById(jsonRev.getProductId());
      // Alternative: Inject ProductRepository and use productRepository.findById(...)

      // 2. Create Review Entity
      Review review = new Review();
      review.setReviewId(jsonRev.getReviewId()); // Use pre-generated UUID
      review.setProduct(product); // Set the relationship
      review.setRating(jsonRev.getRating());
      review.setComment(jsonRev.getComment());
      review.setDate(jsonRev.getDate());
      review.setReviewerName(jsonRev.getReviewerName());
      review.setReviewerEmail(jsonRev.getReviewerEmail());

      // 3. Save directly using the Repository to bypass rating update logic
      reviewRepository.save(review);
      // logger.debug("Processed review for product ID: {}", jsonRev.getProductId());

    } catch (ResourceNotFoundException e) {
      logger.warn("Skipping review ID {} because associated product ID {} was not found.", jsonRev.getReviewId(),
          jsonRev.getProductId());
    } catch (Exception e) {
      logger.error("Failed to process review ID {}: {}", jsonRev.getReviewId(), e.getMessage());
    }
  }
}