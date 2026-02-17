package com.mz.common.security;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class UserPrincipal {
    private final String userId;
    private final String email;
    private final List<Role> roles;

}
