package pl.rafalcelinski.home_budget.controller;

import jakarta.validation.groups.Default;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.rafalcelinski.home_budget.dto.ExpenseDTO;
import pl.rafalcelinski.home_budget.dto.ExpenseStatisticsDTO;
import pl.rafalcelinski.home_budget.dto.validation.groups.OnCreate;
import pl.rafalcelinski.home_budget.dto.validation.groups.OnUpdate;
import pl.rafalcelinski.home_budget.exception.InvalidDateRangeException;
import pl.rafalcelinski.home_budget.service.AuthorizationService;
import pl.rafalcelinski.home_budget.service.ExpenseService;

import java.time.LocalDate;
import java.util.Optional;


@RestController
@RequestMapping("/expenses")
public class ExpenseController {
    private final ExpenseService expenseService;
    private final AuthorizationService authorizationService;

    ExpenseController(ExpenseService expenseService, AuthorizationService authorizationService) {
        this.expenseService = expenseService;
        this.authorizationService = authorizationService;
    }

    private void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new InvalidDateRangeException("Start date cannot be after end date");
        }
    }

    @GetMapping
    public ResponseEntity<Page<ExpenseDTO>> getExpenses(@RequestParam(value = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                         @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> endDate,
                                         @RequestParam(value = "categoryId", required = false) Optional<Long> categoryId,
                                         @RequestHeader("Authorization") String authorizationHeader,
                                         Pageable pageable) {

        Long userId = authorizationService.validAuthorizationAndExtractUserID(authorizationHeader);

        LocalDate finalEndDate = endDate.orElse(LocalDate.now());
        validateDateRange(startDate, finalEndDate);

        Page<ExpenseDTO> expenses = expenseService.getExpensesByDateAndCategoryId(startDate, finalEndDate, categoryId, userId, pageable);

        return ResponseEntity.ok(expenses);
    }

    @PostMapping
    public ResponseEntity<ExpenseDTO> addExpense(@RequestBody @Validated({OnCreate.class, Default.class}) ExpenseDTO expenseDTO,
                                        @RequestHeader("Authorization") String authorizationHeader) {

        Long userId = authorizationService.validAuthorizationAndExtractUserID(authorizationHeader);
        ExpenseDTO newExpenseDTO = expenseService.addExpense(expenseDTO, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(newExpenseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id,
                                           @RequestHeader("Authorization") String authorizationHeader) {

        Long userId = authorizationService.validAuthorizationAndExtractUserID(authorizationHeader);
        expenseService.deleteExpenseById(id, userId);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseDTO> updateExpense(@PathVariable Long id,
                                           @RequestBody @Validated({OnUpdate.class, Default.class}) ExpenseDTO expenseDTO,
                                           @RequestHeader("Authorization") String authorizationHeader) {

        Long userId = authorizationService.validAuthorizationAndExtractUserID(authorizationHeader);
        ExpenseDTO newExpenseDTO = expenseService.updateExpenseById(id, expenseDTO, userId);

        return ResponseEntity.ok(newExpenseDTO);
    }

    @GetMapping("/stats")
    public ResponseEntity<ExpenseStatisticsDTO> getExpenseStats(@RequestParam(value = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                             @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> endDate,
                                             @RequestParam(value = "categoryId", required = false) Optional<Long> categoryId,
                                             @RequestHeader("Authorization") String authorizationHeader) {

        Long userId = authorizationService.validAuthorizationAndExtractUserID(authorizationHeader);

        LocalDate finalEndDate = endDate.orElse(LocalDate.now());

        validateDateRange(startDate, finalEndDate);
        ExpenseStatisticsDTO expenseStatisticsDTO = expenseService.getExpenseStatisticsByDateAndCategory(startDate, finalEndDate, categoryId, userId);

        return ResponseEntity.ok(expenseStatisticsDTO);
    }
}
