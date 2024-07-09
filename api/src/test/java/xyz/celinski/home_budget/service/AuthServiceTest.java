package xyz.celinski.home_budget.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import xyz.celinski.home_budget.exception.InvalidCredentialsException;
import xyz.celinski.home_budget.exception.UserNotFoundException;
import xyz.celinski.home_budget.model.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    UserService userService;

    @Mock
    TokenService tokenService;

    @InjectMocks
    AuthService authService;

    @Test
    public void login_shouldReturnToken_whenCredentialsAreValid() {
        User user = new User("test@email.com", "passwordHash");
        user.setId(1L);
        when(userService.getUserByEmail(anyString()))
                .thenReturn(Optional.of(user));
        when(tokenService.generateToken(anyLong()))
                .thenReturn("dummy-token");

        String token = authService.login("test@email.com", "passwordHash");
        assertThat(token).isNotNull();
        assertThat(token).isEqualTo("dummy-token");
    }

    @Test
    public void login_shouldThrowUserNotFoundException_whenUserWithGivenEmailDoesntExist() {
        when(userService.getUserByEmail(anyString()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login("test@email.com", "passwordHash"))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User with this email does not exist");
    }

    @Test
    public void login_shouldThrowInvalidCredentialsException_whenPasswordIsIncorrect() {
        when(userService.getUserByEmail(anyString()))
                .thenReturn(Optional.of(new User("test@email.com", "passwordHash")));

        assertThatThrownBy(() -> authService.login("test@email.com", "wrongHash"))
                .isInstanceOf(InvalidCredentialsException.class)
                .hasMessage("Invalid password");
    }

}
