package xyz.celinski.home_budget.controller;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.groups.Default;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.celinski.home_budget.dto.ExpenseDTO;
import xyz.celinski.home_budget.dto.HttpErrorDTO;
import xyz.celinski.home_budget.dto.TokenDTO;
import xyz.celinski.home_budget.dto.validation.groups.OnCreate;
import xyz.celinski.home_budget.dto.validation.groups.OnUpdate;
import xyz.celinski.home_budget.exception.AccessDeniedException;
import xyz.celinski.home_budget.exception.ExpenseNotFoundException;
import xyz.celinski.home_budget.service.ExpenseService;
import xyz.celinski.home_budget.service.TokenService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Date;


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
    public ResponseEntity<?> getExpenses(@RequestParam(value = "startDate") @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}") String startDate,
                                         @RequestParam(value = "endDate", required = false) @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}") String endDate,
                                         @RequestHeader("Authorization") String authorizationHeader,
                                         Pageable pageable) {

        TokenDTO tokenDTO = tokenService.authorizationHeaderToTokenDTO(authorizationHeader);
        if (!tokenService.isTokenValid(tokenDTO)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new HttpErrorDTO(
                    new Date(), HttpStatus.UNAUTHORIZED.value(), "Invalid token", "/expenses")
            );
        }

        LocalDate start;
        LocalDate end;
        try {
            start = LocalDate.parse(startDate);
            end = endDate != null ? LocalDate.parse(endDate) : LocalDate.now();

            if (start.isAfter(end)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new HttpErrorDTO(
                        new Date(), HttpStatus.BAD_REQUEST.value(), "Start date cannot be after end date", "/expenses")
                );
            }

        } catch (DateTimeParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new HttpErrorDTO(
                    new Date(), HttpStatus.BAD_REQUEST.value(), "Invalid date format", "/expenses")
            );
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
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new HttpErrorDTO(
                    new Date(), HttpStatus.UNAUTHORIZED.value(), "Invalid token", "/expenses")
            );
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
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new HttpErrorDTO(
                    new Date(), HttpStatus.UNAUTHORIZED.value(), "Invalid token", "/expenses")
            );
        }

        Long userId = tokenService.extractUserId(tokenDTO);

        try {
            expenseService.deleteExpenseById(id, userId);
            return ResponseEntity.noContent().build();
        }
        catch (ExpenseNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new HttpErrorDTO(
                    new Date(), HttpStatus.NOT_FOUND.value(), e.getMessage(), "/expenses")
            );
        }
        catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new HttpErrorDTO(
                    new Date(), HttpStatus.FORBIDDEN.value(), e.getMessage(), "/expenses")
            );
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateExpense(@PathVariable Long id,
                                           @RequestBody @Validated({OnUpdate.class, Default.class}) ExpenseDTO expenseDTO,
                                           @RequestHeader("Authorization") String authorizationHeader) {

        TokenDTO tokenDTO = tokenService.authorizationHeaderToTokenDTO(authorizationHeader);
        if (!tokenService.isTokenValid(tokenDTO)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new HttpErrorDTO(
                    new Date(), HttpStatus.UNAUTHORIZED.value(), "Invalid token", "/expenses")
            );
        }

        Long userId = tokenService.extractUserId(tokenDTO);

        try {
            ExpenseDTO newExpenseDTO = expenseService.updateExpenseById(id, expenseDTO, userId);
            return ResponseEntity.ok(newExpenseDTO);
        }
        catch (ExpenseNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new HttpErrorDTO(
                    new Date(), HttpStatus.NOT_FOUND.value(), e.getMessage(), "/expenses")
            );
        }
        catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new HttpErrorDTO(
                    new Date(), HttpStatus.FORBIDDEN.value(), e.getMessage(), "/expenses")
            );
        }
    }
}
