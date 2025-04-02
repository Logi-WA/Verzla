package is.hi.verzla_backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import is.hi.verzla_backend.dto.ApiResponse;
import is.hi.verzla_backend.dto.LoginRequest;
import is.hi.verzla_backend.dto.LoginResponse;
import is.hi.verzla_backend.security.JwtUtils;
import is.hi.verzla_backend.security.UserDetailsImpl;

/**
 * Controller for handling authentication-related actions in the Verzla API.
 *
 * <p>This controller manages user authentication through JWT (JSON Web Tokens),
 * providing endpoints for login and logout operations. The authentication system
 * is stateless, with the server issuing tokens that clients present with subsequent
 * requests to establish their identity.</p>
 *
 * <p>The controller implements a dual authentication strategy:
 * <ul>
 *   <li>For newer users: Standard Spring Security authentication with encoded passwords</li>
 *   <li>For legacy users: Direct database comparison with plaintext passwords for backward compatibility</li>
 * </ul>
 * </p>
 *
 * <p>Security considerations:
 * <ul>
 *   <li>Passwords for new users are stored encrypted using BCrypt</li>
 *   <li>JWT tokens are signed to prevent tampering</li>
 *   <li>Tokens expire after a configured time period</li>
 * </ul>
 * </p>
 *
 * @see is.hi.verzla_backend.security.JwtUtils For token generation and validation
 * @see is.hi.verzla_backend.security.WebSecurityConfig For security configuration
 * @see is.hi.verzla_backend.security.UserDetailsImpl For user details implementation
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    /**
     * Spring Security authentication manager
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Utility for JWT token operations
     */
    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @RequestBody LoginRequest loginRequest) {
        try {
            // Use Spring Security authentication ONLY
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            // If authentication is successful, proceed with token generation
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            String jwt = jwtUtils.generateJwtToken(userDetails.getUsername(), userDetails.getId());

            // Create response with user details and token
            LoginResponse loginResponse = new LoginResponse(
                    userDetails.getId(),
                    userDetails.getName(),
                    userDetails.getEmail(),
                    jwt);

            return ResponseEntity.ok(ApiResponse.success("Login successful", loginResponse));

        } catch (AuthenticationException e) { // Catch specific Spring Security exception
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Invalid credentials"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Login failed due to an internal error."));
        }
    }

    /**
     * Handles user logout from the application.
     *
     * <p>With JWT-based authentication, server-side logout is not strictly necessary
     * since authentication is stateless. The client simply discards the token to
     * effectively log out. However, this endpoint is provided for API completeness
     * and potential future enhancements such as token blacklisting.</p>
     *
     * <p>Security best practices for clients:
     * <ul>
     *   <li>Discard the JWT token from client-side storage</li>
     *   <li>Clear any user data from memory</li>
     *   <li>Redirect to login or public area</li>
     * </ul>
     * </p>
     *
     * @return ResponseEntity with success message indicating the logout operation completed
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        // With JWT, server-side logout is not needed
        // The client simply discards the token
        return ResponseEntity.ok(ApiResponse.success("Logout successful", null));
    }
}
