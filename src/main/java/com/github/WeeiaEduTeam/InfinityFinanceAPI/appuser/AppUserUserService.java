package com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto.AppUserCredentialsDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto.AppUserDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto.CreateAppUserUserDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto.ReplaceAppUserByUserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AppUserUserService {

    private final AppUserAdminService appUserAdminService;
    private final AppUserRepository appUserRepository;
    private final AppUserUtil appUserUtil;

    public AppUserDTO getCurrentLoggedUserInformation() {
        long loggedInUserId = getLoggedUserId();

        var user = getUserById(loggedInUserId);

        return appUserUtil.mapToAppUserDTO(user);
    }

    private AppUser getUserById(long id) {
        return appUserAdminService.getUserById(id);
    }

    private long getLoggedUserId() {
        return appUserAdminService.getLoggedInUserId();
    }

    public void deleteCurrentLoggedUser() {
        long loggedInUserId = getLoggedUserId();

        appUserAdminService.findAndDeleteUserWithRolesAndTransactions(loggedInUserId);
    }

    public AppUserDTO createAccount(CreateAppUserUserDTO createAppUserUserDTO) {
        return appUserAdminService.createAccount(createAppUserUserDTO);
    }

    public AppUserDTO replaceUserCredentials(AppUserCredentialsDTO appUserCredentialsDTO) {
        long loggedInUserId = getLoggedUserId();

        var foundUser = getUserById(loggedInUserId);

        var overwrittenUser = overwriteAppUserCredentials(foundUser, appUserCredentialsDTO);

        overwrittenUser = saveUser(overwrittenUser);

        return mapAppUserToAppUserDTO(overwrittenUser);
    }

    private AppUser overwriteAppUserCredentials(AppUser foundUser, AppUserCredentialsDTO appUserCredentialsDTO) {
        return appUserUtil.overwriteAppUserCredentials(foundUser, appUserCredentialsDTO);
    }

    public AppUserDTO replaceUserDetails(ReplaceAppUserByUserDTO replaceAppUserByUserDTO) {
        long loggedInUserId = getLoggedUserId();

        var foundUser = getUserById(loggedInUserId);

        var overwrittenUser = overwriteAppUserDetails(foundUser, replaceAppUserByUserDTO);

        overwrittenUser = saveUser(overwrittenUser);

        return mapAppUserToAppUserDTO(overwrittenUser);
    }

    public AppUser overwriteAppUserDetails(AppUser foundUser, ReplaceAppUserByUserDTO replaceAppUserByUserDTO) {
        return appUserUtil.overwriteAppUserDetails(foundUser, replaceAppUserByUserDTO);
    }

    private AppUser saveUser(AppUser user) {
        return appUserRepository.save(user);
    }

    private AppUserDTO mapAppUserToAppUserDTO(AppUser user) {
        return appUserUtil.mapToAppUserDTO(user);
    }
}
