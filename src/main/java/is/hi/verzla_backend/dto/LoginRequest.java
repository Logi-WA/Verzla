package is.hi.verzla_backend.dto;

/**
 * Data Transfer Object for user login requests in the Verzla API.
 *
 * <p>This DTO encapsulates the credentials provided by a user during login attempts.
 * It includes the username (which is typically the user's email address) and password
 * for authentication purposes.</p>
 *
 * <p>The class serves as an intermediary between the client request and the
 * authentication logic in the backend, separating concerns and improving security by:
 * <ul>
 *   <li>Isolating request data structure from entity models</li>
 *   <li>Providing a clean input object for validation</li>
 *   <li>Handling credential transmission without exposing user entity details</li>
 * </ul>
 * </p>
 *
 * <p>This DTO is primarily used by the {@code AuthController} to authenticate users
 * and generate JWT tokens for successful authentication.</p>
 *
 * @see is.hi.verzla_backend.controllers.AuthController
 * @see is.hi.verzla_backend.dto.LoginResponse
 */
public class LoginRequest {

    /**
     * The username for authentication, typically the user's email address.
     */
    private String username;

    /**
     * The password for authentication, should be sent securely (over HTTPS).
     */
    private String password;

    /**
     * Gets the username for authentication.
     *
     * @return The username (typically email) provided by the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username for authentication.
     *
     * @param username The username (typically email) to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password for authentication.
     *
     * @return The password provided by the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password for authentication.
     *
     * @param password The password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
