package com.mz.security.filter;

import com.mz.common.security.Role;
import com.mz.common.security.UserPrincipal;
import com.mz.security.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        extractToken(request)
                .flatMap(this::parseToken)
                .ifPresent(claims -> authenticateUser(claims, request));

        filterChain.doFilter(request, response);
    }

    private Optional<String> extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            return Optional.of(authHeader.substring(BEARER_PREFIX.length()));
        }
        return Optional.empty();
    }

    private Optional<Claims> parseToken(String token) {
        try {
            return Optional.of(jwtService.extractClaims(token));
        } catch (JwtException e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
            return Optional.empty();
        }
    }

    private void authenticateUser(Claims claims, HttpServletRequest request) {
        if (isAlreadyAuthenticated()) {
            return;
        }

        String userId = claims.getSubject();
        if (userId == null) {
            return;
        }

        List<Role> roles = extractRoles(claims);
        UserPrincipal principal = buildPrincipal(claims, userId, roles);
        setAuthentication(principal, roles, request);
    }

    private boolean isAlreadyAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication() != null;
    }

    @SuppressWarnings("unchecked")
    private List<Role> extractRoles(Claims claims) {
        List<String> rolesString = claims.get("roles", List.class);
        if (rolesString == null) {
            return Collections.emptyList();
        }
        return rolesString.stream()
                .map(this::parseRole)
                .flatMap(Optional::stream)
                .toList();
    }

    private Optional<Role> parseRole(String roleName) {
        try {
            return Optional.of(Role.valueOf(roleName));
        } catch (IllegalArgumentException e) {
            log.warn("Unknown role in JWT: {}", roleName);
            return Optional.empty();
        }
    }

    private UserPrincipal buildPrincipal(Claims claims, String userId, List<Role> roles) {
        return UserPrincipal.builder()
                .userId(userId)
                .email(claims.get("email", String.class))
                .roles(roles)
                .build();
    }

    private void setAuthentication(UserPrincipal principal, List<Role> roles, HttpServletRequest request) {
        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .toList();

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                principal, null, authorities
        );
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}
