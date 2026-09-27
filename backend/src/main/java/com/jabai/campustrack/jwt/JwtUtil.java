package com.jabai.campustrack.jwt;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.jabai.campustrack.model.*;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Configuration 
public class JwtUtil {
    
    @Value("${jwt.secret}")
    private String key; 

    public String generateToken(User user){ 
    SecretKey signingKey = Keys.hmacShaKeyFor(key.getBytes()); 
    return  Jwts.builder()
    .subject(user.getEmail())
    .claim("role", user.getUserRole())
    .issuedAt(new Date())
    .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 5))
    .signWith(signingKey)
    .compact(); 
    }












}
