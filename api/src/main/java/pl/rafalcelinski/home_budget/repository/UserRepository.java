package pl.rafalcelinski.home_budget.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.rafalcelinski.home_budget.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByEmail(String email);
}
