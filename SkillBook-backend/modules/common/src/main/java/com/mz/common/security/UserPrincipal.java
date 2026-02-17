package com.mz.common.security;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@RequiredArgsConstructor
@Builder
public class UserPrincipal {
    private final String userId;
    private final String email;
    private final List<Role> roles;

}
