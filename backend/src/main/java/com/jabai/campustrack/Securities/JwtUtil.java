package com.jabai.campustrack.Securities;

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


    //This generates a web token so that every request made must go through a validation before allowing the user to do certain actions
    
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


  //  This extracts the user's role so that later on we'll be able to use this extraction when evaluating their role for certain authority

    public String extractRole(String token){ 
        SecretKey signingKey = Keys.hmacShaKeyFor(JWT_SECRET.getBytes()); 
        return  Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class); 
    }


    //This extracts the Username/ Email / Subject / Unique Identifier of the user so that we know who are we currently working with 
    
    public String extractSubject(String token){ 
        SecretKey signingKey = Keys.hmacShaKeyFor(JWT_SECRET.getBytes()); 
        return  Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject(); 
    } 

    //This checks if the JWT of a user request is valid 

    public boolean isTokenValid(String token){ 
        SecretKey signingKey = Keys.hmacShaKeyFor(JWT_SECRET.getBytes()); 
        try{    
              Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(token);
                    return true; 

        }catch(Exception e){ 

            //Basin pwede mo maka make og exception if ever dili ko maka make exception ani 
        
            return  false;
        }
    }

}
