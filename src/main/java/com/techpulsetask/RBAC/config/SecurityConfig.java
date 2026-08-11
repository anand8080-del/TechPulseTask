package com.techpulsetask.RBAC.config;


import com.techpulsetask.RBAC.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * HTTP-layer security: decides WHO can reach the app (authentication) and
 * which endpoints require login at all. This deliberately does NOT decide
 * WHAT an authenticated user can do - that is fully delegated to
 * @PreAuthorize + RbacPermissionEvaluator at the method layer, keeping
 * authentication and authorization cleanly separated.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(CustomUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Stateless REST API - no browser session/cookie to forge, so
            // CSRF protection (designed for session-cookie based apps)
            // doesn't apply here.
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth
                // H2 console left open for local dev/inspection only
                .requestMatchers("/h2-console/**").permitAll()
                // Every other endpoint requires SOME authenticated user.
                // Fine-grained WHAT-can-they-do checks happen via
                // @PreAuthorize on each controller method.
                .anyRequest().authenticated()
            )

            .httpBasic(Customizer.withDefaults())

            // No server-side session state - credentials are sent with
            // every Basic Auth request, so there's nothing to store.
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // H2 console renders in an iframe; default Spring Security
            // header would block that. SAMEORIGIN keeps it usable for
            // local dev without opening it up to other sites.
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }
}