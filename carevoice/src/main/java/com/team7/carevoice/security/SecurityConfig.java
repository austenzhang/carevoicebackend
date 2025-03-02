package com.team7.carevoice.security;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    private final CareVoiceUserDetailsService userDetailsService;
    private final AuthTokenFilterUtil authTokenFilterUtil;

    /**
     * Constructor to inject dependencies.
     *
     * @param userDetailsService  the custom UserDetailsService implementation
     * @param authTokenFilterUtil the JWT authentication filter
     */
    @Lazy
    public SecurityConfig(CareVoiceUserDetailsService userDetailsService, AuthTokenFilterUtil authTokenFilterUtil) {
        this.userDetailsService = userDetailsService;
        this.authTokenFilterUtil = authTokenFilterUtil;
    }

    /**
     * Configures the security filter chain, defining public and protected
     * endpoints.
     *
     * @param http the HttpSecurity instance
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs configuring security
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // Define public and protected endpoints directly here
        List<String> publicEndpoints = List.of(
                "/api/auth/login/**",
                "/api/auth/register/**",
                "/public/**", // Static files and public resources
                "/static/**",
                "/api/patient/**",
                "/api/patients/**",
                "/api/DARP/**",
                "/api/head-to-toe/**",
                "/api/summary/**",
                "/api/summary/patient/**",
                "/api/transcript/**"
                );       


        List<String> protectedEndpoints = List.of(
                "/api/user/profile/**",
                "/api/user/profile/billing-address/**");

        http
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfig = new org.springframework.web.cors.CorsConfiguration();
                    corsConfig.setAllowedOrigins(List.of("https://carevoicefrontend-92e5a3f12555.herokuapp.com/", "https://carevoicefrontendexpress-1b06bd543d5f.herokuapp.com/")); // Frontend URL
                    corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
                    corsConfig.setAllowedHeaders(List.of("*"));
                    corsConfig.setAllowCredentials(true);
                    return corsConfig;
                }))
                .csrf(csrf -> csrf.disable()) // Disable CSRF for stateless APIs
                .authorizeHttpRequests(auth -> auth
                        // Permit all requests to public endpoints
                        .requestMatchers(publicEndpoints.toArray(new String[0])).permitAll()
                        // Require authentication for protected endpoints
                        .requestMatchers(protectedEndpoints.toArray(new String[0])).authenticated()
                        // Require authentication for any other requests
                        .anyRequest().authenticated())
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout") // Define the logout endpoint
                        .logoutSuccessHandler((request, response, authentication) -> {
                            logger.info("User logged out successfully: {}",
                                    authentication != null ? authentication.getName() : "Anonymous");
                            response.setStatus(HttpServletResponse.SC_OK);
                            response.getWriter().write("Logout successful");
                        })
                        .invalidateHttpSession(true) // Invalidate the user's session
                        .deleteCookies("JSESSIONID") // Delete session cookies (if any)
                )
                .authenticationProvider(daoAuthenticationProvider()) // Use custom UserDetailsService
                .addFilterBefore(authTokenFilterUtil, UsernamePasswordAuthenticationFilter.class); // Add JWT filter

        logger.info("Security filter chain configured with CORS.");
        return http.build();
    }

    /**
     * Configures the password encoder for encoding and validating passwords.
     *
     * @return a BCryptPasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the AuthenticationManager using AuthenticationConfiguration.
     *
     * @param authenticationConfiguration the configuration for authentication
     * @return the configured AuthenticationManager
     * @throws Exception if an error occurs creating the AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Configures a DaoAuthenticationProvider using the custom UserDetailsService
     * and PasswordEncoder.
     *
     * @return a configured DaoAuthenticationProvider
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
}
