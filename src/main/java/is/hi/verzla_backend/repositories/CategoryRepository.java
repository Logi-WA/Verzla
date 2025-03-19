package is.hi.verzla_backend.repositories;

import java.util.UUID;

import is.hi.verzla_backend.entities.Category;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for performing CRUD operations on {@link Category} entities.
 */
public interface CategoryRepository extends JpaRepository<Category, UUID> {
}
