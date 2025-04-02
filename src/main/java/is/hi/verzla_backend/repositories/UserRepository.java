package is.hi.verzla_backend.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import is.hi.verzla_backend.entities.User;

/**
 * Repository interface for performing CRUD operations on {@link User} entities.
 *
 * <p>This repository provides methods to interact with user data in the database,
 * including standard JPA Repository operations and custom query methods for user-specific
 * requirements.</p>
 *
 * <p>Key operations supported:
 * <ul>
 *   <li>Creating new user accounts</li>
 *   <li>Retrieving users by ID or email address</li>
 *   <li>Updating user information</li>
 *   <li>Deleting user accounts</li>
 * </ul>
 * </p>
 *
 * <p>User authentication and profile management in the Verzla application rely heavily
 * on this repository's ability to efficiently locate and manipulate user records.</p>
 *
 * @see is.hi.verzla_backend.entities.User
 * @see is.hi.verzla_backend.services.UserService
 * @see is.hi.verzla_backend.controllers.UserController
 */
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Finds a user by their email address.
     *
     * <p>This method is primarily used during authentication to locate user accounts
     * based on the email address provided in login credentials. It is also used to
     * check for existing accounts when registering new users to prevent duplicate
     * email addresses.</p>
     *
     * @param email The email address of the user to be found
     * @return The {@link User} entity with the specified email, or null if no user is found
     * @throws IllegalArgumentException if email is null
     */
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
