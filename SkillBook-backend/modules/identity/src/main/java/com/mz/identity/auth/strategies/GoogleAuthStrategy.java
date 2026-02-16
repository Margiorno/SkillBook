package com.mz.identity.auth.strategies;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.mz.common.security.Role;
import com.mz.common.security.UserPrincipal;
import com.mz.identity.auth.AuthStrategy;
import com.mz.identity.config.GoogleProperties;
import com.mz.identity.enums.AuthType;
import com.mz.identity.exception.InvalidGoogleTokenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoogleAuthStrategy implements AuthStrategy {

    private final GoogleProperties googleProperties;

    @Override
    public boolean supports(AuthType authType) {
        return AuthType.GOOGLE.equals(authType);
    }

    @Override
    public UserPrincipal authenticate(String token) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    new GsonFactory())
                    .setAudience(Collections.singletonList(googleProperties.getClientId()))
                    .build();

            GoogleIdToken idToken = verifier.verify(token);

            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();

                String userId = payload.getSubject();
                String email = payload.getEmail();

                log.info("Verified google user: {}", email);


                return new UserPrincipal(
                        userId,
                        email,
                        List.of(Role.USER)
                );
            } else {
                log.error("Invalid Google ID Token");
                throw new InvalidGoogleTokenException("Invalid Google ID Token");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
