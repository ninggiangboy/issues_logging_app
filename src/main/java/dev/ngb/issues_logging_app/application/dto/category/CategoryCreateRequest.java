package dev.ngb.issues_logging_app.application.dto.category;

import lombok.experimental.FieldNameConstants;

@FieldNameConstants
public record CategoryCreateRequest(
        String name
) {
}
