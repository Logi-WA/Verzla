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
            "http://localhost:8081",     // Web development
            "http://localhost:3000",     // React development
            "capacitor://localhost",     // Capacitor
            "ionic://localhost",         // Ionic local
            "http://localhost",          // Local apps
            "file://"                    // Android/iOS file protocol
        )
        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
        .allowedHeaders("*")
        .exposedHeaders("Authorization")  // Allow Authorization header to be exposed
        .allowCredentials(true)
        .maxAge(3600);

    // Configure CORS for auth endpoints
    registry.addMapping("/auth/**")
        .allowedOrigins(
            "http://localhost:8081",
            "http://localhost:3000", 
            "capacitor://localhost",
            "ionic://localhost",
            "http://localhost",
            "file://"
        )
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
        .allowedHeaders("*")
        .exposedHeaders("Authorization")
        .allowCredentials(true)
        .maxAge(3600);
  }
}
