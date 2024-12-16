package dev.ngb.issues_logging_app.application.dto.auth;

import jakarta.validation.constraints.NotNull;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
public record LoginRequest(
        @NotNull
        String email,
        @NotNull
        String password
) {
}
