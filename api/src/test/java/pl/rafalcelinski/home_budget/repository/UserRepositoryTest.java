package pl.rafalcelinski.home_budget.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import pl.rafalcelinski.home_budget.entity.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private String testEmail;
    private User testUser;

    @BeforeEach
    void createUser() {
        testEmail = "test@mail.com";
        testUser = new User(testEmail, "passwordHash");
    }

    @Test
    public void save_shouldGenerateId_whenSaved() {
        User savedUser = userRepository.save(testUser);

        assertThat(savedUser.getId()).isNotNull();
    }

    @Test
    public void save_shouldThrowException_whenEmailIsNotUnique() {
        userRepository.save(testUser);

        User anotherUserWithSameEmail = new User(testUser.getEmail(), "passwordHash");

        assertThatThrownBy(() -> {
            userRepository.save(anotherUserWithSameEmail);
            entityManager.flush();
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void save_shouldThrowException_whenEmailIsNull() {
        User userWithNullEmail = new User(null ,"passwordHash");

        assertThatThrownBy(() -> {
            userRepository.save(userWithNullEmail);
            entityManager.flush();
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void save_shouldThrowException_whenPasswordHashIsNull() {
        User userWithNullPasswordHash = new User(testEmail, null);

        assertThatThrownBy(() -> {
            userRepository.save(userWithNullPasswordHash);
            entityManager.flush();
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void findByEmail_shouldReturnUser_whenEmailExists() {
        User savedUser = userRepository.save(testUser);

        Optional<User> foundUser = userRepository.findByEmail(testEmail);

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo(testEmail);
    }

    @Test
    public void findByEmail_shouldReturnEmpty_whenEmailDoesNotExist() {
        Optional<User> foundUser = userRepository.findByEmail(testEmail);

        assertThat(foundUser).isEmpty();
    }
}
