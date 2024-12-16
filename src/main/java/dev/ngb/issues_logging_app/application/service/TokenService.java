package dev.ngb.issues_logging_app.application.service;

import dev.ngb.issues_logging_app.application.constant.TokenType;

import java.util.UUID;

public interface TokenService {
    String generateAndSaveUserToken(String userId, TokenType type);

    UUID getUserIdByToken(String token, TokenType type);

    void revokeToken(String token, TokenType type);
}
