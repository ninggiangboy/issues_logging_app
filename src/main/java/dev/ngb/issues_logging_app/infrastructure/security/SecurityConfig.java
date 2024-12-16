package dev.ngb.issues_logging_app.infrastructure.security;

import dev.ngb.issues_logging_app.application.exception.UserNotFoundException;
import dev.ngb.issues_logging_app.domain.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.*;
import java.util.stream.Collectors;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private static final String[] AUTH_URLS_WHITELIST = {
            "/api-docs/**",
            "/swagger-ui/**",
            "/actuator/**",
            "/error",
            "/greeting/public",
            "/auth/login",
            "/auth/refresh",
            "/auth/reset-password",
    };

    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;

    public SecurityConfig(AuthenticationEntryPoint authenticationEntryPoint, AccessDeniedHandler accessDeniedHandler) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return (username) -> userRepository.findByEmail(username)
                .map(user -> User.builder()
                        .username(user.getId().toString())
                        .password(user.getPassword())
                        .authorities(user.getIsAdmin() ? getAdminAuthorities() : Collections.emptyList())
                        .accountLocked(user.getIsBlocked())
                        .disabled(false)
                        .build())
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));
    }

    public Collection<GrantedAuthority> getAdminAuthorities() {
        return List.of(new SimpleGrantedAuthority(AuthConstant.ROLE_PREFIX + AuthConstant.ADMIN_ROLE_NAME));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                        .requestMatchers(AUTH_URLS_WHITELIST).permitAll()
                        .anyRequest().authenticated()
        );
        http.oauth2ResourceServer(oauth2ResourceServer ->
                oauth2ResourceServer.jwt(jwt ->
                                jwt.jwtAuthenticationConverter(jwtConverter())
                        ).authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
        );
//                .addFilterBefore(jwtBlacklistFilter, JwtBlacklistFilter.class);
        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    public JwtAuthenticationConverter jwtConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new CustomJwtGrantedAuthoritiesConverter());
        return converter;
    }

    private static class CustomJwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
        @Override
        public Collection<GrantedAuthority> convert(Jwt jwt) {
            return Optional.ofNullable(jwt.getClaimAsStringList(AuthConstant.ROLE_CLAIMS_NAME))
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(role -> new SimpleGrantedAuthority(AuthConstant.ROLE_PREFIX + role))
                    .collect(Collectors.toList());
        }
    }
}
