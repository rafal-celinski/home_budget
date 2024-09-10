package xyz.celinski.home_budget.exception;

public class InvalidDateRangeException extends RuntimeException {
    public InvalidDateRangeException() {
        super();
    }
    public InvalidDateRangeException(String message) {
        super(message);
    }
}
