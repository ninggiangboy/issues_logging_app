package dev.ngb.issues_logging_app.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResult<T>(
        String message,
        T data
) {
}