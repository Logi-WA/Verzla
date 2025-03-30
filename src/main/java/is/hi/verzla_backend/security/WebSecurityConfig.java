package is.hi.verzla_backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import is.hi.verzla_backend.services.UserDetailsServiceImpl;

/**
 * Security configuration for the Verzla backend application.
 * 
 * <p>This class configures Spring Security to secure the API endpoints using JWT-based
 * authentication. It defines security rules, authentication providers, and JWT filter
 * configurations to protect the application's resources while allowing public access
 * to specific endpoints.</p>
 * 
 * <p>Key security features implemented:
 * <ul>
 *   <li>JWT-based stateless authentication</li>
 *   <li>BCrypt password encoding for secure password storage</li>
 *   <li>Custom authentication entry point for handling unauthorized requests</li>
 *   <li>Public access to authentication endpoints and API documentation</li>
 *   <li>Protected access to all other API endpoints requiring authentication</li>
 * </ul>
 * </p>
 * 
 * @see is.hi.verzla_backend.security.JwtAuthenticationFilter
 * @see is.hi.verzla_backend.security.JwtAuthEntryPoint
 * @see is.hi.verzla_backend.services.UserDetailsServiceImpl
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

  /**
   * Service for loading user details from the database
   */
  @Autowired
  private UserDetailsServiceImpl userDetailsService;

  /**
   * Custom entry point for handling authentication exceptions
   */
  @Autowired
  private JwtAuthEntryPoint unauthorizedHandler;

  /**
   * Creates a JWT authentication filter bean to intercept and process requests with JWT tokens.
   * 
   * <p>This filter extracts JWT tokens from request headers, validates them, and sets
   * authentication in the Spring Security context if the token is valid.</p>
   * 
   * @return A new JWT authentication filter instance
   */
  @Bean
  public JwtAuthenticationFilter authenticationJwtTokenFilter() {
    return new JwtAuthenticationFilter();
  }

  /**
   * Configures the authentication provider that validates user credentials.
   * 
   * <p>This provider uses the custom user details service to load user data
   * and the password encoder to verify passwords.</p>
   * 
   * @return A configured DaoAuthenticationProvider
   */
  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  /**
   * Creates an authentication manager bean for processing authentication requests.
   * 
   * @param authConfig The authentication configuration
   * @return The configured AuthenticationManager
   * @throws Exception if an error occurs during authentication manager setup
   */
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }

  /**
   * Creates a password encoder bean for securely hashing passwords.
   * 
   * <p>BCrypt is used as the hashing algorithm, which includes automatic salt generation
   * and is resistant to brute force attacks.</p>
   * 
   * @return A BCrypt password encoder
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * Configures the security filter chain with rules for protecting API endpoints.
   * 
   * <p>This method defines:
   * <ul>
   *   <li>Which endpoints are publicly accessible vs. require authentication</li>
   *   <li>CSRF protection settings</li>
   *   <li>Session management policy (stateless for JWT)</li>
   *   <li>Custom exception handling for unauthorized requests</li>
   *   <li>The order of security filters in the chain</li>
   * </ul>
   * </p>
   * 
   * @param http The HttpSecurity to configure
   * @return The built SecurityFilterChain
   * @throws Exception if an error occurs during configuration
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
        .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth.requestMatchers("/auth/**").permitAll()
            .requestMatchers("/api/public/**").permitAll()
            .requestMatchers("/v3/api-docs/**").permitAll()
            .requestMatchers("/swagger-ui/**").permitAll()
            .requestMatchers("/swagger-ui.html").permitAll()
            .requestMatchers("/swagger-resources/**").permitAll()
            .requestMatchers("/webjars/**").permitAll()
            .requestMatchers("/api-docs/**").permitAll()
            .anyRequest().authenticated());

    http.authenticationProvider(authenticationProvider());
    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}