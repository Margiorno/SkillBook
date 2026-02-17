package com.mz.identity.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class GoogleConfig {

    @Bean
    public NetHttpTransport netHttpTransport() {
        return new NetHttpTransport();
    }

    @Bean
    public GoogleIdTokenVerifier googleIdTokenVerifier(GoogleProperties googleProperties,
                                                      NetHttpTransport netHttpTransport) {

        return new GoogleIdTokenVerifier.Builder(
                netHttpTransport,
                new GsonFactory())
                .setAudience(Collections.singletonList(googleProperties.getClientId()))
                .build();
    }


}
