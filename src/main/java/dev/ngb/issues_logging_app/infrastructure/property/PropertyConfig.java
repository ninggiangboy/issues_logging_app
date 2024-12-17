package dev.ngb.issues_logging_app.infrastructure.property;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({JwtProperty.class, TokenExpirationProperties.class})
public class PropertyConfig {
}
