package com.team7.carevoice.services;

import com.team7.carevoice.dto.request.CreateUserRequest;
import com.team7.carevoice.model.CareVoiceUser;
import com.team7.carevoice.repository.UserRepository;
import com.team7.carevoice.security.AuthUserPrincipalService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service layer for managing user profile operations such as registration, profile updates,
 * and billing address management.
 */
@Service
public class CareVoiceUserProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthUserPrincipalService authUserPrincipalService;

    public CareVoiceUserProfileService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthUserPrincipalService authUserPrincipalService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authUserPrincipalService = authUserPrincipalService;
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username the username of the user
     * @return the CareVoiceUser entity
     * @throws IllegalArgumentException if the user is not found
     */
    @Transactional(readOnly = true)
    public CareVoiceUser getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
    }

    /**
     * Registers a new user.
     *
     * @param registerRequest the registration request containing user details
     * @return the newly registered CareVoiceUser entity
     * @throws IllegalArgumentException if the registration request is invalid or the username is already in use
     */
    @Transactional
    public CareVoiceUser registerUser(CreateUserRequest registerRequest) {
        validateRegistrationRequest(registerRequest);
        checkIfUsernameExists(registerRequest.getUsername());

        CareVoiceUser newUser = new CareVoiceUser(
                registerRequest.getUsername(),
                passwordEncoder.encode(registerRequest.getPassword()),
                registerRequest.getFirstName(),
                registerRequest.getLastName()
        );

        return userRepository.save(newUser);
    }

    /**
     * Validates the registration request.
     *
     * @param registerRequest the registration request to validate
     * @throws IllegalArgumentException if the request is null or required fields are missing
     */
    private void validateRegistrationRequest(CreateUserRequest registerRequest) {
        if (registerRequest == null || registerRequest.getUsername() == null || registerRequest.getPassword() == null) {
            throw new IllegalArgumentException("Registration details must not be null.");
        }
    }

    /**
     * Checks if a username already exists in the database.
     *
     * @param username the username to check
     * @throws IllegalArgumentException if the username is already in use
     */
    private void checkIfUsernameExists(String username) {
        userRepository.findByUsername(username).ifPresent(existing -> {
            throw new IllegalArgumentException("Username already in use: " + username);
        });
    }

    /**
     * Checks if a string is not blank.
     *
     * @param value the string to check
     * @return true if the string is not blank, false otherwise
     */
    private boolean isNotBlank(String value) {
        return value != null && !value.isBlank();
    }

    /**
     * Saves or updates a user in the database.
     *
     * @param user the user to save or update
     * @return the saved or updated CareVoiceUser entity
     */
    @Transactional
    public CareVoiceUser save(CareVoiceUser user) {
        return userRepository.save(user);
    }
}