package is.hi.verzla_backend.dto;

import java.time.LocalDateTime;

/**
 * Standard response format for all API endpoints in the Verzla backend.
 * 
 * <p>This class provides a consistent structure for all responses returned by the API,
 * whether they represent successful operations or errors. It encapsulates:
 * <ul>
 *   <li>A success flag indicating if the operation succeeded or failed</li>
 *   <li>A human-readable message describing the operation result</li>
 *   <li>The actual data payload (when the operation is successful)</li>
 *   <li>A timestamp recording when the response was created</li>
 * </ul>
 * </p>
 * 
 * <p>The class uses a generic type parameter to allow for different types of data
 * payloads to be returned while maintaining the same standardized envelope structure.
 * This ensures that client applications can handle API responses consistently.</p>
 * 
 * <p>Static factory methods are provided for creating common response types:
 * <ul>
 *   <li>{@code success(data)} - Creates a success response with default message</li>
 *   <li>{@code success(message, data)} - Creates a success response with custom message</li>
 *   <li>{@code error(message)} - Creates an error response with the specified message</li>
 * </ul>
 * </p>
 *
 * @param <T> The type of data payload being returned (null for error responses)
 */
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    /**
     * Default constructor.
     * Initializes the timestamp to the current date and time.
     */
    public ApiResponse() {
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Constructor for creating a response without a data payload.
     * Typically used for error responses.
     *
     * @param success Whether the operation was successful
     * @param message A descriptive message about the operation result
     */
    public ApiResponse(boolean success, String message) {
        this();
        this.success = success;
        this.message = message;
    }

    /**
     * Constructor for creating a complete response with data payload.
     * Typically used for successful operations that return data.
     *
     * @param success Whether the operation was successful
     * @param message A descriptive message about the operation result
     * @param data The data payload to be returned to the client
     */
    public ApiResponse(boolean success, String message, T data) {
        this(success, message);
        this.data = data;
    }

    /**
     * Creates a success response with a custom message and data payload.
     *
     * @param <T> The type of data being returned
     * @param message A descriptive message about the successful operation
     * @param data The data payload to be returned to the client
     * @return A new ApiResponse object indicating success
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    /**
     * Creates a success response with a default message and data payload.
     *
     * @param <T> The type of data being returned
     * @param data The data payload to be returned to the client
     * @return A new ApiResponse object indicating success
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "Operation successful", data);
    }

    /**
     * Creates an error response with a specified error message.
     *
     * @param <T> The type parameter (not used for error responses)
     * @param message A descriptive error message explaining what went wrong
     * @return A new ApiResponse object indicating failure
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message);
    }

    // Getters and setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
