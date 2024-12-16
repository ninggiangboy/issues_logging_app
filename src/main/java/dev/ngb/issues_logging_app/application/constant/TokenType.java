package dev.ngb.issues_logging_app.application.constant;

import dev.ngb.issues_logging_app.infrastructure.cache.CacheConstant;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TokenType {
    REFRESH(CacheConstant.REFRESH_TOKEN_KEY_PATTERN),
    VERIFICATION(CacheConstant.VERIFICATION_TOKEN_KEY_PATTERN);

    private final String cacheKeyPattern;

}
