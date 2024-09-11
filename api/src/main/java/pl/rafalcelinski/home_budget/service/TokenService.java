package pl.rafalcelinski.home_budget.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;
import pl.rafalcelinski.home_budget.dto.TokenDTO;
import pl.rafalcelinski.home_budget.repository.UserRepository;

@Service
public class TokenService {

    private final UserRepository userRepository;

    TokenService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private final String SECRET_KEY = "secret"; // TODO: make this more secure :>

    public TokenDTO generateToken(Long userId) {
        String token = Jwts.builder()
                .setSubject(userId.toString())
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

        return new TokenDTO(token);
    }

    public Long extractUserId(TokenDTO tokenDTO) {
        return Long.parseLong(Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(tokenDTO.getToken())
                .getBody()
                .getSubject());
    }

    public boolean isTokenValid(TokenDTO tokenDTO) {
        try {
            Long userId = extractUserId(tokenDTO);
            return userRepository.existsById(userId);
        }
        catch (Exception e) {
            return false;
        }
    }

    public TokenDTO authorizationHeaderToTokenDTO(String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        return new TokenDTO(token);
    }
}
