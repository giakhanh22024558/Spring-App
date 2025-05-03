package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Arrays;


@Configuration
@EnableWebSecurity
public class KeycloakSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/store/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(
                        oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
                .cors();
        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter defaultConverter = new JwtGrantedAuthoritiesConverter();
        defaultConverter.setAuthorityPrefix(""); // Remove "SCOPE_" prefix

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Collection<GrantedAuthority> authorities = defaultConverter.convert(jwt);

            // Extract roles from Keycloak token
            List<String> realmRoles = extractRolesFromToken(jwt);
            List<GrantedAuthority> roleAuthorities = realmRoles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role)) // Ensure "ROLE_" prefix
                    .collect(Collectors.toList());

            // Merge Keycloak roles with default OAuth2 authorities
            roleAuthorities.addAll(authorities);
            return roleAuthorities;
        });

        return converter;
    }

    private List<String> extractRolesFromToken(Jwt jwt) {
        try {
            // Get the `resource_access` object from the JWT
            Map<String, Object> resourceAccess = jwt.getClaim("resource_access");

            if (resourceAccess != null && resourceAccess.containsKey("spring-boot-client")) {
                // Get the client-specific roles from `resource_access`
                Map<String, Object> clientAccess = (Map<String, Object>) resourceAccess.get("spring-boot-client");

                if (clientAccess != null && clientAccess.containsKey("roles")) {
                    // Extract roles as a List<String>
                    return (List<String>) clientAccess.get("roles");
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log the error for debugging
        }

        return Collections.emptyList(); // Return an empty list if roles are not found
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Arrays.asList("http://localhost:5173")); // Frontend URL
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE")); // Allowed methods
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept")); // Allowed headers
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}

