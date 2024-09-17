package pl.rafalcelinski.home_budget.mapper;

import org.springframework.stereotype.Component;
import pl.rafalcelinski.home_budget.dto.ExpenseStatisticsDTO;
import pl.rafalcelinski.home_budget.query.ExpenseStatistics;

@Component
public class ExpenseStatisticsMapper {
    public ExpenseStatisticsDTO toDTO(ExpenseStatistics expenseStatistics) {
        if (expenseStatistics == null) {
            return null;
        }

        ExpenseStatisticsDTO expenseStatisticsDTO = new ExpenseStatisticsDTO();
        expenseStatisticsDTO.setTotalExpense(expenseStatistics.getTotalExpense());
        expenseStatisticsDTO.setMinimumExpense(expenseStatistics.getMinimumExpense());
        expenseStatisticsDTO.setMaximumExpense(expenseStatistics.getMaximumExpense());
        expenseStatisticsDTO.setAverageExpense(expenseStatistics.getAverageExpense());
        expenseStatisticsDTO.setCategoryPercentage(expenseStatistics.getCategoryPercentage());

        return expenseStatisticsDTO;
    }
}
