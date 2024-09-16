package pl.rafalcelinski.home_budget.mapper;

import org.springframework.stereotype.Component;
import pl.rafalcelinski.home_budget.dto.ExpenseDTO;
import pl.rafalcelinski.home_budget.entity.Category;
import pl.rafalcelinski.home_budget.entity.Expense;
import pl.rafalcelinski.home_budget.entity.User;
import pl.rafalcelinski.home_budget.exception.EntityDTOMappingException;
import pl.rafalcelinski.home_budget.repository.CategoryRepository;
import pl.rafalcelinski.home_budget.service.CategoryService;

import java.time.LocalDate;

@Component
public class ExpenseMapper {
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;

    public ExpenseMapper(CategoryService categoryService, CategoryRepository categoryRepository) {
        this.categoryService = categoryService;
        this.categoryRepository = categoryRepository;
    }

    public ExpenseDTO toDTO(Expense expense) {
        if (expense == null) {
            return null;
        }

        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setId(expense.getId());
        expenseDTO.setAmount(expense.getAmount());
        expenseDTO.setDescription(expense.getDescription());
        expenseDTO.setDate(expense.getDate().toString());
        expenseDTO.setCategoryId(expense.getCategory().getId());

        return expenseDTO;
    }

    public Expense toEntity(ExpenseDTO expenseDTO, User user) {
        if (expenseDTO == null) {
            return null;
        }

        Expense expense = new Expense();
        expense.setId(expenseDTO.getId());
        expense.setAmount(expenseDTO.getAmount());
        expense.setDescription(expenseDTO.getDescription());
        expense.setDate(LocalDate.parse(expenseDTO.getDate()));
        expense.setUser(user);

        Category category = categoryRepository.findById(expenseDTO.getCategoryId()).orElseThrow(() -> new EntityDTOMappingException("Cannot find category"));
        expense.setCategory(category);

        return expense;
    }
}
