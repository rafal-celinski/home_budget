package xyz.celinski.home_budget.exception;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException() {
        super();
    }
    public InvalidTokenException(String message) {
        super(message);
    }
}

