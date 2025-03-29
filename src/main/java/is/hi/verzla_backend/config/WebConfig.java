package is.hi.verzla_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

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
