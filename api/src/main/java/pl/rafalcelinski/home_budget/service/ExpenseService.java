package pl.rafalcelinski.home_budget.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.rafalcelinski.home_budget.dto.ExpenseDTO;
import pl.rafalcelinski.home_budget.exception.AccessDeniedException;
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
    private final UserRepository userRepository;
    private final ExpenseMapper expenseMapper;

    public ExpenseService(ExpenseRepository expenseRepository, UserRepository userRepository, ExpenseMapper expenseMapper) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.expenseMapper = expenseMapper;
    }

    public Page<ExpenseDTO> getExpensesByDate(LocalDate startDate, LocalDate endDate, Long userId, Pageable pageable) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));

        return expenseRepository.findByUserAndDateBetween(user, startDate, endDate, pageable)
                .map(expenseMapper::toDTO);
    }

    public ExpenseDTO addExpense(ExpenseDTO expenseDTO, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));

        Expense expense = expenseMapper.toEntity(expenseDTO, user);
        expense = expenseRepository.save(expense);
        return expenseMapper.toDTO(expense);
    }

    public void deleteExpenseById(Long expenseId, Long userId) {
        Expense expense = expenseRepository.findById(expenseId).orElseThrow(() -> new ExpenseNotFoundException("Expense not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!expense.getUser().equals(user)) throw new AccessDeniedException("User does not have permission to delete this expense");

        expenseRepository.delete(expense);
    }

    public ExpenseDTO updateExpenseById(Long expenseId, ExpenseDTO expenseDTO, Long userId) {
        Expense expense = expenseRepository.findById(expenseId).orElseThrow(() -> new ExpenseNotFoundException("Expense not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!expense.getUser().equals(user)) throw new AccessDeniedException("User does not have permission to delete this expense");

        expenseDTO.setId(expenseId);

        Expense newExpense = expenseMapper.toEntity(expenseDTO, user);
        newExpense = expenseRepository.save(newExpense);
        return expenseMapper.toDTO(newExpense);
    }
}
