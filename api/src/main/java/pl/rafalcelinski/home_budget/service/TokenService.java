package pl.rafalcelinski.home_budget.service;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.rafalcelinski.home_budget.dto.TokenDTO;
import pl.rafalcelinski.home_budget.exception.InvalidTokenException;

@Service
public class TokenService {
    private final String secretKey;

    public TokenService(@Value("${jwt.secretKey}") String secretKey) {
        this.secretKey = secretKey;
    }

    public TokenDTO generateToken(Long userId) {
        String token = Jwts.builder()
                .setSubject(userId.toString())
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return new TokenDTO(token);
    }

    public Long extractUserId(TokenDTO tokenDTO) {
        String token = tokenDTO.getToken();
        return Long.parseLong(Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject());
    }

    public void validateToken(TokenDTO tokenDTO) {
        try {
            String token = tokenDTO.getToken();
            Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new InvalidTokenException("Token expired");
        } catch (SignatureException e) {
            throw new InvalidTokenException("Invalid token signature");
        } catch (MalformedJwtException e) {
            throw new InvalidTokenException("Invalid token format");
        } catch (Exception e) {
            throw new InvalidTokenException("Invalid token");
        }
    }
}
