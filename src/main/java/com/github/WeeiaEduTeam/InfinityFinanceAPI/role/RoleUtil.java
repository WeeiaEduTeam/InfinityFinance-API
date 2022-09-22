package com.github.WeeiaEduTeam.InfinityFinanceAPI.role;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.dto.RoleDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RoleUtil {

    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    public RoleDTO mapRoleToRoleDTO(Role role) {
        return RoleDTO.builder()
                .name(role.getName())
                .build();
    }

    public Role buildRole(String role) {
        return Role.builder()
                .name(role)
                .build();
    }
}
