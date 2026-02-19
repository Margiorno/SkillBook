package com.mz.identity.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.ValueMapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @ValueMapping(source = "USER", target = "USER")
    com.mz.identity.models.Role mapRoleFromCommonToModel(
            com.mz.common.security.Role role
    );

    @ValueMapping(source = "USER", target = "USER")
    com.mz.common.security.Role mapRoleFromModelToCommon(
            com.mz.identity.models.Role role
    );
}
