package dev.ngb.issues_logging_app.application.dto.auth;

import dev.ngb.issues_logging_app.infrastructure.security.AuthConstant;
import lombok.Builder;

@Builder
public record LoginResponse(
        String accessToken,
        String refreshToken,
        String accessTokenType,
        Long expiration
) {
    public LoginResponse {
        accessTokenType = AuthConstant.ACCESS_TOKEN_TYPE;
    }
}
