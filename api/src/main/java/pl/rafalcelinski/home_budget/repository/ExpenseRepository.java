package pl.rafalcelinski.home_budget.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.rafalcelinski.home_budget.entity.Category;
import pl.rafalcelinski.home_budget.entity.Expense;
import pl.rafalcelinski.home_budget.entity.User;
import pl.rafalcelinski.home_budget.query.ExpenseStatistics;

import java.time.LocalDate;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    Page<Expense> findByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate, Pageable pageable);
    Page<Expense> findByUserAndDateBetweenAndCategory(User user, LocalDate startDate, LocalDate endDate, Category category, Pageable pageable);

    @Query("SELECT new pl.rafalcelinski.home_budget.query.ExpenseStatistics(" +
                "COALESCE(SUM(e.amount),0), " +
                "COALESCE(MIN(e.amount),0), " +
                "COALESCE(MAX(e.amount),0), " +
                "COALESCE(AVG(e.amount),0), " +
                "NULL)" +
            "FROM Expense e " +
            "WHERE e.date BETWEEN :startDate AND :endDate " +
            "AND e.user = :user " +
            "AND (:category IS NULL OR e.category = :category)")
    Optional<ExpenseStatistics> findExpenseStatisticsByUserAndDateBetweenAndCategory(@Param("user") User user, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("category") Category category);
}
