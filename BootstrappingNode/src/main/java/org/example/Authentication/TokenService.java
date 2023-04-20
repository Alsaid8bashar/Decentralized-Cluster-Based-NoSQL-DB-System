package org.example.Authentication;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenService {
    public static String tokenGenerator(String username, String password,long id) {
        JwtBuilder builder = Jwts.builder()
                .setSubject(String.valueOf(id))
                .claim("username",username)
                .claim("password", password)
                .signWith(SignatureAlgorithm.HS256, "secret-key");
        return builder.compact();
    }
}
