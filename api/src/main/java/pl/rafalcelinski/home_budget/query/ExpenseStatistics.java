package pl.rafalcelinski.home_budget.query;

public class ExpenseStatistics {
    private Double totalExpense;
    private Double minimumExpense;
    private Double maximumExpense;
    private Double averageExpense;
    private Double categoryPercentage;

    public ExpenseStatistics(Double totalExpense, Double minimumExpense, Double maximumExpense, Double averageExpense, Double categoryPercentage) {
        this.totalExpense = totalExpense;
        this.minimumExpense = minimumExpense;
        this.maximumExpense = maximumExpense;
        this.averageExpense = averageExpense;
        this.categoryPercentage = categoryPercentage;
    }

    public Double getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(Double totalExpense) {
        this.totalExpense = totalExpense;
    }

    public Double getMinimumExpense() {
        return minimumExpense;
    }

    public void setMinimumExpense(Double minimumExpense) {
        this.minimumExpense = minimumExpense;
    }

    public Double getMaximumExpense() {
        return maximumExpense;
    }

    public void setMaximumExpense(Double maximumExpense) {
        this.maximumExpense = maximumExpense;
    }

    public Double getAverageExpense() {
        return averageExpense;
    }

    public void setAverageExpense(Double averageExpense) {
        this.averageExpense = averageExpense;
    }

    public Double getCategoryPercentage() {
        return categoryPercentage;
    }

    public void setCategoryPercentage(Double categoryPercentage) {
        this.categoryPercentage = categoryPercentage;
    }
}
