package is.hi.verzla_backend.dto;

import java.util.UUID;

/**
 * Response DTO for successful login attempts.
 * Contains user information for client applications.
 */
public class LoginResponse {
    private UUID userId;
    private String name;
    private String email;
    private String sessionId;

    public LoginResponse(UUID userId, String name, String email, String sessionId) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.sessionId = sessionId;
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

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
