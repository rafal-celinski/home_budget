package xyz.celinski.home_budget.service;

import org.springframework.stereotype.Service;
import xyz.celinski.home_budget.exception.InvalidCredentialsException;
import xyz.celinski.home_budget.exception.UserNotFoundException;
import xyz.celinski.home_budget.model.User;

@Service
public class AuthService {
    private final UserService userService;
    private final TokenService tokenService;

    AuthService(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    public String login(String email, String passwordHash) {
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with this email does not exist"));

        if (!user.getPasswordHash().equals(passwordHash)) {
            throw new InvalidCredentialsException("Invalid password");
        }

        return tokenService.generateToken(user.getId());
    }
}
