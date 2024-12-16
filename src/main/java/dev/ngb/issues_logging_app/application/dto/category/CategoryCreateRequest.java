package dev.ngb.issues_logging_app.application.dto.category;

public record CategoryCreateRequest(
        String name,
        String description
) {
}
