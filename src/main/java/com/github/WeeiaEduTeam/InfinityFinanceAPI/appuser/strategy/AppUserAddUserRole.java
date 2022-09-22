package com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.strategy;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.AppUser;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.Role;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.RoleService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.github.WeeiaEduTeam.InfinityFinanceAPI.role.RoleUtil.ROLE_USER;

@Component
public class AppUserAddUserRole implements AppUserRoleStrategy {

    private static AppUserAddUserRole instance = null;
    private final RoleService roleService;

    private AppUserAddUserRole(RoleService roleService) {
        this.roleService = roleService;
    }

    public static AppUserAddUserRole getInstance(RoleService roleService) {
        if (instance == null) {
            instance = new AppUserAddUserRole(roleService);
        }

        return instance;
    }

    @Override
    public void addRolesForUser(AppUser user) {
        var roles = getRoles(user);

        if(!userRoleExists(roles)) {
            var userRoles = getUserRoleList();

            user.setRoles(userRoles);
        }
    }

    private List<Role> getRoles(AppUser user) {
        if(user.getRoles() == null)
            return new ArrayList<>();

        return user.getRoles();
    }

    private boolean userRoleExists(List<Role> roles) {
        return roles.stream()
                .map(Role::getName)
                .filter(e -> e.equals(ROLE_USER))
                .toList()
                .size() > 0;
    }

    private List<Role> getUserRoleList() {
        return List.of(roleService.getUserRoleOrCreate());
    }
}
