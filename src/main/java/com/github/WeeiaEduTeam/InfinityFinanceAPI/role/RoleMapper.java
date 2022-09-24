package com.github.WeeiaEduTeam.InfinityFinanceAPI.role;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.dto.RoleDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RoleMapper {
    public RoleDTO mapToRoleDTO(Role role) {
        return RoleDTO.builder()
                .name(role.getName())
                .build();
    }

    public Role buildRole(String roleName) {
        return Role.builder()
                .name(RoleType.ROLE_ADMIN.getName())
                .build();
    }
}
