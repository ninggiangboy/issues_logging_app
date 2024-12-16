package dev.ngb.issues_logging_app.application.dto.tag;

import lombok.experimental.FieldNameConstants;

@FieldNameConstants
public record TagCreateRequest(
        String name
) {
}
