package com.jabai.campustrack.Securities;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    private final String[] UNAUTHORIZED_PATHS = {
            "/api/auth/register",
            "/api/auth/login",

            // Temporary development access
            "/api/assets/**",
            "/api/buildings/**",
            "/api/incidents/**",
            "/api/maintenance-records/**",
            "/api/nfc-tags/**",
            "/api/rooms/**",

            // swagger docs
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-ui.html",

            "/h2-console/**",
            "/error"
    };

    private final String[] AUTHORIZED_PATHS = {
            "/api/auth/**"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> {
                auth.requestMatchers(UNAUTHORIZED_PATHS).permitAll()
                    .requestMatchers(AUTHORIZED_PATHS).authenticated()
                    .anyRequest().authenticated();
            })
            .addFilterBefore(jwtFilter,UsernamePasswordAuthenticationFilter.class); //This configures the token filter before allowing spring security to run

        return http.build();
    }
}
