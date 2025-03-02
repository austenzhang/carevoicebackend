package com.team7.carevoice.security;

import com.team7.carevoice.model.CareVoiceUser;
import com.team7.carevoice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service that implements {@link UserDetailsService} to load user-specific data for authentication.
 * Uses {@link CareVoiceUser} as the user model and provides a {@link UserPrincipal} with the user's details.
 * Also handles user registration and caching of user data.
 */
@Service
public class CareVoiceUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CareVoiceUserDetailsService.class);

    private final UserRepository userRepository;

    public CareVoiceUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Cacheable(value = "userProfileCache", key = "#username")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(user -> new UserPrincipal(
                        user.getId(),
                        user.getUsername(),
                        user.getPassword(),
                        user.getFirstName(),
                        user.getLastName()))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }
}

