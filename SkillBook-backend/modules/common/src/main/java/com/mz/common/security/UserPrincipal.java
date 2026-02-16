package com.mz.common.security;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class UserPrincipal {
    private UUID userId;
    private String email;
    private List<Role> roles;
}
