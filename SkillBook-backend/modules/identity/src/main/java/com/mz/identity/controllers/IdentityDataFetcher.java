package com.mz.identity.controllers;

import com.mz.identity.auth.AuthRequest;
import com.mz.identity.generated.types.AuthPayload;
import com.mz.identity.generated.types.LoginInput;
import com.mz.identity.mappers.AuthMapper;
import com.mz.identity.service.AuthService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.InputArgument;
import lombok.RequiredArgsConstructor;

@DgsComponent
@RequiredArgsConstructor
public class IdentityDataFetcher {

    private final AuthMapper authMapper;
    private final AuthService authService;

    @DgsMutation
    public AuthPayload login(@InputArgument LoginInput input){
        AuthRequest request = authMapper.toCommand(input);

        String token = authService.login(request);

        return AuthPayload.newBuilder().token(token).build();
    }
}
