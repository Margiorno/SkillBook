package com.mz.identity.mappers;

import com.mz.identity.auth.AuthRequest;
import com.mz.identity.generated.types.LoginInput;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public interface AuthMapper {

    @Mappings({
            @Mapping(target = "type", source = "authType"),
            @Mapping(target = "email", source = "email"),
            @Mapping(target = "password", source = "password"),
            @Mapping(target = "token", source = "token")
    })
    AuthRequest toCommand(LoginInput input);

    com.mz.identity.enums.AuthType map(
            com.mz.identity.generated.types.AuthType authType
    );
}
