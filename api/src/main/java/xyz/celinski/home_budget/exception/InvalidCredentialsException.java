package xyz.celinski.home_budget.exception;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        super();
    }
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
