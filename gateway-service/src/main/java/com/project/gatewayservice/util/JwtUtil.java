package com.project.gatewayservice.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;

@Component
public class JwtUtil {


    public static final String SECRET_KEY = "c9f4e8a1b7d2c6f93e5acuny45GUVYU765hjhgh2a6d9b3f1c4e8a7d2c5f9b6e3a1c8d4f7b";


    public void validateToken(final String token) {
        Jwts.parser()
                .verifyWith((SecretKey) generateKey())
                .build()
                .parseClaimsJws(token);
    }


    private Key generateKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

}