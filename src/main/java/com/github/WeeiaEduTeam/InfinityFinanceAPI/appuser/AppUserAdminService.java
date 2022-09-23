package com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto.*;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.strategy.AppUserRoleStrategyFacade;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.TransactionAdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Slf4j
public class AppUserAdminService implements UserDetailsService {

    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private AppUserUtil appUserUtil;
    @Autowired
    private AppUserRoleStrategyFacade appUserRoleStrategyFacade;

    private TransactionAdminService transactionAdminService;

    public void setTransactionAdminService(TransactionAdminService transactionAdminService) {
        this.transactionAdminService = transactionAdminService;
    }

    public AppUser getUserById(long userId) {

        return appUserRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found in the database"));
    }

    public AppUser getUserByUserName(String username) {
        return appUserRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUserByUserName(username);
    }

    public Long getLoggedInUserId() {
        return appUserRepository.getLoggedInUserId().orElseThrow(() -> new UsernameNotFoundException("User not found in the database"));
    }

    public List<AppUserDTO> getAllUsers() {
        var foundUsers = appUserRepository.findAll();

        return foundUsers.stream().map(appUserUtil::mapToAppUserDTO).toList();
    }

    public AppUserDTO getSingleUser(long userId) {
        var foundUser = getUserById(userId);

        return appUserUtil.mapToAppUserDTO(foundUser);
    }

    public <T> AppUserDTO createAccount(T objectDTO) {
        AppUser user = createAppUserFromCreateAppUserDTOAndHashPassword(objectDTO);

        setRolesForUser(user, objectDTO);

        user = saveUser(user);

        return appUserUtil.mapToAppUserDTO(user); //TODO: new abstract layer
    }

    private <T> void setRolesForUser(AppUser user, T objectDTO) {
        appUserRoleStrategyFacade.addRolesForUser(user, objectDTO);
    }


    private <T> AppUser createAppUserFromCreateAppUserDTOAndHashPassword(T createAppUserDTO) {
        return appUserUtil.mapToAppUserFactory(createAppUserDTO);
    }
    @Transactional
    public void findAndDeleteUserWithRolesAndTransactions(long userId) {
        deleteTransactionsRelatedWithUser(userId);
        deleteUserWithRoles(userId);
    }

    private void deleteTransactionsRelatedWithUser(long userId) {
        transactionAdminService.deleteTransactionsRelatedWithUser(userId);
    }

    private void deleteUserWithRoles(long userId) {
        var user = getUserById(userId);
        deleteUser(user);
        deleteRoleFromUser(user);
    }

    private void deleteRoleFromUser(AppUser user) {
        appUserRoleStrategyFacade.removeRoles(user);
    }

    private void deleteUser(AppUser user) {
        appUserRepository.delete(user);
    }

    private AppUser saveUser(AppUser user) {
        return appUserRepository.save(user);
    }

    @Transactional
    public AppUserDTO replaceUserAllDetails(long userId, ReplaceAppUserAllDetailsDTO replaceAppUserAllDetailsDTO) {
        var foundUser = getUserById(userId);

        var overwrittenUser = overwriteAppUserAllDetails(foundUser, replaceAppUserAllDetailsDTO);

        return mapAppUserToAppUserDTO(overwrittenUser);
    }

    private AppUserDTO mapAppUserToAppUserDTO(AppUser user) {
        return appUserUtil.mapToAppUserDTO(user);
    }

    private AppUser overwriteAppUserAllDetails(AppUser foundUser, ReplaceAppUserAllDetailsDTO replaceAppUserAllDetailsDTO) {
        return appUserUtil.overwriteAppUserAllDetails(foundUser, replaceAppUserAllDetailsDTO);
    }
}
