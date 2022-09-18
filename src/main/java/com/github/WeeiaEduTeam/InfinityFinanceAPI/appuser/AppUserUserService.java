package com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto.AppUserDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto.CreateAppUserUserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AppUserUserService {

    private final AppUserAdminService appUserAdminService;
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
}
