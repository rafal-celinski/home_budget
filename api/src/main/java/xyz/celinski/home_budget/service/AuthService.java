package xyz.celinski.home_budget.service;

import org.springframework.stereotype.Service;
import xyz.celinski.home_budget.dto.LoginDTO;
import xyz.celinski.home_budget.dto.TokenDTO;
import xyz.celinski.home_budget.exception.InvalidCredentialsException;
import xyz.celinski.home_budget.exception.UserNotFoundException;
import xyz.celinski.home_budget.entity.User;
import xyz.celinski.home_budget.repository.UserRepository;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final TokenService tokenService;

    AuthService(UserRepository userRepository, TokenService tokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    public TokenDTO login(LoginDTO loginDTO) {
        User user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User with this email does not exist"));

        if (!user.getPasswordHash().equals(loginDTO.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid password");
        }

        return tokenService.generateToken(user.getId());
    }
}
