package pl.rafalcelinski.home_budget.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.rafalcelinski.home_budget.entity.Expense;
import pl.rafalcelinski.home_budget.entity.User;

import java.time.LocalDate;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    Page<Expense> findByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate, Pageable pageable);
}