package com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser;


import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto.*;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.strategy.AppUserRoleStrategyFacade;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.Role;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.RoleService;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.dto.RoleDTO;
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
    private final AppUserRoleStrategyFacade appUserRoleStrategyFacade;

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

    private AppUser mapToAppUser(CreateAppUserAdminDTO dto) {

        return AppUser.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build();
    }

    private AppUser mapToAppUser(ReplaceAppUserByUserDTO dto) {

        return AppUser.builder()
                .firstName(dto.getFirstName())
                .email(dto.getEmail())
                .secondName(dto.getSecondName())
                .build();
    }

    private AppUser mapToAppUser(AppUserCredentialsDTO dto) {

        return AppUser.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .build();
    }

    private AppUser mapToAppUser(CreateAppUserUserDTO dto) {

        return AppUser.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .email(dto.getEmail())
                .build();
    }

    private AppUser mapToAppUser(ReplaceAppUserAllDetailsDTO dto) {

        return AppUser.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .email(dto.getEmail())
                .secondName(dto.getSecondName())
                .firstName(dto.getFirstName())
                .build();
    }

    //TODO: move all mappers and factory into AppUserFactory
    public <T> AppUser mapToAppUserFactory(T dto) {
        AppUser user = null;

        /* Keep given dtos higher than others
         * ReplaceAll details > CreateAdminDTO > CreateUserDTO
         * because of the inheritance */
        if(dto instanceof ReplaceAppUserAllDetailsDTO) {
            user = mapToAppUser((ReplaceAppUserAllDetailsDTO) dto);
            user.setPassword(hashPassword(((ReplaceAppUserAllDetailsDTO) dto).getPassword()));
        } else if(dto instanceof CreateAppUserAdminDTO) {
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

    public AppUser overwriteAppUserAllDetails(AppUser foundUser, ReplaceAppUserAllDetailsDTO replaceAppUserAllDetailsDTO) {
        var convertedUser = mapToAppUserFactory(replaceAppUserAllDetailsDTO);

        foundUser.setPassword(convertedUser.getPassword());
        foundUser.setUsername(convertedUser.getUsername());
        foundUser.setEmail(convertedUser.getEmail());
        foundUser.setSecondName(convertedUser.getSecondName());
        foundUser.setFirstName(convertedUser.getFirstName());

        appUserRoleStrategyFacade.removeRoles(foundUser);

        // USTAWIC ROLE I WYWALIC ROLE SERVICE KONTAKT Z FASADA
        appUserRoleStrategyFacade.addRolesForUser(foundUser, replaceAppUserAllDetailsDTO);


        return foundUser;
    }
}
