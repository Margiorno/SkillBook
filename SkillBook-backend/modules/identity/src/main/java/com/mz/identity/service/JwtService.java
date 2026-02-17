package com.mz.identity.service;

import com.mz.common.security.UserPrincipal;
import com.mz.identity.config.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;

    public String generateToken(UserPrincipal principal) {
        SecretKey key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .subject(principal.getUserId())
                .claim("email", principal.getEmail())
                .claim("roles", principal.getRoles())
                .issuedAt(new Date())
                .expiration(Date.from(
                        Instant.now().plusSeconds(jwtProperties.getBearerExpiration())))
                .signWith(key)
                .compact();
    }
}
