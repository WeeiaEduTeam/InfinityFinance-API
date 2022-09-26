package com.github.WeeiaEduTeam.InfinityFinanceAPI.role;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.dto.RoleDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
class RoleUtil {

    private final RoleMapper roleMapper;
    public RoleDTO mapToRoleDTO(Role role) {
        return roleMapper.mapToRoleDTO(role);
    }

    public Role buildRole(String role) {
        role = validateStringRole(role);

        return roleMapper.buildRole(role);
    }

    private String validateStringRole(String role) {
        List<String> rolesFound = findRolesInEnum(role);

        checkErrors(rolesFound);

        return rolesFound.get(0);
    }

    @NotNull
    private List<String> findRolesInEnum(String role) {
        var enumValues = RoleType.values();

        return Arrays.stream(enumValues)
                .map(RoleType::getName)
                .map(String::toUpperCase)
                .filter(e -> e.contains(role.toUpperCase()))
                .toList();
    }

    private void checkErrors(List<String> rolesFound) {
        roleNotFound(rolesFound);
        tooManyRolesFound(rolesFound);
    }

    private void tooManyRolesFound(List<String> rolesFound) {
        if(rolesFound.size() > 1) {
            throw new IllegalArgumentException("Found more than one role, try to more specify your role name.");
        }
    }

    private void roleNotFound(List<String> rolesFound) {
        if(rolesFound.size() == 0) {
            throw new IllegalArgumentException("Role not found in enum, try enlarge enum and then add role to db.");
        }
    }
}
