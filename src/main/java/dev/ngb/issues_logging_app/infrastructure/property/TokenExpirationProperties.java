package dev.ngb.issues_logging_app.infrastructure.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "token.expiration")
public record TokenExpirationProperties(long refresh, long verification) {

}
