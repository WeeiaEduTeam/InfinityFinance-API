package com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.rolestrategy;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.AppUser;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.RoleService;

class AppUserRoleRemover {

    private static AppUserRoleRemover instance = null;
    private final RoleService roleService;

    private AppUserRoleRemover(RoleService roleService) {
        this.roleService = roleService;
    }

    public static AppUserRoleRemover getInstance(RoleService roleService) {
        if (instance == null) {
            instance = new AppUserRoleRemover(roleService);
        }

        return instance;
    }

    public void deleteRolesForUser(AppUser user) {
        roleService.deleteRolesFromUser(user);
    }
}
