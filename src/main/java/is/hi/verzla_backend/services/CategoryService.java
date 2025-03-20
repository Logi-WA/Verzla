package is.hi.verzla_backend.services;

import is.hi.verzla_backend.entities.Category;
import java.util.List;

/**
 * Service interface for handling operations related to product categories.
 * Provides methods to retrieve categories.
 */
public interface CategoryService {

  /**
   * Retrieves a list of all available product categories.
   *
   * @return A list of {@link Category} objects.
   */
  List<Category> getAllCategories();
}
