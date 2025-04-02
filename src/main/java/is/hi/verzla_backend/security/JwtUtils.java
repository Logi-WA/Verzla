package is.hi.verzla_backend.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

/**
 * Utility class for JSON Web Token (JWT) operations in the Verzla backend.
 *
 * <p>This class provides functionality for:
 * <ul>
 *   <li>Generating new JWT tokens with user information</li>
 *   <li>Validating existing JWT tokens</li>
 *   <li>Extracting user information from tokens (username and user ID)</li>
 * </ul>
 * </p>
 *
 * <p>JWT tokens are used to authenticate users in a stateless manner, allowing the API
 * to verify the identity of users without maintaining session state. Each token contains
 * encrypted user information and is signed to prevent tampering.</p>
 *
 * <p>The tokens created by this utility include:
 * <ul>
 *   <li>The username as the subject claim</li>
 *   <li>The user's UUID as a custom claim</li>
 *   <li>Issuance and expiration timestamps</li>
 *   <li>A digital signature using HMAC-SHA256</li>
 * </ul>
 * </p>
 */
@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    /**
     * Secret key used to sign the JWT tokens, configured in application properties.
     * A default value is provided for development environments.
     */
    @Value("${app.jwt.secret:defaultSecretKey12345678901234567890}")
    private String jwtSecret;

    /**
     * Token expiration time in milliseconds, configured in application properties.
     * The default is 24 hours (86400000 milliseconds).
     */
    @Value("${app.jwt.expiration:86400000}")
    private int jwtExpirationMs;

    /**
     * Generates a new JWT token for a user.
     *
     * <p>This method creates a new token containing the user's username and ID,
     * sets the issuance time to now, and calculates the expiration time based on
     * the configured expiration period.</p>
     *
     * @param username The username (typically email) to store in the token
     * @param userId   The UUID of the user to store in the token
     * @return A signed JWT token string
     */
    public String generateJwtToken(String username, UUID userId) {
        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extracts the username from a JWT token.
     *
     * <p>This method parses the token, verifies its signature, and extracts
     * the subject claim which contains the username.</p>
     *
     * @param token The JWT token string to parse
     * @return The username contained in the token
     * @throws JwtException if the token is invalid or expired
     */
    public String getUsernameFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Extracts the user ID from a JWT token.
     *
     * <p>This method parses the token, verifies its signature, and extracts
     * the custom "userId" claim which contains the user's UUID.</p>
     *
     * @param token The JWT token string to parse
     * @return The UUID of the user contained in the token
     * @throws JwtException if the token is invalid or expired
     */
    public UUID getUserIdFromJwtToken(String token) {
        String userId = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("userId", String.class);
        return UUID.fromString(userId);
    }

    /**
     * Validates a JWT token.
     *
     * <p>This method attempts to parse the token and verify its signature.
     * If parsing is successful, the token is considered valid. If any
     * exception occurs during parsing (e.g., expired token, invalid signature),
     * the token is considered invalid.</p>
     *
     * @param authToken The JWT token string to validate
     * @return true if the token is valid, false otherwise
     */
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(authToken);
            return true;
        } catch (JwtException e) {
            logger.error("JWT token validation error: {}", e.getMessage());
        }
        return false;
    }

    /**
     * Creates a signing key from the configured secret.
     *
     * <p>This method converts the string secret key into a cryptographic key
     * that can be used for signing and verifying JWT tokens.</p>
     *
     * @return A Key object suitable for HMAC-SHA256 signing
     */
    private Key getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}