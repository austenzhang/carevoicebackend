package com.team7.carevoice.facade;

import com.team7.carevoice.dto.request.CreateUserRequest;
import com.team7.carevoice.model.*;
import com.team7.carevoice.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Facade for handling the registration flow, including user, billing address, and payment type management.
 */
@Service
public class RegistrationFacade {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationFacade.class);

    private final CareVoiceUserProfileService userProfileService;

    public RegistrationFacade(
            CareVoiceUserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    /**
     * Handles the complete registration process.
     *
     * @param registerRequest      The registration request with user
     * @return                     The registered AcmePlexUser.
     */
    @Transactional
    public CareVoiceUser registerUser(CreateUserRequest registerRequest) {
        try {
            logger.info("Starting user registration for email: {}", registerRequest.getUsername());

            CareVoiceUser newUser = registerAndFetchUser(registerRequest);
            logger.info("Registration process completed successfully for user: {}", registerRequest.getUsername());
            return newUser;

        } catch (Exception ex) {
            logger.error("Unexpected error during registration for user: {}", registerRequest.getUsername(), ex);
            throw new IllegalStateException("An unexpected error occurred during registration: " + ex.getMessage());
        }
    }

    private CareVoiceUser registerAndFetchUser(CreateUserRequest createUserRequest) {
        try {
            userProfileService.registerUser(createUserRequest);
            CareVoiceUser newUser = userProfileService.getUserByUsername(createUserRequest.getUsername());
            logger.debug("User created: {}", newUser.getUsername());
            return newUser;
        } catch (IllegalArgumentException ex) {
            logger.error("User registration failed for email: {}", createUserRequest.getUsername(), ex);
            throw new IllegalArgumentException("User registration failed: " + ex.getMessage());
        }
    }
}
