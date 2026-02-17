package com.mz.identity.auth;

import com.mz.identity.enums.AuthType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthRequest {
    private final AuthType type;
    private final String token;
    private final String email;
    private final String password;
}
