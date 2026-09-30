package com.jabai.campustrack.Securities;

import java.security.Key;
import java.util.Date;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import com.jabai.campustrack.Models.*;

@Configuration 
public class JwtUtil {
    @Value("${spring.jwt.secret}")
    private String JWT_SECRET;

    @Value("${spring.jwt.issuer}")
    private String JWT_ISSUER;

    @Value("${spring.jwt.expiration}")
    private long JWT_EXPIRATION;

    public String generateToken(User user) {
        SecretKey signingKey = Keys.hmacShaKeyFor(JWT_SECRET.getBytes());
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("role", user.getUserRole())
                .issuedAt(new Date())
                .issuer(JWT_ISSUER)
                .expiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                .signWith(signingKey)
                .compact();
    } 

    public String extractRole(String token){ 
        SecretKey signingKey = Keys.hmacShaKeyFor(JWT_SECRET.getBytes()); 
        return  Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class); 
    }

}
