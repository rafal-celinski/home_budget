package pl.rafalcelinski.home_budget.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.rafalcelinski.home_budget.entity.Category;
import pl.rafalcelinski.home_budget.entity.User;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByUser(User user);
}
