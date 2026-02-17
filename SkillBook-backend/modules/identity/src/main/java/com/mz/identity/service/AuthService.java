package com.mz.identity.service;

import com.mz.common.security.UserPrincipal;
import com.mz.identity.auth.AuthRequest;
import com.mz.identity.auth.AuthStrategy;
import com.mz.identity.exception.UnsupportedLoginMethodException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final List<AuthStrategy> strategies;
    private final JwtService jwtService;

    public String login(AuthRequest request) {
        AuthStrategy strategy = strategies.stream()
                .filter(s->s.supports(request.getType()))
                .findFirst()
                .orElseThrow(()->new UnsupportedLoginMethodException("Unsupported Login Method Exception"));

        UserPrincipal principal = strategy.authenticate(request);

        return jwtService.generateToken(principal);
    }
}
