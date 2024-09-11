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
import pl.rafalcelinski.home_budget.repository.ExpenseRepository;
import pl.rafalcelinski.home_budget.repository.UserRepository;

import java.time.LocalDate;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    public ExpenseService(ExpenseRepository expenseRepository, UserRepository userRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
    }

    public Page<ExpenseDTO> getExpensesByDate(LocalDate startDate, LocalDate endDate, Long userId, Pageable pageable) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));

        return expenseRepository.findByUserAndDateBetween(user, startDate, endDate, pageable)
                .map(this::convertToDTO);
    }

    public ExpenseDTO addExpense(ExpenseDTO expenseDTO, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));

        Expense expense = convertToEntity(expenseDTO, user);
        expense = expenseRepository.save(expense);
        return convertToDTO(expense);
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

        Expense newExpense = convertToEntity(expenseDTO, user);
        newExpense = expenseRepository.save(newExpense);
        return convertToDTO(newExpense);
    }

    private Expense convertToEntity(ExpenseDTO expenseDTO, User user) {
        Expense expense = new Expense();
        expense.setId(expenseDTO.getId());
        expense.setAmount(expenseDTO.getAmount());
        expense.setDescription(expenseDTO.getDescription());
        expense.setDate(LocalDate.parse(expenseDTO.getDate()));
        expense.setUser(user);
        return expense;
    }

    private ExpenseDTO convertToDTO(Expense expense) {
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setId(expense.getId());
        expenseDTO.setAmount(expense.getAmount());
        expenseDTO.setDescription(expense.getDescription());
        expenseDTO.setDate(expense.getDate().toString());
        return expenseDTO;
    }
}
