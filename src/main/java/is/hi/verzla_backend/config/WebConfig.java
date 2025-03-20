package is.hi.verzla_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Configure CORS for all API endpoints
        registry.addMapping("/api/**")
                .allowedOrigins(
                        "capacitor://localhost", // Capacitor
                        "ionic://localhost", // Ionic local
                        "file://" // Android file protocol
                )
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Authorization") // Allow Authorization header to be exposed
                .allowCredentials(true)
                .maxAge(3600);

        // Configure CORS for auth endpoints
        registry.addMapping("/auth/**")
                .allowedOrigins(
                        "capacitor://localhost",
                        "ionic://localhost",
                        "file://")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Authorization")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
