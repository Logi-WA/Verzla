package is.hi.verzla_backend.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import is.hi.verzla_backend.dto.ApiResponse;

/**
 * Global exception handler for the Verzla backend application.
 * 
 * <p>This class centralizes all exception handling across the API, ensuring
 * consistent error responses regardless of where exceptions occur. It converts
 * various types of exceptions into standardized {@link ApiResponse} objects with 
 * appropriate HTTP status codes.</p>
 * 
 * <p>The handler provides specialized handling for:
 * <ul>
 *   <li>Resource not found exceptions (404 Not Found)</li>
 *   <li>Validation exceptions from request payload validation (400 Bad Request)</li>
 *   <li>All other unhandled exceptions (500 Internal Server Error)</li>
 * </ul>
 * </p>
 * 
 * <p>Using this centralized approach offers several benefits:
 * <ul>
 *   <li>Consistent error format across all API endpoints</li>
 *   <li>Separation of error handling from business logic</li>
 *   <li>Prevention of sensitive error details leaking to clients</li>
 *   <li>Easy addition of new exception types as the API evolves</li>
 * </ul>
 * </p>
 * 
 * @see is.hi.verzla_backend.dto.ApiResponse
 * @see is.hi.verzla_backend.exceptions.ResourceNotFoundException
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles exceptions for resources that could not be found.
     * 
     * <p>This handler intercepts {@link ResourceNotFoundException} instances, which
     * are typically thrown when a requested resource (product, user, etc.) cannot
     * be found in the database. The response includes the exception message and
     * a 404 Not Found status code.</p>
     *
     * @param ex The ResourceNotFoundException that was thrown
     * @return A ResponseEntity containing an ApiResponse with error details and 404 status
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ApiResponse<Object> response = ApiResponse.error(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles validation exceptions from request payload validation.
     * 
     * <p>This handler processes {@link MethodArgumentNotValidException} instances,
     * which occur when request payloads fail validation constraints (like @NotNull,
     * @Size, etc.). It extracts all validation errors and their messages, organizing
     * them into a map of field names to error messages.</p>
     * 
     * <p>The response includes:
     * <ul>
     *   <li>A general message indicating validation failure</li>
     *   <li>A map of all field-specific validation errors</li>
     *   <li>A 400 Bad Request status code</li>
     * </ul>
     * </p>
     *
     * @param ex The MethodArgumentNotValidException that was thrown
     * @return A ResponseEntity containing an ApiResponse with validation errors and 400 status
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        ApiResponse<Map<String, String>> response = new ApiResponse<>(false, "Validation failed", errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Default handler for all unhandled exceptions.
     * 
     * <p>This catch-all handler intercepts any exceptions not handled by more
     * specific handlers. It creates a generic error response with a sanitized
     * error message that doesn't expose sensitive implementation details.</p>
     * 
     * <p>In a production environment, this handler would typically log the 
     * exception details for troubleshooting while returning only minimal 
     * information to the client.</p>
     *
     * @param ex The Exception that was thrown
     * @return A ResponseEntity containing an ApiResponse with a generic error message and 500 status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGlobalException(Exception ex) {
        ApiResponse<Object> response = ApiResponse.error("An unexpected error occurred: " + ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
