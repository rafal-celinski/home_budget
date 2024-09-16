package pl.rafalcelinski.home_budget.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.rafalcelinski.home_budget.dto.ExpenseDTO;
import pl.rafalcelinski.home_budget.entity.Category;
import pl.rafalcelinski.home_budget.exception.AccessDeniedException;
import pl.rafalcelinski.home_budget.exception.CategoryNotFoundException;
import pl.rafalcelinski.home_budget.exception.ExpenseNotFoundException;
import pl.rafalcelinski.home_budget.exception.UserNotFoundException;
import pl.rafalcelinski.home_budget.entity.Expense;
import pl.rafalcelinski.home_budget.entity.User;
import pl.rafalcelinski.home_budget.mapper.ExpenseMapper;
import pl.rafalcelinski.home_budget.repository.ExpenseRepository;
import pl.rafalcelinski.home_budget.repository.UserRepository;

import java.time.LocalDate;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final UserService userService;
    private final ExpenseMapper expenseMapper;
    private final CategoryService categoryService;

    public ExpenseService(ExpenseRepository expenseRepository, UserService userService, ExpenseMapper expenseMapper, CategoryService categoryService) {
        this.expenseRepository = expenseRepository;
        this.userService = userService;
        this.expenseMapper = expenseMapper;
        this.categoryService = categoryService;
    }

    public Page<ExpenseDTO> getExpensesByDate(LocalDate startDate, LocalDate endDate, Long userId, Pageable pageable) {
        User user = userService.getUserById(userId);

        return expenseRepository.findByUserAndDateBetween(user, startDate, endDate, pageable)
                .map(expenseMapper::toDTO);
    }

    public Page<ExpenseDTO> getExpensesByDateAndCategoryId(LocalDate startDate, LocalDate endDate, Long categoryId, Long userId, Pageable pageable) {
        User user = userService.getUserById(userId);
        Category category = categoryService.getCategoryByIdAndUser(categoryId, userId);

        return expenseRepository.findByUserAndDateBetweenAndCategory(user, startDate, endDate, category, pageable)
                .map(expenseMapper::toDTO);

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
