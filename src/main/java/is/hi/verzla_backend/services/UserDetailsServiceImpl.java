package is.hi.verzla_backend.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import is.hi.verzla_backend.entities.User;
import is.hi.verzla_backend.repositories.UserRepository;
import is.hi.verzla_backend.security.UserDetailsImpl;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByEmail(username);

        // Use orElseThrow to get the User or throw the exception if Optional is empty
        User user = userOptional
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        // Build the UserDetails object from the found User entity
        return UserDetailsImpl.build(user);
    }
}