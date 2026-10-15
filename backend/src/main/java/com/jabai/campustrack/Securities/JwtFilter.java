package com.jabai.campustrack.Securities;

import java.io.IOException;

import io.jsonwebtoken.Claims;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

    public JwtFilter(JwtUtil jwtUtil) {
      this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
      String header = request.getHeader("Authorization");

      if (header != null && header.startsWith("Bearer ") && SecurityContextHolder.getContext().getAuthentication() == null) {
        Claims claims = jwtUtil.parseToken(header.substring(7));

        if (claims != null) {
          String userId = claims.getSubject();
          String role = claims.get("role", String.class);

          if (isNumber(userId) && role != null && !role.isBlank()) {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userId,
                    null,
                    List.of(new SimpleGrantedAuthority("ROLE_" + role)));
            SecurityContextHolder.getContext().setAuthentication(authentication);
          }
        }
      }

      filterChain.doFilter(request, response);
    }

    private boolean isNumber(String value) {
      try {
        Long.parseLong(value);
        return true;
      } catch (NumberFormatException | NullPointerException e) {
        return false;
    }
  }
}
