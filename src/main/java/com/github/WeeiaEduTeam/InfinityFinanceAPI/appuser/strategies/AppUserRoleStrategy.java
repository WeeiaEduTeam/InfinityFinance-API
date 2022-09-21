package com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.strategies;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.AppUser;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.RoleService;

public interface AppUserRoleStrategy {
    void addRolesForUser(AppUser user);
}
