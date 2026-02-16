package com.mz.common.security;

import java.util.List;
import java.util.UUID;

public class UserPrincipal {
    private UUID userId;
    private String email;
    private List<Role> roles;
}
