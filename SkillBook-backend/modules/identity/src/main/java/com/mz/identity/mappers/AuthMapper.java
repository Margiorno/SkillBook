package com.mz.identity.mappers;

import com.mz.identity.auth.AuthRequest;
import com.mz.identity.generated.types.LoginInput;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ValueMapping;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    @Mappings({
            @Mapping(target = "type", source = "authType"),
            @Mapping(target = "email", source = "email"),
            @Mapping(target = "password", source = "password"),
            @Mapping(target = "token", source = "token")
    })
    AuthRequest toCommand(LoginInput input);

    @ValueMapping(source = "GOOGLE", target = "GOOGLE")
    com.mz.identity.enums.AuthType map(
            com.mz.identity.generated.types.AuthType authType
    );
}
