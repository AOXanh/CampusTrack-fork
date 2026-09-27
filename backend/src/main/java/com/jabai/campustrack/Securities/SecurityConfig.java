package com.jabai.campustrack.Securities;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    private final String[] UNAUTHORIZED_PATHS = {"/api/auth/register", "/api/auth/login", "/h2-console/**", "/error"};
    private final String[] AUTHORIZED_PATHS = {"/api/auth/**"};

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http.csrf(AbstractHttpConfigurer::disable)
            .headers(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> {
                auth.requestMatchers(UNAUTHORIZED_PATHS).permitAll()
                    .requestMatchers(AUTHORIZED_PATHS).authenticated()
                    .anyRequest().authenticated();
            });
        return http.build(); 
    }
}
