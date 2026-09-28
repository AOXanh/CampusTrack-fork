package com.jabai.campustrack.Securities;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * <p>NOTE: DO NOT VIOLATE LAYERS STRUCTURE.</p>
 *
 * <p>JADE: Continue dri bai and pag add ug policy which user role ang mga perform CRUD each tables and unsay mga permissions nila ;D</p>
 *
 * <p>Layer structure:</p>
 * <ul>
 *   <li>Controller layer -> DTO (with annotations)</li>
 *   <li>Service layer -> DTO</li>
 *   <li>Repository layer -> Model</li>
 * </ul>
 */
@Configuration
public class SecurityConfig {
    private final String[] UNAUTHORIZED_PATHS = {
            "/api/auth/register",
            "/api/auth/login",

            // Temporary rani ha
            "/api/assets/**",
            "/api/buildings/**",
            "/api/incidents/**",
            "/api/maintenance-records/**",
            "/api/nfc-tags/**",
            "/api/rooms/**",

            "/h2-console/**",
            "/error"
    };
    private final String[] AUTHORIZED_PATHS = {"/api/auth/**"};

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http.csrf(AbstractHttpConfigurer::disable)
            .headers(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable) // <--- Temporary ra sad ni
            .authorizeHttpRequests(auth -> {
                auth.requestMatchers(UNAUTHORIZED_PATHS).permitAll()
                    .requestMatchers(AUTHORIZED_PATHS).authenticated()
                    .anyRequest().authenticated();
            });
        return http.build(); 
    }
}
