package dev.ngb.issues_logging_app.common.util;

import dev.ngb.issues_logging_app.infrastructure.security.AuthConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;
import java.util.UUID;

@Slf4j
public class SecurityUtils {

    public static boolean isCurrentUserAdmin() {
        String adminRole = AuthConstant.ROLE_PREFIX + AuthConstant.ADMIN_ROLE_NAME;
        return isCurrentHasRole(adminRole);
    }

    public static boolean isCurrentHasRole(String role) {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getAuthorities)
                .map(grantedAuthorities -> grantedAuthorities.stream()
                        .anyMatch(grantedAuthority -> grantedAuthority
                                .getAuthority().equals(role)))
                .orElse(false);
    }

    public static UUID getCurrentUserId() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getPrincipal)
                .map(principal -> {
                    if (principal instanceof UserDetails userDetails) {
                        return userDetails.getUsername();
                    } else if (principal instanceof Jwt jwt) {
                        return jwt.getSubject();
                    } else {
                        return principal.toString();
                    }
                })
                .map(UUID::fromString)
                .orElse(null);
    }

}