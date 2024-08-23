package xyz.celinski.home_budget.controller;

import jakarta.validation.groups.Default;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.celinski.home_budget.dto.ExpenseDTO;
import xyz.celinski.home_budget.dto.TokenDTO;
import xyz.celinski.home_budget.dto.validation.groups.OnCreate;
import xyz.celinski.home_budget.dto.validation.groups.OnUpdate;
import xyz.celinski.home_budget.exception.InvalidDateRangeException;
import xyz.celinski.home_budget.exception.InvalidTokenException;
import xyz.celinski.home_budget.service.ExpenseService;
import xyz.celinski.home_budget.service.TokenService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;


@RestController
@RequestMapping("/expenses")
public class ExpenseController {
    private final ExpenseService expenseService;
    private final TokenService tokenService;

    ExpenseController(ExpenseService expenseService, TokenService tokenService) {
        this.expenseService = expenseService;
        this.tokenService = tokenService;
    }

    @GetMapping
    public ResponseEntity<?> getExpenses(@RequestParam(value = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String startDate,
                                         @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String endDate,
                                         @RequestHeader("Authorization") String authorizationHeader,
                                         Pageable pageable) {

        TokenDTO tokenDTO = tokenService.authorizationHeaderToTokenDTO(authorizationHeader);
        if (!tokenService.isTokenValid(tokenDTO)) {
            throw new InvalidTokenException("Invalid token");
        }

        LocalDate start, end;
        try {
            start = LocalDate.parse(startDate);
            end = endDate != null ? LocalDate.parse(endDate) : LocalDate.now();
        }
        catch (DateTimeParseException e) {
            throw new InvalidDateRangeException("Date doesnt exist");
        }

        if (start.isAfter(end)) {
            throw new InvalidDateRangeException("Start date cannot be after end date");
        }

        Long userId = tokenService.extractUserId(tokenDTO);
        Page<ExpenseDTO> expenses = expenseService.getExpensesByDate(start, end, userId, pageable);
        return ResponseEntity.ok(expenses);
    }


    @PostMapping
    public ResponseEntity<?> addExpense(@RequestBody @Validated({OnCreate.class, Default.class}) ExpenseDTO expenseDTO,
                                        @RequestHeader("Authorization") String authorizationHeader) {

        TokenDTO tokenDTO = tokenService.authorizationHeaderToTokenDTO(authorizationHeader);
        if (!tokenService.isTokenValid(tokenDTO)) {
            throw new InvalidTokenException("Invalid token");
        }

        Long userId = tokenService.extractUserId(tokenDTO);
        ExpenseDTO newExpenseDTO = expenseService.addExpense(expenseDTO, userId);
        return ResponseEntity.ok(newExpenseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExpense(@PathVariable Long id,
                                           @RequestHeader("Authorization") String authorizationHeader) {

        TokenDTO tokenDTO = tokenService.authorizationHeaderToTokenDTO(authorizationHeader);
        if (!tokenService.isTokenValid(tokenDTO)) {
            throw new InvalidTokenException("Invalid token");
        }

        Long userId = tokenService.extractUserId(tokenDTO);

        expenseService.deleteExpenseById(id, userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateExpense(@PathVariable Long id,
                                           @RequestBody @Validated({OnUpdate.class, Default.class}) ExpenseDTO expenseDTO,
                                           @RequestHeader("Authorization") String authorizationHeader) {

        TokenDTO tokenDTO = tokenService.authorizationHeaderToTokenDTO(authorizationHeader);
        if (!tokenService.isTokenValid(tokenDTO)) {
            throw new InvalidTokenException("Invalid token");
        }

        Long userId = tokenService.extractUserId(tokenDTO);
        ExpenseDTO newExpenseDTO = expenseService.updateExpenseById(id, expenseDTO, userId);
        return ResponseEntity.ok(newExpenseDTO);
    }
}
