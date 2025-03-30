package is.hi.verzla_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main entry point for the Verzla Spring Boot application.
 * 
 * <p>This application serves as a RESTful backend for the Verzla Android e-commerce 
 * application. It provides endpoints for authentication, product management, 
 * user management, cart and wishlist functionality, and order processing.</p>
 * 
 * <p>The application uses Spring Boot's auto-configuration capabilities to set up
 * the necessary components including database connection, security configurations,
 * and web endpoints.</p>
 * 
 * @see is.hi.verzla_backend.controllers Package containing REST controllers that handle HTTP requests
 * @see is.hi.verzla_backend.services Package containing service interfaces that define business logic
 * @see is.hi.verzla_backend.servicesimpl Package containing service implementations
 * @see is.hi.verzla_backend.entities Package containing JPA entity classes
 */
@SpringBootApplication
public class VerzlaApplication {

    /**
     * The main method, which starts the Spring Boot application.
     *
     * @param args Command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(VerzlaApplication.class, args);
    }

}
