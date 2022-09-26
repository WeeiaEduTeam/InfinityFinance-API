package com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto.*;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.rolestrategy.AppUserRoleStrategyFacade;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.util.CustomPageable;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.TransactionAdminService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.function.Function;

@Service
@Slf4j
public class AppUserAdminService implements UserDetailsService {

    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private AppUserUtil appUserUtil;
    @Autowired
    private AppUserRoleStrategyFacade appUserRoleStrategyFacade;
    @Autowired
    private CustomPageable customPageable;

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

    public List<AppUserDTO> getAllUsers(Integer pageNumber, Sort.Direction sortDirection, String sortBy) {
        Pageable page = validateAndCreatePageable(pageNumber, sortDirection, sortBy);

        var foundUsers = appUserRepository.findAll(page);

        return foundUsers.stream().map(mapToAppUserDTO()).toList();
    }

    @NotNull
    private Function<AppUser, AppUserDTO> mapToAppUserDTO() {
        return appUserUtil::mapToAppUserDTO;
    }

    private Pageable validateAndCreatePageable(int pageNumber, Sort.Direction sortDirection, String sortBy) {
        return customPageable.validateAndCreatePageable(pageNumber, sortDirection, sortBy, AppUser.class);
    }

    public AppUserDTO getSingleUser(long userId) {
        var foundUser = getUserById(userId);

        return mapToAppUserDTO(foundUser);
    }

    private AppUserDTO mapToAppUserDTO(AppUser foundUser) {
        return appUserUtil.mapToAppUserDTO(foundUser);
    }

    public <T> AppUserDTO createAccount(T objectDTO) {
        AppUser user = createAppUserFromCreateAppUserDTOAndHashPassword(objectDTO);

        setRolesForUser(user, objectDTO);

        user = saveUser(user);

        return mapToAppUserDTO(user);
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

    AppUser saveUser(AppUser user) {
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
