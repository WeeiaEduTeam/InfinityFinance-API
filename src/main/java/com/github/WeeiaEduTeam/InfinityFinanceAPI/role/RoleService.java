package com.github.WeeiaEduTeam.InfinityFinanceAPI.role;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.AppUser;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.role.RoleDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    public void deleteRoleFromUser(AppUser user) {
        var roles = user.getRoles();

        roles.stream()
                .map(e -> getRole(e.getName()))
                .forEach(e -> e.getUsers().remove(user));
    }
}
