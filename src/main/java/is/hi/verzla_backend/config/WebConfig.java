package is.hi.verzla_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for web-specific settings in the Verzla backend application.
 *
 * <p>This class configures Cross-Origin Resource Sharing (CORS) to allow the Verzla
 * Android application and development environments to access the API. CORS is a security
 * feature implemented by browsers that restricts web page requests to other domains.</p>
 *
 * <p>The configuration allows:
 * <ul>
 *   <li>Access from mobile platforms (Capacitor, Ionic, Android file protocol)</li>
 *   <li>Access from development environments (Angular, Ionic)</li>
 *   <li>All standard HTTP methods (GET, POST, PUT, PATCH, DELETE)</li>
 *   <li>Credentials to be included in requests</li>
 *   <li>The Authorization header to be exposed for JWT authentication</li>
 * </ul>
 * </p>
 *
 * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurer
 * @see org.springframework.web.servlet.config.annotation.CorsRegistry
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configures CORS settings for the application.
     *
     * <p>This method adds CORS mappings that apply to all endpoints in the application,
     * allowing specific origins to access the API while maintaining security.</p>
     *
     * @param registry The CorsRegistry to configure
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Configure CORS for all API endpoints
        registry.addMapping("/**")
                .allowedOrigins(
                        "capacitor://localhost", // Capacitor
                        "ionic://localhost", // Ionic local
                        "file://", // Android file protocol
                        "http://localhost:4200", // Angular development
                        "http://localhost:8100" // Ionic development
                )
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Authorization") // Allow Authorization header to be exposed
                .allowCredentials(true)
                .maxAge(3600);
    }
}
