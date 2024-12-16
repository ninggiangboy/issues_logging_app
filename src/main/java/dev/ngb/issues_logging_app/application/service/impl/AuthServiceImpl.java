package dev.ngb.issues_logging_app.application.service.impl;

import dev.ngb.issues_logging_app.application.constant.TokenType;
import dev.ngb.issues_logging_app.application.dto.auth.LoginRequest;
import dev.ngb.issues_logging_app.application.dto.auth.LoginResponse;
import dev.ngb.issues_logging_app.application.exception.ValidationException;
import dev.ngb.issues_logging_app.application.service.AuthService;
import dev.ngb.issues_logging_app.application.service.JwtService;
import dev.ngb.issues_logging_app.application.service.TokenService;
import dev.ngb.issues_logging_app.application.validator.ValidatorFactory;
import dev.ngb.issues_logging_app.common.validator.ValidatorChain;
import dev.ngb.issues_logging_app.domain.repository.UserRepository;
import dev.ngb.issues_logging_app.infrastructure.security.AuthConstant;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenService tokenService;
    private final ValidatorFactory validatorFactory;
    private final UserRepository userRepository;

    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtService jwtService, TokenService tokenService, ValidatorFactory validatorFactory, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.tokenService = tokenService;
        this.validatorFactory = validatorFactory;
        this.userRepository = userRepository;
    }

    @Override
    public LoginResponse authenticate(LoginRequest request) {
        ValidatorChain<LoginRequest> validator = validatorFactory.getValidator(LoginRequest.class);
        if (validator.isNotValid(request)) {
            throw new ValidationException(validator.getErrors(), LoginRequest.class);
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(), request.password()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String accessToken = jwtService.generateAccessToken(userDetails);
        Long accessTokenExpiration = jwtService.getAccessTokenExpiration();
        String refreshToken = tokenService.generateAndSaveUserToken(userDetails.getUsername(), TokenType.REFRESH);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiration(accessTokenExpiration)
                .build();
    }

    @Override
    public LoginResponse refresh(String refreshToken) {
        UUID userId = tokenService.getUserIdByToken(refreshToken, TokenType.REFRESH);
        UserDetails userDetails = loadUserDetails(userId);
        String accessToken = jwtService.generateAccessToken(userDetails);
        Long accessTokenExpiration = jwtService.getAccessTokenExpiration();
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiration(accessTokenExpiration)
                .build();
    }

    @Override
    public void invalidate(String refreshToken) {
        tokenService.revokeToken(refreshToken, TokenType.REFRESH);
    }

    private UserDetails loadUserDetails(UUID userId) {
        return userRepository.findById(userId)
                .map(user -> User.builder()
                        .username(user.getId().toString())
                        .password("")
                        .authorities(user.getIsAdmin() ? getAdminAuthorities() : Collections.emptyList())
                        .accountLocked(user.getIsBlocked())
                        .disabled(false)
                        .build())
                .filter(user -> !user.isAccountNonLocked())
                .filter(user -> !user.isAccountNonExpired())
                .filter(user -> !user.isEnabled())
                .filter(user -> !user.isCredentialsNonExpired())
                .orElseThrow(() -> new BadCredentialsException("User can't be authenticated"));
    }

    public Collection<GrantedAuthority> getAdminAuthorities() {
        return List.of(new SimpleGrantedAuthority(AuthConstant.ROLE_PREFIX + AuthConstant.ADMIN_ROLE_NAME));
    }
}
