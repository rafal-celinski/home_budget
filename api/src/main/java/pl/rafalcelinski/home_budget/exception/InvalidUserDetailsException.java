package pl.rafalcelinski.home_budget.exception;

public class InvalidUserDetailsException extends RuntimeException {
    public InvalidUserDetailsException() {
        super();
    }
    public InvalidUserDetailsException(String message) {
        super(message);
    }
}
