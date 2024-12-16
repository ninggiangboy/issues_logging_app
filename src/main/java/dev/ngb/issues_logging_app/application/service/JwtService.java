package dev.ngb.issues_logging_app.application.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    /**
     * Generates an access token for the given user.
     *
     * @param user the user details
     * @return the generated access token
     */
    String generateAccessToken(UserDetails user);

    Long getAccessTokenExpiration();
}
