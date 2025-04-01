package is.hi.verzla_backend.dto;

import java.util.UUID;

/**
 * Response DTO for successful login attempts in the Verzla API.
 *
 * <p>This class encapsulates all the data that should be returned to a client
 * after a successful authentication. It includes:
 * <ul>
 *   <li>Basic user information (ID, name, email)</li>
 *   <li>The JWT authentication token for subsequent authenticated requests</li>
 *   <li>The token type (always "Bearer" in this implementation)</li>
 * </ul>
 * </p>
 *
 * <p>The included JWT token should be stored by the client application and included
 * in the Authorization header of subsequent API requests using the format:
 * {@code Authorization: Bearer [token]}.</p>
 *
 * <p>This DTO plays a critical role in the stateless authentication flow:
 * <ol>
 *   <li>Client submits credentials via a LoginRequest</li>
 *   <li>Server authenticates and returns this LoginResponse with token</li>
 *   <li>Client stores token and includes it in future API requests</li>
 *   <li>Server validates token on each request to identify the user</li>
 * </ol>
 * </p>
 *
 * @see is.hi.verzla_backend.dto.LoginRequest
 * @see is.hi.verzla_backend.controllers.AuthController
 * @see is.hi.verzla_backend.security.JwtUtils
 */
public class LoginResponse {

    /**
     * The unique identifier of the authenticated user.
     */
    private UUID userId;

    /**
     * The display name of the authenticated user.
     */
    private String name;

    /**
     * The email address of the authenticated user, which typically serves as the username.
     */
    private String email;

    /**
     * The JWT token that should be used for authentication in subsequent API requests.
     */
    private String token;

    /**
     * The type of authentication token provided.
     * Always set to "Bearer" as per JWT authentication standards.
     */
    private String type = "Bearer";

    /**
     * Constructs a complete login response with user details and authentication token.
     *
     * @param userId The unique identifier of the authenticated user
     * @param name   The display name of the authenticated user
     * @param email  The email address of the authenticated user
     * @param token  The JWT token for authenticating subsequent requests
     */
    public LoginResponse(UUID userId, String name, String email, String token) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.token = token;
    }

    /**
     * Gets the user ID of the authenticated user.
     *
     * @return The unique identifier of the user
     */
    public UUID getUserId() {
        return userId;
    }

    /**
     * Sets the user ID of the authenticated user.
     *
     * @param userId The unique identifier to set
     */
    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    /**
     * Gets the display name of the authenticated user.
     *
     * @return The name of the user
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the display name of the authenticated user.
     *
     * @param name The name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the email address of the authenticated user.
     *
     * @return The email address of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the authenticated user.
     *
     * @param email The email address to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the JWT authentication token.
     *
     * @return The JWT token for authenticating subsequent requests
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the JWT authentication token.
     *
     * @param token The JWT token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Gets the token type (always "Bearer").
     *
     * @return The token type string
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the token type.
     *
     * @param type The token type to set
     */
    public void setType(String type) {
        this.type = type;
    }
}
