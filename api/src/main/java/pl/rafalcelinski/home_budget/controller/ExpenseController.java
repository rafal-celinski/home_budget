package pl.rafalcelinski.home_budget.controller;

import jakarta.validation.groups.Default;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.rafalcelinski.home_budget.dto.ExpenseDTO;
import pl.rafalcelinski.home_budget.dto.TokenDTO;
import pl.rafalcelinski.home_budget.dto.validation.groups.OnCreate;
import pl.rafalcelinski.home_budget.dto.validation.groups.OnUpdate;
import pl.rafalcelinski.home_budget.exception.InvalidDateRangeException;
import pl.rafalcelinski.home_budget.exception.InvalidTokenException;
import pl.rafalcelinski.home_budget.service.ExpenseService;
import pl.rafalcelinski.home_budget.service.TokenService;

import java.time.LocalDate;
import java.util.Optional;


@RestController
@RequestMapping("/expenses")
public class ExpenseController {
    private final ExpenseService expenseService;
    private final TokenService tokenService;

    ExpenseController(ExpenseService expenseService, TokenService tokenService) {
        this.expenseService = expenseService;
        this.tokenService = tokenService;
    }

    private Long validAuthorizationAndExtractUserID(String authorizationHeader) {
        TokenDTO tokenDTO = tokenService.authorizationHeaderToTokenDTO(authorizationHeader);
        if (!tokenService.isTokenValid(tokenDTO)) {
            throw new InvalidTokenException("Invalid token");
        }
        return tokenService.extractUserId(tokenDTO);
    }

    @GetMapping
    public ResponseEntity<?> getExpenses(@RequestParam(value = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                         @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> endDate,
                                         @RequestParam(value = "categoryId", required = false) Optional<Long> categoryId,
                                         @RequestHeader("Authorization") String authorizationHeader,
                                         Pageable pageable) {

        Long userId = validAuthorizationAndExtractUserID(authorizationHeader);

        LocalDate finalEndDate = endDate.orElse(LocalDate.now());
        if (startDate.isAfter(finalEndDate)) {
            throw new InvalidDateRangeException("Start date cannot be after end date");
        }

        Page<ExpenseDTO> expenses;
        if (categoryId.isPresent()) {
            expenses = expenseService.getExpensesByDateAndCategoryId(startDate, finalEndDate, categoryId.get(), userId, pageable);
        }
        else {
            expenses = expenseService.getExpensesByDate(startDate, finalEndDate, userId, pageable);
        }

        return ResponseEntity.ok(expenses);
    }

    @PostMapping
    public ResponseEntity<?> addExpense(@RequestBody @Validated({OnCreate.class, Default.class}) ExpenseDTO expenseDTO,
                                        @RequestHeader("Authorization") String authorizationHeader) {

        Long userId = validAuthorizationAndExtractUserID(authorizationHeader);
        ExpenseDTO newExpenseDTO = expenseService.addExpense(expenseDTO, userId);
        return ResponseEntity.ok(newExpenseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExpense(@PathVariable Long id,
                                           @RequestHeader("Authorization") String authorizationHeader) {

        Long userId = validAuthorizationAndExtractUserID(authorizationHeader);
        expenseService.deleteExpenseById(id, userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateExpense(@PathVariable Long id,
                                           @RequestBody @Validated({OnUpdate.class, Default.class}) ExpenseDTO expenseDTO,
                                           @RequestHeader("Authorization") String authorizationHeader) {

        Long userId = validAuthorizationAndExtractUserID(authorizationHeader);
        ExpenseDTO newExpenseDTO = expenseService.updateExpenseById(id, expenseDTO, userId);
        return ResponseEntity.ok(newExpenseDTO);
    }
}
