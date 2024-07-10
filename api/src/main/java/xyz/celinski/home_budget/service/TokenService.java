package xyz.celinski.home_budget.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;
import xyz.celinski.home_budget.dto.TokenDTO;

@Service
public class TokenService {
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
}
