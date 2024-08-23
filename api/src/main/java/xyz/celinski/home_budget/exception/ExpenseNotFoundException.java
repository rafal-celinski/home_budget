package xyz.celinski.home_budget.exception;

public class ExpenseNotFoundException extends RuntimeException {
    public ExpenseNotFoundException() {
        super();
    }
    public ExpenseNotFoundException(String message) {
        super(message);
    }
}
