package xyz.celinski.home_budget.exception;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException() {
        super();
    }
    public AccessDeniedException(String message) {
        super(message);
    }
}

