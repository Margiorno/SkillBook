package com.mz.identity.controllers;

import com.mz.identity.generated.types.AuthPayload;
import com.mz.identity.generated.types.LoginInput;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.InputArgument;
import lombok.RequiredArgsConstructor;

@DgsComponent
@RequiredArgsConstructor
public class IdentityDataFetcher {

//    @DgsMutation
//    public AuthPayload login(@InputArgument LoginInput input){
//
//    }
//}
