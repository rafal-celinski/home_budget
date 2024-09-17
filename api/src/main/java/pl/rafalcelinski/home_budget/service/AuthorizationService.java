package pl.rafalcelinski.home_budget.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;
import pl.rafalcelinski.home_budget.dto.LoginDTO;
import pl.rafalcelinski.home_budget.dto.TokenDTO;
import pl.rafalcelinski.home_budget.entity.User;
import pl.rafalcelinski.home_budget.exception.InvalidCredentialsException;
import pl.rafalcelinski.home_budget.exception.InvalidTokenException;
import pl.rafalcelinski.home_budget.exception.UserNotFoundException;
import pl.rafalcelinski.home_budget.repository.UserRepository;

@Service
public class AuthorizationService {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    AuthorizationService(UserRepository userRepository, TokenService tokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    private final String SECRET_KEY = "secret"; // TODO: make this more secure :>

    public TokenDTO authenticate(LoginDTO loginDTO) {
        User user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User with this email does not exist"));

        if (!user.getPasswordHash().equals(loginDTO.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid password");
        }

        return tokenService.generateToken(user.getId());
    }

    private TokenDTO authorizationHeaderToTokenDTO(String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        return new TokenDTO(token);
    }

    public Long validAuthorizationAndExtractUserID(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new InvalidTokenException("Authorization header is missing or invalid.");
        }

        TokenDTO tokenDTO = authorizationHeaderToTokenDTO(authorizationHeader);
        tokenService.validateToken(tokenDTO);

        Long userId = tokenService.extractUserId(tokenDTO);

        if (!userRepository.existsById(userId)) {
            throw new InvalidTokenException("User does not exist");
        }

        return userId;
    }
}
