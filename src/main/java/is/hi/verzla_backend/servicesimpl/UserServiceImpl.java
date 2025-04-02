package is.hi.verzla_backend.servicesimpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import is.hi.verzla_backend.dto.SignUpDto;
import is.hi.verzla_backend.dto.UpdateUserDto;
import is.hi.verzla_backend.dto.UserDto;
import is.hi.verzla_backend.entities.Cart;
import is.hi.verzla_backend.entities.User;
import is.hi.verzla_backend.entities.Wishlist;
import is.hi.verzla_backend.exceptions.ResourceNotFoundException;
import is.hi.verzla_backend.repositories.UserRepository;
import is.hi.verzla_backend.services.UserService;

/**
 * Implementation of the {@link UserService} interface. Provides methods for
 * managing users, including
 * creating, updating, retrieving, and deleting user accounts.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public User createUserFromDto(SignUpDto dto) {
        if (existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email address already in use.");
        }

        User newUser = new User();
        newUser.setName(dto.getName());
        newUser.setEmail(dto.getEmail());
        // HASH the password before setting it
        newUser.setPassword(passwordEncoder.encode(dto.getPassword()));

        // Create Cart and Wishlist for the new user
        initializeCartAndWishlist(newUser);

        return userRepository.save(newUser);
    }

    // Helper method to initialize Cart/Wishlist
    private void initializeCartAndWishlist(User user) {
        if (user.getCart() == null) {
            Cart cart = new Cart();
            cart.setUser(user); // Set bidirectional relationship
            user.setCart(cart);
        }
        if (user.getWishlist() == null) {
            Wishlist wishlist = new Wishlist();
            wishlist.setUser(user); // Set bidirectional relationship
            user.setWishlist(wishlist);
        }
    }

    @Override
    public User updateUser(UUID id, UpdateUserDto userDto) {
        User user = this.getUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));

        // Update name
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        // Update email if provided and different, checking for conflicts
        if (userDto.getEmail() != null && !userDto.getEmail().equals(user.getEmail())) {
            if (existsByEmail(userDto.getEmail())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Email address already in use.");
            }
            user.setEmail(userDto.getEmail());
        }

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(UUID userId) {
        User user = this.getUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
        userRepository.delete(user);
    }

    @Override
    public User updatePassword(UUID id, String currentPasswordPlain, String newPasswordPlain) {
        // Fetch the user or throw if not found
        User user = this.getUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));

        // Verify current password
        if (!passwordEncoder.matches(currentPasswordPlain, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Current password does not match.");
        }

        // Hash the new password before saving
        user.setPassword(passwordEncoder.encode(newPasswordPlain));
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    private UserDto convertToDto(User user) {
        if (user == null) {
            return null;
        }
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        return dto;
    }
}
