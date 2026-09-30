package com.jabai.campustrack.Securities;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.*;

@Component 
public class JwtFilter extends OncePerRequestFilter {
    
   private final JwtUtil jwtUtil; 

    public JwtFilter(JwtUtil jwtUtil){ 
    this.jwtUtil = jwtUtil; 
    }

    @Override  
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)throws ServletException, IOException{ 

        String authHeader = request.getHeader("Authorization"); 
        if(authHeader == null || !authHeader.startsWith("Bearer ")){ 
            filterChain.doFilter(request, response);
            return; 
        }
        String token = authHeader.substring(7); 
        boolean isTokenValid = jwtUtil.isTokenValid(token);

        if(!isTokenValid){ 
            filterChain.doFilter(request, response);
            return ; 
        }

        //This also means {email}
        String username = jwtUtil.extractSubject(token); 
        String role = jwtUtil.extractRole(token); 

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);
        List<GrantedAuthority> authorities = List.of(authority); 

        UsernamePasswordAuthenticationToken currentUser = new UsernamePasswordAuthenticationToken(username, null, authorities);  

        SecurityContextHolder
        .getContext()
        .setAuthentication(currentUser); 

        filterChain.doFilter(request, response);
        

    }





}
