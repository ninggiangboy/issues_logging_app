package dev.ngb.issues_logging_app.application.exception;

import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class ValidationException extends RuntimeException {

    private final Map<String, List<String>> errors;
    private final Class<?> invalidClass;

    public ValidationException(Map<String, List<String>> errors, Class<?> invalidClass) {
        super("Validation failed");
        this.errors = errors;
        this.invalidClass = invalidClass;
    }
}
