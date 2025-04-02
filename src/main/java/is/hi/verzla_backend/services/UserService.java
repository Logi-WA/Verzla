package is.hi.verzla_backend.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import is.hi.verzla_backend.dto.SignUpDto;
import is.hi.verzla_backend.dto.UpdateUserDto;
import is.hi.verzla_backend.dto.UserDto;
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
    List<UserDto> getAllUsers();

    /**
     * Retrieves a user by their ID.
     *
     * @param id The ID of the user to retrieve.
     * @return The {@link User} object with the specified ID.
     */
    Optional<User> getUserById(UUID id);

    User createUserFromDto(SignUpDto signUpDto);

    User updateUser(UUID id, UpdateUserDto userDto);

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
     * @param currentPassword The current password of the user.
     * @param newPassword The new password for the user.
     * @return The updated {@link User} object.
     */
    User updatePassword(UUID id, String currentPassword, String newPassword);

    /**
     * Retrieves a user by their email address.
     *
     * @param email The email address of the user to retrieve.
     * @return The {@link User} object with the specified email.
     */
    Optional<User> getUserByEmail(String email);

    boolean existsByEmail(String email);
}
