package com.github.WeeiaEduTeam.InfinityFinanceAPI.role;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.AppUser;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.AppUserRepository;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.dto.RoleDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import static com.github.WeeiaEduTeam.InfinityFinanceAPI.role.RoleUtil.ROLE_ADMIN;
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

    public Role getAdminRoleOrCreate() {
        var role = getRole(ROLE_ADMIN);

        if(role == null)
            role = createRole(ROLE_ADMIN);

        return role;
    }

    private Role getRole(String name) {
        return roleRepository.findOneByName(name);
    }

    private Role createRole(String name) {
        var role = roleUtil.buildRole(name);

        return saveRole(role);
    }

    private Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    public RoleDTO mapToRoleDTO(Role role) {
        return roleUtil.mapRoleToRoleDTO(role);
    }

    public void deleteRolesFromUser(AppUser user) {
        int roleLength = user.getRoles().size();

        if (roleLength > 0) {
            user.getRoles().subList(0, roleLength).clear();
        }
    }
}
