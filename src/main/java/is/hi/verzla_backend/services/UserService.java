package is.hi.verzla_backend.services;

import java.util.List;
import java.util.UUID;

import is.hi.verzla_backend.entities.User;

/**
 * Service interface for managing users.
 * Provides methods to create, retrieve, update, and delete users, as well as
 * manage passwords.
 */
public interface UserService {

  /**
   * Retrieves a list of all users.
   *
   * @return A list of {@link User} objects.
   */
  List<User> getAllUsers();

  /**
   * Retrieves a user by their ID.
   *
   * @param id The ID of the user to retrieve.
   * @return The {@link User} object with the specified ID.
   */
  User getUserById(UUID id);

  /**
   * Creates a new user.
   *
   * @param user The {@link User} object to be created.
   * @return The created {@link User} object.
   */
  User createUser(User user);

  /**
   * Updates an existing user's details.
   *
   * @param id          The ID of the user to update.
   * @param userDetails The new details for the user.
   * @return The updated {@link User} object.
   */
  User updateUser(UUID id, User userDetails);

  /**
   * Deletes a user by their ID.
   *
   * @param userId The ID of the user to delete.
   */
  void deleteUser(UUID userId);

  /**
   * Updates the password for a specific user.
   *
   * @param id          The ID of the user whose password will be updated.
   * @param newPassword The new password for the user.
   * @return The updated {@link User} object.
   */
  User updatePassword(UUID id, String newPassword);

  /**
   * Retrieves a user by their email address.
   *
   * @param email The email address of the user to retrieve.
   * @return The {@link User} object with the specified email.
   */
  User getUserByEmail(String email);
}
