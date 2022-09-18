package com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser;


import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto.*;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.Role;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.RoleService;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.role.RoleDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class AppUserUtil {
    private final PasswordEncoder encoder;
    private final RoleService roleService;

    public AppUserDTO mapToAppUserDTO(AppUser appUser) {

        return AppUserDTO.builder()
                .id(appUser.getId())
                .username(appUser.getUsername())
                .email(appUser.getEmail())
                .firstName(appUser.getFirstName())
                .secondName(appUser.getSecondName())
                .roles(mapToRolesDTO(appUser.getRoles()))
                .build();
    }

    private List<RoleDTO> mapToRolesDTO(List<Role> roles) {
        return roles.stream()
                .map(roleService::mapToRoleDTO)
                .toList();
    }

    private AppUser mapToAppUser(CreateAppUserAdminDTO createAppUserAdminDTO) {

        return AppUser.builder()
                .username(createAppUserAdminDTO.getUsername())
                .email(createAppUserAdminDTO.getEmail())
                .password(createAppUserAdminDTO.getPassword())
                .build();
    }

    private AppUser mapToAppUser(ReplaceAppUserByUserDTO replaceAppUserByUserDTO) {

        return AppUser.builder()
                .firstName(replaceAppUserByUserDTO.getFirstName())
                .email(replaceAppUserByUserDTO.getEmail())
                .secondName(replaceAppUserByUserDTO.getSecondName())
                .build();
    }

    private AppUser mapToAppUser(AppUserCredentialsDTO appUserCredentialsDTO) {

        return AppUser.builder()
                .username(appUserCredentialsDTO.getUsername())
                .password(appUserCredentialsDTO.getPassword())
                .build();
    }

    private AppUser mapToAppUser(CreateAppUserUserDTO createAppUserUserDTO) {

        return AppUser.builder()
                .username(createAppUserUserDTO.getUsername())
                .password(createAppUserUserDTO.getPassword())
                .email(createAppUserUserDTO.getEmail())
                .build();
    }

    //TODO: move all mappers and factory into AppUserFactory
    public <T> AppUser mapToAppUserFactory(T dto) {
        AppUser user = null;

        if(dto instanceof CreateAppUserAdminDTO) {
            user = mapToAppUser((CreateAppUserAdminDTO) dto);
            user.setPassword(hashPassword(((CreateAppUserAdminDTO) dto).getPassword()));
        } else if(dto instanceof CreateAppUserUserDTO) {
            user = mapToAppUser((CreateAppUserUserDTO) dto);
            user.setPassword(hashPassword(((CreateAppUserUserDTO) dto).getPassword()));
        } else if(dto instanceof ReplaceAppUserByUserDTO) {
            user = mapToAppUser((ReplaceAppUserByUserDTO) dto);
        } else if(dto instanceof AppUserCredentialsDTO) {
            user = mapToAppUser((AppUserCredentialsDTO) dto);
            user.setPassword(hashPassword(((AppUserCredentialsDTO) dto).getPassword()));
        }

        return user;
    }

    private String hashPassword(String password) {

        return encoder.encode(password);
    }

    public AppUser overwriteAppUserDetails(AppUser foundUser, ReplaceAppUserByUserDTO replaceAppUserByUserDTO) {
        var convertedUser = mapToAppUserFactory(replaceAppUserByUserDTO);

        foundUser.setFirstName(convertedUser.getFirstName());
        foundUser.setSecondName(convertedUser.getSecondName());
        foundUser.setEmail(convertedUser.getEmail());

        return foundUser;
    }

    public AppUser overwriteAppUserCredentials(AppUser foundUser, AppUserCredentialsDTO appUserCredentialsDTO) {
        var convertedUser = mapToAppUserFactory(appUserCredentialsDTO);

        foundUser.setPassword(convertedUser.getPassword());
        foundUser.setUsername(convertedUser.getUsername());

        return foundUser;
    }
}
