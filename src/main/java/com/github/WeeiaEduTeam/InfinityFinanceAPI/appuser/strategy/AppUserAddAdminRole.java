package com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.strategy;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.AppUser;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.Role;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.RoleType;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.RoleService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AppUserAddAdminRole implements AppUserRoleStrategy {

    private static AppUserAddAdminRole instance = null;
    private final RoleService roleService;

    private AppUserAddAdminRole(RoleService roleService) {
        this.roleService = roleService;
    }

    public static AppUserAddAdminRole getInstance(RoleService roleService) {
        if (instance == null) {
            instance = new AppUserAddAdminRole(roleService);
        }

        return instance;
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
                .filter(e -> e.equals(RoleType.ROLE_ADMIN.getName()))
                .toList()
                .size() > 0;
    }


    private List<Role> getAdminRoleList() {
        return List.of(roleService.getUserRoleOrCreate(), roleService.getAdminRoleOrCreate());
    }
}
