package com.project.authservice.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class JwtUtil {


    public static final String SECRET_KEY = "c9f4e8a1b7d2c6f93e5acuny45GUVYU765hjhgh2a6d9b3f1c4e8a7d2c5f9b6e3a1c8d4f7b";

    public String generateToken(String username, List<String> roles) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 5))
                .signWith(SignatureAlgorithm.HS256, generateKey())
                .compact();
    }

    private Key generateKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) generateKey()).build()
                .parseClaimsJws(token)
                .getPayload();
    }

    public Date extractExpiration(String token){
        return extractAllClaims(token).getExpiration();
    }
}