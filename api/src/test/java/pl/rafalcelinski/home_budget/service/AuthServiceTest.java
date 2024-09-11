package pl.rafalcelinski.home_budget.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.rafalcelinski.home_budget.dto.LoginDTO;
import pl.rafalcelinski.home_budget.dto.TokenDTO;
import pl.rafalcelinski.home_budget.exception.InvalidCredentialsException;
import pl.rafalcelinski.home_budget.exception.UserNotFoundException;
import pl.rafalcelinski.home_budget.entity.User;
import pl.rafalcelinski.home_budget.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    TokenService tokenService;

    @InjectMocks
    AuthService authService;

    @Test
    public void login_shouldReturnTokenDTO_whenCredentialsAreValid() {

        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(new User(1L, "test@email.com", "passwordHash")));
        when(tokenService.generateToken(anyLong())).thenReturn(new TokenDTO("dummy-token"));

        LoginDTO loginDTO = new LoginDTO("test@email.com", "passwordHash");

        TokenDTO token = authService.login(loginDTO);
        assertThat(token).isNotNull();
        assertThat(token.getToken()).isEqualTo("dummy-token");
    }

    @Test
    public void login_shouldThrowUserNotFoundException_whenUserWithGivenEmailDoesntExist() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        LoginDTO loginDTO = new LoginDTO("test@email.com", "passwordHash");

        assertThatThrownBy(() -> authService.login(loginDTO))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User with this email does not exist");
    }

    @Test
    public void login_shouldThrowInvalidCredentialsException_whenPasswordIsIncorrect() {
        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(new User("test@email.com", "passwordHash")));

        LoginDTO loginDTO = new LoginDTO("test@email.com", "incorrectPassword");

        assertThatThrownBy(() -> authService.login(loginDTO))
                .isInstanceOf(InvalidCredentialsException.class)
                .hasMessage("Invalid password");
    }

}
