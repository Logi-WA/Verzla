package is.hi.verzla_backend.dto;

import java.util.UUID;

/**
 * Response DTO for successful login attempts.
 * Contains user information and JWT token for client applications.
 */
public class LoginResponse {
    private UUID userId;
    private String name;
    private String email;
    private String token;
    private String type = "Bearer";

    public LoginResponse(UUID userId, String name, String email, String token) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.token = token;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
