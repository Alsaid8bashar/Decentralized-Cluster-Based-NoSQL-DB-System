package org.example.Authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class TokenService {
    public TokenService() {
    }
    public  Long getUserId(String token) {
        Claims claims = Jwts.parser().setSigningKey("secret-key").parseClaimsJws(token).getBody();
        return Long.parseLong(claims.getSubject());
    }
}
