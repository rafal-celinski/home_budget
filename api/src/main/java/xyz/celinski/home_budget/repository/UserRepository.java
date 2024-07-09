package xyz.celinski.home_budget.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.celinski.home_budget.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByEmail(String email);
}
