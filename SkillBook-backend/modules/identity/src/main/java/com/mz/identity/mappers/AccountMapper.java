package com.mz.identity.mappers;

import com.mz.common.security.UserPrincipal;
import com.mz.identity.models.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;


@Mapper(componentModel = "spring", uses = { RoleMapper.class })
public interface AccountMapper {

    @Mappings({
            @Mapping(target = "userId", source = "id"),
            @Mapping(target = "email", source = "email"),
            @Mapping(target = "roles", source = "roles")
    })
    UserPrincipal toUserPrincipal(Account savedAccount);
}
