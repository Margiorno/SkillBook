package com.mz.common.security;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@RequiredArgsConstructor
public class UserPrincipal {
    private final String userId;
    private final String email;
    private final List<Role> roles;

}
