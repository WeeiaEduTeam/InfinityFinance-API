package com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser;


import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto.*;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.rolestrategy.AppUserRoleStrategyFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AppUserUtil {

    private final AppUserMapperFactory appUserMapperFactory;
    private final AppUserRoleStrategyFacade appUserRoleStrategyFacade;

    public <T> AppUser mapToAppUserFactory(T dto) {
        return appUserMapperFactory.mapToAppUserFactory(dto);
    }

    public AppUserDTO mapToAppUserDTO(AppUser appUser) {
        return appUserMapperFactory.mapToAppUserDTO(appUser);
    }

    public AppUser overwriteAppUserDetails(AppUser foundUser, ReplaceAppUserByUserDTO replaceAppUserByUserDTO) {
        var convertedUser = mapToAppUser(replaceAppUserByUserDTO);

        foundUser.setFirstName(convertedUser.getFirstName());
        foundUser.setSecondName(convertedUser.getSecondName());
        foundUser.setEmail(convertedUser.getEmail());

        return foundUser;
    }

    private <T> AppUser mapToAppUser(T dto) {
        return appUserMapperFactory.mapToAppUserFactory(dto);
    }

    public AppUser overwriteAppUserCredentials(AppUser foundUser, AppUserCredentialsDTO appUserCredentialsDTO) {
        var convertedUser = mapToAppUser(appUserCredentialsDTO);

        foundUser.setPassword(convertedUser.getPassword());
        foundUser.setUsername(convertedUser.getUsername());

        return foundUser;
    }

    public AppUser overwriteAppUserAllDetails(AppUser foundUser, ReplaceAppUserAllDetailsDTO replaceAppUserAllDetailsDTO) {
        var convertedUser = mapToAppUser(replaceAppUserAllDetailsDTO);

        foundUser.setPassword(convertedUser.getPassword());
        foundUser.setUsername(convertedUser.getUsername());
        foundUser.setEmail(convertedUser.getEmail());
        foundUser.setSecondName(convertedUser.getSecondName());
        foundUser.setFirstName(convertedUser.getFirstName());

        removeAndSetRoles(foundUser, replaceAppUserAllDetailsDTO);

        return foundUser;
    }

    private void removeAndSetRoles(AppUser foundUser, ReplaceAppUserAllDetailsDTO replaceAppUserAllDetailsDTO) {
        appUserRoleStrategyFacade.removeRoles(foundUser);

        appUserRoleStrategyFacade.addRolesForUser(foundUser, replaceAppUserAllDetailsDTO);
    }
}
