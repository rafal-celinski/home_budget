package xyz.celinski.home_budget.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    private final String SECRET_KEY = "secret"; // TODO: make this more secure :>

    public String generateToken(Long userId) {
        return Jwts.builder()
                .setSubject(userId.toString())
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public Long extractUserId(String token) {
        return Long.parseLong(Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject());
    }
}
