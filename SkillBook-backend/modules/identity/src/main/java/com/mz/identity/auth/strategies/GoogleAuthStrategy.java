package com.mz.identity.auth.strategies;

import com.mz.common.security.UserPrincipal;
import com.mz.identity.auth.AuthStrategy;
import com.mz.identity.enums.AuthType;
import org.springframework.stereotype.Component;

@Component
public class GoogleAuthStrategy implements AuthStrategy {
    @Override
    public boolean supports(AuthType authType) {
        return AuthType.GOOGLE.equals(authType);
    }

    @Override
    public UserPrincipal authenticate(String token) {
        return null;
    }
}
