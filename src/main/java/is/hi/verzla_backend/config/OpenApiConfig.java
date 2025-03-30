package is.hi.verzla_backend.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

/**
 * Configuration for OpenAPI 3.0 documentation for the Verzla API.
 * 
 * <p>This class configures the OpenAPI documentation (Swagger UI) for the application, 
 * including API information, security requirements, and available servers.</p>
 * 
 * <p>The OpenAPI specification provides a standardized way to document RESTful APIs.
 * This configuration enables API consumers to:
 * <ul>
 *   <li>Discover available endpoints and their operations</li>
 *   <li>Understand authentication requirements</li>
 *   <li>View request/response formats</li>
 *   <li>Test API endpoints directly via the Swagger UI</li>
 * </ul>
 * </p>
 * 
 * <p>The documentation is accessible at:
 * <ul>
 *   <li>Local: http://localhost:8080/swagger-ui.html</li>
 *   <li>Production: https://verzla-71cda7a37a2e.herokuapp.com/swagger-ui.html</li>
 * </ul>
 * </p>
 * 
 * @see <a href="https://springdoc.org/">SpringDoc OpenAPI documentation</a>
 */
@Configuration
public class OpenApiConfig {

    /**
     * Configures the OpenAPI documentation with information about the API,
     * security schemes, and server information.
     * 
     * <p>This method sets up:
     * <ul>
     *   <li>JWT authentication requirements and configuration</li>
     *   <li>API title, description, and version information</li>
     *   <li>License information</li>
     *   <li>Available server environments</li>
     * </ul>
     * </p>
     * 
     * @return A configured {@link OpenAPI} instance
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-jwt",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .in(SecurityScheme.In.HEADER)
                                        .name("Authorization")))
                .info(new Info()
                        .title("Verzla Backend API")
                        .description("RESTful API for the Verzla Mobile Application")
                        .version("1.0.0")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html")))
                .addSecurityItem(new SecurityRequirement().addList("bearer-jwt"))
                .servers(List.of(
                        new Server()
                                .url("https://verzla-71cda7a37a2e.herokuapp.com")
                                .description("Production Server")));
    }
}
