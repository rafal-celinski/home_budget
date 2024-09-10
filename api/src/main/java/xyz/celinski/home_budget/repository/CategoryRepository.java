package xyz.celinski.home_budget.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.celinski.home_budget.entity.Category;
import xyz.celinski.home_budget.entity.User;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByUser(User user);
}
