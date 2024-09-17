package pl.rafalcelinski.home_budget.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.rafalcelinski.home_budget.dto.ExpenseDTO;
import pl.rafalcelinski.home_budget.dto.ExpenseStatisticsDTO;
import pl.rafalcelinski.home_budget.entity.Category;
import pl.rafalcelinski.home_budget.exception.AccessDeniedException;
import pl.rafalcelinski.home_budget.exception.ExpenseNotFoundException;
import pl.rafalcelinski.home_budget.entity.Expense;
import pl.rafalcelinski.home_budget.entity.User;
import pl.rafalcelinski.home_budget.mapper.ExpenseMapper;
import pl.rafalcelinski.home_budget.mapper.ExpenseStatisticsMapper;
import pl.rafalcelinski.home_budget.query.ExpenseStatistics;
import pl.rafalcelinski.home_budget.repository.ExpenseRepository;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final UserService userService;
    private final ExpenseMapper expenseMapper;
    private final CategoryService categoryService;
    private final ExpenseStatisticsMapper expenseStatisticsMapper;

    public ExpenseService(ExpenseRepository expenseRepository, UserService userService, ExpenseMapper expenseMapper, CategoryService categoryService, ExpenseStatisticsMapper expenseStatisticsMapper) {
        this.expenseRepository = expenseRepository;
        this.userService = userService;
        this.expenseMapper = expenseMapper;
        this.categoryService = categoryService;
        this.expenseStatisticsMapper = expenseStatisticsMapper;
    }

    public Page<ExpenseDTO> getExpensesByDateAndCategoryId(LocalDate startDate, LocalDate endDate, Optional<Long> categoryId, Long userId, Pageable pageable) {
        User user = userService.getUserById(userId);
        Optional<Category> category = categoryId.map(id -> categoryService.getCategoryByIdAndUser(id, userId));

        return expenseRepository.findByUserAndDateBetweenAndCategory(user, startDate, endDate, category.orElse(null), pageable)
                .map(expenseMapper::toDTO);
    }

    public ExpenseStatisticsDTO getExpenseStatisticsByDateAndCategory(LocalDate startDate, LocalDate endDate, Optional<Long> categoryId, Long userId) {
        User user = userService.getUserById(userId);
        Optional<Category> category = categoryId.map(id -> categoryService.getCategoryByIdAndUser(id, userId));

        ExpenseStatistics expenseStatisticsByCategory = expenseRepository.findExpenseStatisticsByUserAndDateBetweenAndCategory(user, startDate, endDate, category.orElse(null))
                .orElse(new ExpenseStatistics(0.0,0.0,0.0,0.0,null));

        ExpenseStatistics allExpenseStatistics = expenseRepository.findExpenseStatisticsByUserAndDateBetweenAndCategory(user, startDate, endDate, null)
                .orElse(new ExpenseStatistics(0.0,0.0,0.0,0.0,null));


        System.out.println(expenseStatisticsByCategory.getTotalExpense());
        System.out.println(allExpenseStatistics.getTotalExpense());

        Double categoryPercentage = !allExpenseStatistics.getTotalExpense().equals(0.0) ? expenseStatisticsByCategory.getTotalExpense() / allExpenseStatistics.getTotalExpense() : 0.0;
        expenseStatisticsByCategory.setCategoryPercentage(categoryPercentage);

        return expenseStatisticsMapper.toDTO(expenseStatisticsByCategory);
    }

    public ExpenseDTO addExpense(ExpenseDTO expenseDTO, Long userId) {
        User user = userService.getUserById(userId);

        Expense expense = expenseMapper.toEntity(expenseDTO, user);
        expense = expenseRepository.save(expense);
        return expenseMapper.toDTO(expense);
    }

    public void deleteExpenseById(Long expenseId, Long userId) {
        Expense expense = getExpenseByIdAndUser(expenseId, userId);
        expenseRepository.delete(expense);
    }

    public ExpenseDTO updateExpenseById(Long expenseId, ExpenseDTO expenseDTO, Long userId) {
        Expense expense = getExpenseByIdAndUser(expenseId, userId);

        expenseDTO.setId(expenseId);

        Expense newExpense = expenseMapper.toEntity(expenseDTO, expense.getUser());
        newExpense = expenseRepository.save(newExpense);
        return expenseMapper.toDTO(newExpense);
    }

    Expense getExpenseByIdAndUser(Long expenseId, Long userId) {
        Expense expense = expenseRepository.findById(expenseId).orElseThrow(() -> new ExpenseNotFoundException("Expense not found"));
        User user = userService.getUserById(userId);

        if (!expense.getUser().equals(user)) throw new AccessDeniedException("This user does not have permission to access this expense");

        return expense;
    }
}
