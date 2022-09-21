package com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.strategies;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.AppUser;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto.CreateAppUserAdminDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto.CreateAppUserUserDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.github.WeeiaEduTeam.InfinityFinanceAPI.role.RoleUtil.ROLE_ADMIN;

@Component
@Slf4j
public class AppUserRoleStrategyFacade {

    private final RoleService roleService;

    public AppUserRoleStrategyFacade(RoleService roleService) {
        this.roleService = roleService;
    }

    public <T> void addRolesForUser(AppUser user, T objectDTO) {
        createStrategy(objectDTO).addRolesForUser(user);
    }

    private <T> AppUserRoleStrategy createStrategy(T objectDTO) {
        if(isAdmin(objectDTO)) {
            return new AppUserAddAdminRole(roleService);
        } else if(isDefaultUser(objectDTO)) {
            return new AppUserAddUserRole(roleService);
        }

        throw new IllegalArgumentException("Error during create strategy occurred.");
    }

    private <T> boolean isDefaultUser(T objectDTO) {
        return isCreateAppUserUserDTO(objectDTO);
    }


    private <T> boolean isAdmin(T objectDTO) {
        if(isCreateAppUserAdminDTO(objectDTO)) {
            return ((CreateAppUserAdminDTO) objectDTO).hasRole(ROLE_ADMIN);
        }

        return false;
    }

    private <T> boolean isCreateAppUserUserDTO(T objectDTO) {
        return objectDTO instanceof CreateAppUserUserDTO;
    }

    private <T> boolean isCreateAppUserAdminDTO(T objectDTO) {
        return objectDTO instanceof CreateAppUserAdminDTO;
    }

    public void removeRoles(AppUser user) {
        new AppUserRoleRemover(roleService).deleteRolesForUser(user);
    }
}
