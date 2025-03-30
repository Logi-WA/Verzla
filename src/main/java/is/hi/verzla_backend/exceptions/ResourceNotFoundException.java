package is.hi.verzla_backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a requested resource cannot be found.
 * Results in HTTP 404 Not Found responses.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

  public ResourceNotFoundException(String message) {
    super(message);
  }

  public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
    super(String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue));
  }
}
