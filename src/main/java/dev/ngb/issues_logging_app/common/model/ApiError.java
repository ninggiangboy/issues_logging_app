package dev.ngb.issues_logging_app.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiError(
        String message,
        String path,
        Map<String, List<String>> errors
) {
}
