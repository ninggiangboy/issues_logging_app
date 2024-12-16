package dev.ngb.issues_logging_app.common.factory;

import dev.ngb.issues_logging_app.common.model.ApiError;
import dev.ngb.issues_logging_app.common.model.ApiResult;

import java.util.List;
import java.util.Map;

public class ResponseFactory {

    private static final String SUCCESS_RESPONSE_MESSAGE = "Success";

    public static <T> ApiResult<T> createResultResponse(String message, T data) {
        if (message == null) {
            message = SUCCESS_RESPONSE_MESSAGE;
        }
        return ApiResult.<T>builder()
                .data(data)
                .message(message)
                .build();
    }

    public static <T> ApiResult<T> createResultResponse(T data) {
        return createResultResponse(null, data);
    }

    public static <T> ApiResult<T> createResultResponse(String message) {
        return createResultResponse(message, null);
    }

    public static <T> ApiResult<T> createResultResponse() {
        return createResultResponse(null, null);
    }

    public static ApiError createErrorResponse(String message, String path, Map<String, List<String>> errors) {
        return ApiError.builder()
                .message(message)
                .path(path)
                .errors(errors)
                .build();
    }

    public static ApiError createErrorResponse(String message, String path) {
        return createErrorResponse(message, path, null);
    }
}
