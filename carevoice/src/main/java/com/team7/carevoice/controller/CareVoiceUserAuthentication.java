package com.team7.carevoice.controller;

import com.team7.carevoice.dto.request.ConnectCareLoginRequest;
import com.team7.carevoice.dto.request.CreateUserRequest;
import com.team7.carevoice.dto.response.*;
import com.team7.carevoice.facade.RegistrationFacade;
import com.team7.carevoice.model.CareVoiceUser;
import com.team7.carevoice.security.AuthTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class CareVoiceUserAuthentication {

    private static final Logger logger = LoggerFactory.getLogger(CareVoiceUserAuthentication.class);

    private final AuthenticationManager authenticationManager;
    private final AuthTokenUtil authTokenUtil;
    private final RegistrationFacade registrationFacade;

    public CareVoiceUserAuthentication(AuthenticationManager authenticationManager, AuthTokenUtil authTokenUtil, RegistrationFacade registrationFacade) {
        this.authenticationManager = authenticationManager;
        this.authTokenUtil = authTokenUtil;
        this.registrationFacade = registrationFacade;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody ConnectCareLoginRequest request) {
        logger.info("Login request received for email: {}", request.getUsername());

        // Authenticate the user
        org.springframework.security.core.Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // Generate a token for the embedded app
        String token = authTokenUtil.generateToken(request.getUsername());
        logger.info("User {} authenticated successfully.", request.getUsername());

        // Return the token and username to the embedded app
        AuthenticationResponse response = new AuthenticationResponse(token, request.getUsername());
        return ResponseEntity.ok(new ApiResponse<>(true, "Login successful", response));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody CreateUserRequest request) {
        logger.info("Registration request received for email: {}", request.getUsername());

        CareVoiceUser newUser = registrationFacade.registerUser(request);

        String token = authTokenUtil.generateToken(newUser.getUsername());
        logger.info("User {} registered successfully.", request.getUsername());

        AuthenticationResponse response = new AuthenticationResponse(token, newUser.getUsername());
        return ResponseEntity.ok(new ApiResponse<>(true, "User registered successfully", response));
    }
}

