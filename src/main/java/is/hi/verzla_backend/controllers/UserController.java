package is.hi.verzla_backend.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import is.hi.verzla_backend.dto.ApiResponse;
import is.hi.verzla_backend.dto.SignUpDto;
import is.hi.verzla_backend.dto.UpdateUserDto;
import is.hi.verzla_backend.dto.UserDto;
import is.hi.verzla_backend.entities.User;
import is.hi.verzla_backend.exceptions.ResourceNotFoundException;
import is.hi.verzla_backend.security.UserDetailsImpl;
import is.hi.verzla_backend.services.UserService;
import jakarta.validation.Valid;

/**
 * REST controller for managing user-related operations such as creating,
 * retrieving, updating, and deleting user accounts.
 * <p>
 * This controller handles HTTP requests mapped to {@code /api/users} and
 * interacts with the {@link UserService} to perform operations on
 * {@link User} entities.
 * </p>
 *
 * <p>
 * Supported operations include:
 * <ul>
 * <li>Retrieving all users</li>
 * <li>Retrieving a user by ID</li>
 * <li>Creating a new user</li>
 * <li>Updating user details</li>
 * <li>Updating user passwords</li>
 * <li>Deleting a user</li>
 * <li>Managing the currently logged-in user's information</li>
 * </ul>
 * </p>
 *
 * @see UserService
 * @see User
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    /**
     * Service layer for handling user-related business logic.
     */
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDto>>> getAllUsers() {
        List<UserDto> dtos = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success(dtos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable UUID id) {
        Optional<User> userOpt = userService.getUserById(id);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            UserDto dto = convertToDto(user); // Convert to DTO here
            return ResponseEntity.ok(ApiResponse.success(dto));
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("User not found with id " + id));
        }
    }

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody SignUpDto signUpDto) {
        // Check for existing email moved mostly to service, but can double check
        if (userService.existsByEmail(signUpDto.getEmail())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error("Email address already in use."));
        }
        try {
            User newUser = userService.createUserFromDto(signUpDto);
            UserDto dto = convertToDto(newUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("User created", dto));
        } catch (ResponseStatusException e) { // Catch conflict
            return ResponseEntity.status(e.getStatusCode()).body(ApiResponse.error(e.getReason()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error creating user: " + e.getMessage()));
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchUser(@PathVariable UUID id, @Valid @RequestBody UpdateUserDto userDto) {
        try {
            User updatedUser = userService.updateUser(id, userDto);
            UserDto dto = convertToDto(updatedUser);
            return ResponseEntity.ok(ApiResponse.success("User updated", dto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage()));
        } catch (ResponseStatusException e) { // Catch conflict exceptions from service
            return ResponseEntity.status(e.getStatusCode()).body(ApiResponse.error(e.getReason()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error updating user: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable UUID userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok(ApiResponse.success("User deleted successfully"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error deleting user: " + e.getMessage()));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            Optional<User> userOpt = userService.getUserById(userDetails.getId());

            if (userOpt.isPresent()) {
                User user = userOpt.get();
                UserDto dto = convertToDto(user);
                return ResponseEntity.ok(ApiResponse.success(dto));
            } else {
                // This case implies the user existed for JWT creation but was deleted afterwards
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Logged in user data could not be found."));
            }
        }
        // User is not authenticated via SecurityContextHolder
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("User not authenticated"));
    }

    @PatchMapping("/me")
    public ResponseEntity<?> updateCurrentUser(@Valid @RequestBody UpdateUserDto userDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl)) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("User not authenticated"));
        }

        UUID userId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
        try {
            User updatedUser = userService.updateUser(userId, userDto);
            UserDto dto = convertToDto(updatedUser);
            return ResponseEntity.ok(ApiResponse.success("User details updated", dto));
        } catch (ResourceNotFoundException e) { // Should not happen
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage()));
        } catch (ResponseStatusException e) { // Catch conflict exceptions
            return ResponseEntity.status(e.getStatusCode()).body(ApiResponse.error(e.getReason()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error updating user: " + e.getMessage()));
        }
    }

    @PatchMapping("/me/password")
    public ResponseEntity<?> updateCurrentUserPassword(@RequestBody Map<String, String> passwords) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl)) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("User not authenticated"));
        }

        UUID userId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
        String currentPassword = passwords.get("currentPassword");
        String newPassword = passwords.get("newPassword");

        // Basic validation
        if (currentPassword == null || newPassword == null || newPassword.length() < 4) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Invalid password data provided."));
        }

        try {
            // Use service method that handles hashing and verification
            userService.updatePassword(userId, currentPassword, newPassword);
            return ResponseEntity.ok(ApiResponse.success("Password updated successfully"));
        } catch (ResourceNotFoundException e) { // Should not happen
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage()));
        } catch (ResponseStatusException e) { // Catch incorrect current password / other issues
            return ResponseEntity.status(e.getStatusCode()).body(ApiResponse.error(e.getReason()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error updating password: " + e.getMessage()));
        }
    }

    // --- convertToDto helper method ---
    private UserDto convertToDto(User user) {
        if (user == null) {
            return null;
        }
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        return dto;
    }

    /**
     * Inner static class representing the payload for adding a product to the
     * shopping cart.
     */
    public static class ProductRequest {
        /**
         * The ID of the product to be added to the cart.
         */
        private UUID productId;

        /**
         * Retrieves the product ID from the request.
         *
         * @return The ID of the product to add.
         */
        public UUID getProductId() {
            return productId;
        }

        /**
         * Sets the product ID for the request.
         *
         * @param productId The ID of the product to add.
         */
        public void setProductId(UUID productId) {
            this.productId = productId;
        }
    }
}
