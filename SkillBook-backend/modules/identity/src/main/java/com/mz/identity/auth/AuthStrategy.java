package com.mz.identity.auth;

import com.mz.common.security.UserPrincipal;
import com.mz.identity.enums.AuthType;

public interface AuthStrategy {
    boolean supports(AuthType authType);

    UserPrincipal authenticate(AuthRequest request);
}
