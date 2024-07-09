package xyz.celinski.home_budget.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import xyz.celinski.home_budget.model.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    public void save_shouldThrowDataIntegrityViolationException_whenEmailInUserIsNull() {
        User user = new User();
        user.setPasswordHash("passwordHash");

        assertThatThrownBy(() ->  userRepository.save(user))
                .isInstanceOf(org.springframework.dao.DataIntegrityViolationException.class);
    }

    @Test
    public void save_shouldThrowDataIntegrityViolationException_whenPasswordInUserIsNull() {
        User user = new User();
        user.setEmail("test@email.com");

        assertThatThrownBy(() ->  userRepository.save(user))
                .isInstanceOf(org.springframework.dao.DataIntegrityViolationException.class);
    }

    @Test
    public void findByEmail_shouldReturnUser_whenUserWithGivenEmailExists() {
        User user = new User("test@email.com","passwordHash");
        userRepository.save(user);

        Optional<User> found = userRepository.findByEmail("test@email.com");

        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("test@email.com");
    }

    @Test
    public void findByEmail_shouldReturnUser_whenUserWithGivenEmailDoesntExist() {
        Optional<User> found = userRepository.findByEmail("test@email.com");

        assertThat(found).isNotPresent();
    }
}
