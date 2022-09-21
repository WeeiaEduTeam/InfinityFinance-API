package com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.strategies;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.AppUser;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.Role;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.RoleService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.github.WeeiaEduTeam.InfinityFinanceAPI.role.RoleUtil.ROLE_ADMIN;

@Component
public class AppUserAddAdminRole implements AppUserRoleStrategy {

    private final RoleService roleService;

    public AppUserAddAdminRole(RoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    public void addRolesForUser(AppUser user) {
        var roles = getRoles(user);

        if(!adminRoleExists(roles)) {
            var userRoles = getAdminRoleList();

            user.setRoles(userRoles);
        }
    }

    private List<Role> getRoles(AppUser user) {
        if(user.getRoles() == null)
            return new ArrayList<>();

        return user.getRoles();
    }

    private boolean adminRoleExists(List<Role> roles) {
        return roles.stream()
                .map(Role::getName)
                .filter(e -> e.equals(ROLE_ADMIN))
                .toList()
                .size() > 0;
    }


    private List<Role> getAdminRoleList() {
        return List.of(roleService.getUserRoleOrCreate(), roleService.getAdminRoleOrCreate());
    }
}
