package com.mz.identity.auth.strategies;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.mz.common.security.Role;
import com.mz.common.security.UserPrincipal;
import com.mz.identity.auth.AuthRequest;
import com.mz.identity.auth.AuthStrategy;
import com.mz.identity.enums.AuthType;
import com.mz.identity.exception.ExpiredGoogleTokenException;
import com.mz.identity.exception.InvalidGoogleTokenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoogleAuthStrategy implements AuthStrategy {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final GoogleIdTokenVerifier googleIdTokenVerifier;

    @Override
    public boolean supports(AuthType authType) {
        return AuthType.GOOGLE.equals(authType);
    }

    @Override
    public UserPrincipal authenticate(AuthRequest request) {
        String token = request.getToken();

        try {
            GoogleIdToken idToken = googleIdTokenVerifier.verify(token);

            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                log.debug("Verified google user: {}", payload.getEmail());

                return new UserPrincipal(
                        payload.getSubject(),
                        payload.getEmail(),
                        List.of(Role.USER)
                );
            }

            throwAppropriateException(token);

        } catch (IllegalArgumentException e) {
            log.warn("Malformed Google token: {}", e.getMessage());
            throw new InvalidGoogleTokenException("Malformed Google token");
        }  catch (GeneralSecurityException | IOException e) {
            log.warn("Failed to verify Google ID Token: {}", e.getMessage());
            throw new InvalidGoogleTokenException("Failed to verify Google ID Token");
        }

        throw new InvalidGoogleTokenException("Invalid Google ID Token");
    }

    private void throwAppropriateException(String token) {
        getExpirationTime(token)
                .filter(exp -> Instant.now().getEpochSecond() > exp)
                .ifPresent(exp -> {
                    log.warn("Google ID Token expired at {}", Instant.ofEpochSecond(exp));
                    throw new ExpiredGoogleTokenException("Google ID Token has expired");
                });

        log.error("Invalid Google ID Token");
    }

    private Optional<Long> getExpirationTime(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length < 2) return Optional.empty();

            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));
            return Optional.of(objectMapper.readTree(payloadJson).get("exp").asLong());
        } catch (Exception e) {
            log.debug("Could not parse token expiration", e);
            return Optional.empty();
        }
    }
}
