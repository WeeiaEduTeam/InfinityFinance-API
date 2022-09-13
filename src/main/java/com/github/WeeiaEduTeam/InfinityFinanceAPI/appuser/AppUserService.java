package com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto.AppUserDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto.CreateAppUserDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.Role;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppUserService implements UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final AppUserUtil appUserUtil;
    private final RoleService roleService;

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

        return foundUsers.stream().map(appUserUtil::mapAppUserToAppUserDTO).toList();
    }

    public AppUserDTO createUser(CreateAppUserDTO createAppUserDTO) {
        AppUser user = createAppUserFromCreateAppUserDTOAndHashPassword(createAppUserDTO);

        user.setRoles(getUserRoleList());

        user = appUserRepository.save(user);

        return appUserUtil.mapAppUserDTOToAppUser(user);
    }

    private List<Role> getUserRoleList() {
        return List.of(roleService.getUserRoleOrCreate());
    }

    private AppUser createAppUserFromCreateAppUserDTOAndHashPassword(CreateAppUserDTO createAppUserDTO) {
        return appUserUtil.mapCreateAppUserDTOToAppUserAndHashPassword(createAppUserDTO);
    }
}
