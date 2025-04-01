package is.hi.verzla_backend.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Custom authentication entry point for handling unauthorized access attempts.
 *
 * <p>This component is triggered whenever an unauthenticated user attempts to access
 * a protected resource that requires authentication. It handles the HTTP response
 * for unauthorized requests, ensuring a consistent error format is returned.</p>
 *
 * <p>The entry point:
 * <ul>
 *   <li>Logs the unauthorized access attempt for monitoring and troubleshooting</li>
 *   <li>Returns an HTTP 401 Unauthorized status code to the client</li>
 *   <li>Includes a standard error message in the response body</li>
 * </ul>
 * </p>
 *
 * <p>This implementation is essential for proper security in a RESTful API context,
 * as it ensures that clients receive appropriate status codes when authentication
 * is required, rather than being redirected to a login page (which is the default
 * behavior in traditional web applications).</p>
 *
 * @see org.springframework.security.web.AuthenticationEntryPoint
 * @see is.hi.verzla_backend.security.WebSecurityConfig
 */
@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    /**
     * Logger for recording authentication failures and unauthorized access attempts
     */
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthEntryPoint.class);

    /**
     * Handles an unauthorized access attempt.
     *
     * <p>This method is called when a user attempts to access a protected resource without
     * being authenticated or with invalid credentials. It logs the access attempt and
     * returns an HTTP 401 Unauthorized response to the client.</p>
     *
     * @param request       The HTTP request that resulted in an authentication failure
     * @param response      The HTTP response to be modified with the unauthorized status
     * @param authException The exception that caused the authentication failure
     * @throws IOException      If an input or output error occurs while sending the response
     * @throws ServletException If a servlet error occurs while sending the response
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        logger.error("Unauthorized error: {}", authException.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
    }
}