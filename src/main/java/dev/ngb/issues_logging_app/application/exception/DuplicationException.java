package dev.ngb.issues_logging_app.application.exception;

public class DuplicationException extends RuntimeException {
    public DuplicationException(String message) {
        super(message);
    }
}
