package com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.strategies;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.AppUser;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.RoleService;

public class AppUserRoleRemover {

    private final RoleService roleService;
    public AppUserRoleRemover(RoleService roleService) {
        this.roleService = roleService;
    }


    public void deleteRolesForUser(AppUser user) {
        roleService.deleteRoleFromUser(user);
    }
}
