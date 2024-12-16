package dev.ngb.issues_logging_app.api.advice;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.ngb.issues_logging_app.application.exception.NotFoundException;
import dev.ngb.issues_logging_app.application.exception.ValidationException;
import dev.ngb.issues_logging_app.common.factory.ResponseFactory;
import dev.ngb.issues_logging_app.common.model.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "Internal server error";
    private static final String VALIDATION_ERROR_MESSAGE = "Validation error";

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleException(Exception e, HttpServletRequest request) {
        log.error("Error: {} {}", e.getClass().getName(), e.getMessage());
        return ResponseFactory.createErrorResponse(INTERNAL_SERVER_ERROR_MESSAGE, request.getRequestURI());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ApiError handleMethodNotAllowedException(Exception e, HttpServletRequest request) {
        return ResponseFactory.createErrorResponse(e.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler({
            NoResourceFoundException.class,
            NotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(Exception e, HttpServletRequest request) {
        return ResponseFactory.createErrorResponse(e.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(ValidationException e, HttpServletRequest request) {
        Class<?> targetClass = e.getInvalidClass();
        Map<String, List<String>> errors = e.getErrors()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        entry -> resolveJsonName(entry.getKey(), targetClass),
                        Map.Entry::getValue
                ));
        return ResponseFactory.createErrorResponse(VALIDATION_ERROR_MESSAGE, request.getRequestURI(), errors);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e, HttpServletRequest request) {

        Class<?> targetClass = Optional.ofNullable(e.getBindingResult().getTarget())
                .map(Object::getClass)
                .orElse(null);
        Map<String, List<String>> errors = e.getBindingResult().getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(
                        fieldError -> resolveJsonName(fieldError.getField(), targetClass),
                        Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())
                ));

        return ResponseFactory.createErrorResponse(VALIDATION_ERROR_MESSAGE, request.getRequestURI(), errors);
    }

    private String resolveJsonName(String fieldName, Class<?> clazz) {
        if (clazz == null) return fieldName;
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            JsonProperty annotation = field.getAnnotation(JsonProperty.class);
            return (annotation != null) ? annotation.value() : fieldName;
        } catch (NoSuchFieldException e) {
            return fieldName;
        }
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiError handleAuthenticationException(AuthenticationException e, HttpServletRequest request) {
        return ResponseFactory.createErrorResponse(e.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleAuthorizationDeniedException(AuthorizationDeniedException e, HttpServletRequest request) {
        return ResponseFactory.createErrorResponse(e.getMessage(), request.getRequestURI());
    }
}
