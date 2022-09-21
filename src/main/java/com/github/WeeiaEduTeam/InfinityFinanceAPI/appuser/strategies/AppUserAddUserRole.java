package com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.strategies;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.AppUser;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.Role;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.github.WeeiaEduTeam.InfinityFinanceAPI.role.RoleUtil.ROLE_USER;

@Component
public class AppUserAddUserRole implements AppUserRoleStrategy {

    private final RoleService roleService;

    public AppUserAddUserRole(RoleService roleService) {
        this.roleService = roleService;
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
