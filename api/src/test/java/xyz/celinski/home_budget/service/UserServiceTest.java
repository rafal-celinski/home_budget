package xyz.celinski.home_budget.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import xyz.celinski.home_budget.dto.RegisterDTO;
import xyz.celinski.home_budget.dto.UserDTO;
import xyz.celinski.home_budget.exception.InvalidUserDetailsException;
import xyz.celinski.home_budget.exception.UserAlreadyExistsException;
import xyz.celinski.home_budget.entity.User;
import xyz.celinski.home_budget.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;


    @Test
    public void registerNewUser_shouldThrowUserAlreadyExistsException_whenEmailAlreadyExists() {
        User user = new User("test@email.com", "passwordHash");
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        RegisterDTO registerDTO = new RegisterDTO("test@email.com", "passwordHash");

        assertThatThrownBy(() -> userService.registerNewUser(registerDTO))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessage("User with this email already exists");
    }

    @Test
    public void registerNewUser_shouldThrowInvalidUserDetailsException_whenRepositoryThrowsDataAccessException() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenThrow(new DataAccessException("") {});

        RegisterDTO registerDTO = new RegisterDTO("test@email.com", "passwordHash");

        assertThatThrownBy(() -> userService.registerNewUser(registerDTO))
                .isInstanceOf(InvalidUserDetailsException.class)
                .hasMessage("Email and password cannot be empty");
    }

    @Test
    public void registerNewUser_shouldSaveUser_whenEmailDoesNotExist() {
        User user = new User("test@email.com", "passwordHash");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        RegisterDTO registerDTO = new RegisterDTO("test@email.com", "passwordHash");

        UserDTO userDTO = userService.registerNewUser(registerDTO);

        assertThat(userDTO).isNotNull();
        assertThat(userDTO.getEmail()).isEqualTo(user.getEmail());
    }
}
