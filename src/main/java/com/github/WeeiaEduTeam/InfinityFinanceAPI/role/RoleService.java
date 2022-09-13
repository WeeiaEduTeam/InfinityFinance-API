package com.github.WeeiaEduTeam.InfinityFinanceAPI.role;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.role.RoleDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.github.WeeiaEduTeam.InfinityFinanceAPI.role.RoleUtil.ROLE_USER;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleUtil roleUtil;

    public Role getUserRoleOrCreate() {
        var role = getRole(ROLE_USER);

        if(role == null)
            role = createRole(ROLE_USER);

        return role;
    }

    private Role getRole(String name) {
        return roleRepository.findOneByName(name);
    }

    private Role createRole(String name) {
        var role = roleUtil.buildRole(name);

        return roleRepository.save(role);
    }

    public RoleDTO mapRoleToRoleDTO(Role role) {
        return roleUtil.mapRoleToRoleDTO(role);
    }
}
