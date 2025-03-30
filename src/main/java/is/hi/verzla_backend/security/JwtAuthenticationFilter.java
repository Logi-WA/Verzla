package is.hi.verzla_backend.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import is.hi.verzla_backend.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filter for processing and validating JWT tokens in incoming HTTP requests.
 * 
 * <p>This filter intercepts every HTTP request to the API and checks for the presence
 * of a JWT token in the Authorization header. If a valid token is found, it extracts
 * the user information, loads the corresponding user details, and sets up the
 * Spring Security context to authenticate the user for the current request.</p>
 * 
 * <p>The filter performs the following steps:
 * <ol>
 *   <li>Extracts the JWT token from the Authorization header</li>
 *   <li>Validates the token using {@link JwtUtils}</li>
 *   <li>Extracts the username from the token</li>
 *   <li>Loads the user details from the database</li>
 *   <li>Creates an authentication token with user details</li>
 *   <li>Sets the authentication in the Spring Security context</li>
 * </ol>
 * </p>
 * 
 * <p>This filter is essential for the JWT-based stateless authentication system,
 * as it processes each incoming request and establishes the security context
 * based on the provided token, allowing the rest of the application to work
 * with an authenticated user.</p>
 * 
 * @see org.springframework.web.filter.OncePerRequestFilter
 * @see is.hi.verzla_backend.security.JwtUtils
 * @see is.hi.verzla_backend.services.UserDetailsServiceImpl
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  /**
   * Logger for recording filter operations and potential token processing issues
   */
  private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

  /**
   * Utility for JWT token operations such as validation and extracting user information
   */
  @Autowired
  private JwtUtils jwtUtils;

  /**
   * Service for loading user details from the database by username
   */
  @Autowired
  private UserDetailsServiceImpl userDetailsService;

  /**
   * Processes each HTTP request to extract and validate JWT tokens.
   * 
   * <p>This method is called once for each HTTP request to the application.
   * It attempts to extract a JWT token from the Authorization header, validate it,
   * and set up the security context if the token is valid.</p>
   * 
   * <p>If no token is present or the token is invalid, the method simply continues
   * the filter chain without setting authentication, which will result in an
   * anonymous request (and potentially a 401 response if the resource requires
   * authentication).</p>
   *
   * @param request The HTTP request being processed
   * @param response The HTTP response being prepared
   * @param filterChain The filter chain to continue processing after this filter
   * @throws ServletException If a servlet error occurs during processing
   * @throws IOException If an I/O error occurs during processing
   */
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      String jwt = parseJwt(request);
      if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
        String username = jwtUtils.getUsernameFromJwtToken(jwt);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    } catch (Exception e) {
      logger.error("Cannot set user authentication: {}", e);
    }

    filterChain.doFilter(request, response);
  }

  /**
   * Extracts the JWT token from the HTTP request's Authorization header.
   * 
   * <p>This method looks for a Bearer token in the Authorization header
   * and extracts the JWT string if present. It handles the standard format
   * of "Bearer [token]" and removes the "Bearer " prefix.</p>
   *
   * @param request The HTTP request to extract the token from
   * @return The JWT token string if found, null otherwise
   */
  private String parseJwt(HttpServletRequest request) {
    String headerAuth = request.getHeader("Authorization");

    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
      return headerAuth.substring(7);
    }

    return null;
  }
}