package is.hi.verzla_backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import is.hi.verzla_backend.dto.ApiResponse;
import is.hi.verzla_backend.dto.LoginRequest;
import is.hi.verzla_backend.dto.LoginResponse;
import is.hi.verzla_backend.entities.User;
import is.hi.verzla_backend.repositories.UserRepository;
import is.hi.verzla_backend.security.JwtUtils;
import is.hi.verzla_backend.security.UserDetailsImpl;

/**
 * Controller for handling authentication-related actions such as login and logout.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

  @Autowired
  private UserRepository userRepository;
  
  @Autowired
  private AuthenticationManager authenticationManager;
  
  @Autowired
  private JwtUtils jwtUtils;
  
  @Autowired
  private PasswordEncoder passwordEncoder;

  /**
   * Handles user login by verifying credentials and issuing a JWT token if successful.
   * For backward compatibility with existing users, this method tries two authentication methods:
   * 1. Standard Spring Security authentication with encoded passwords
   * 2. Direct comparison with plain text passwords for existing users
   *
   * @param loginRequest The login request containing username (email) and password.
   * @return ResponseEntity with success message and JWT token if login is successful, or an error message if login fails.
   */
  @PostMapping("/login")
  public ResponseEntity<ApiResponse<LoginResponse>> login(
      @RequestBody LoginRequest loginRequest) {
    try {
      // Try Spring Security authentication first (for new users with encoded passwords)
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
      );
      
      // If authentication is successful, proceed with token generation
      SecurityContextHolder.getContext().setAuthentication(authentication);
      UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
      String jwt = jwtUtils.generateJwtToken(userDetails.getUsername(), userDetails.getId());
      
      // Create response with user details and token
      LoginResponse loginResponse = new LoginResponse(
          userDetails.getId(), 
          userDetails.getName(), 
          userDetails.getEmail(),
          jwt
      );
      
      return ResponseEntity.ok(ApiResponse.success("Login successful", loginResponse));
    } catch (Exception e) {
      // If Spring Security authentication fails, try legacy plain text comparison
      User user = userRepository.findByEmail(loginRequest.getUsername());
      if (user != null && user.getPassword().equals(loginRequest.getPassword())) {
        // Plain text password matches, generate token
        String jwt = jwtUtils.generateJwtToken(user.getEmail(), user.getId());
        
        // Create response with user details and token
        LoginResponse loginResponse = new LoginResponse(
            user.getId(), 
            user.getName(), 
            user.getEmail(),
            jwt
        );
        
        return ResponseEntity.ok(ApiResponse.success("Login successful", loginResponse));
      }
      
      // Both authentication methods failed
      return ResponseEntity
          .status(HttpStatus.UNAUTHORIZED)
          .body(ApiResponse.error("Invalid credentials"));
    }
  }

  /**
   * No server-side logout is needed with JWT authentication.
   * Client should simply discard the token.
   *
   * @return ResponseEntity with success message indicating the logout was successful.
   */
  @PostMapping("/logout")
  public ResponseEntity<ApiResponse<Void>> logout() {
    // With JWT, server-side logout is not needed
    // The client simply discards the token
    return ResponseEntity.ok(ApiResponse.success("Logout successful", null));
  }
}
