package com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser;


import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto.AppUserDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto.CreateAppUserDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.Role;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.RoleService;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.role.RoleDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class AppUserUtil {
    private final PasswordEncoder encoder;
    private final RoleService roleService;

    public AppUserDTO mapAppUserToAppUserDTO(AppUser appUser) {

        return AppUserDTO.builder()
                .id(appUser.getId())
                .username(appUser.getUsername())
                .email(appUser.getEmail())
                .firstName(appUser.getFirstName())
                .secondName(appUser.getSecondName())
                .roles(mapRolesToRolesDTO(appUser.getRoles()))
                .build();
    }

    private List<RoleDTO> mapRolesToRolesDTO(List<Role> roles) {
        return roles.stream()
                .map(roleService::mapRoleToRoleDTO)
                .toList();
    }

    private AppUser mapCreateAppUserDTOToAppUser(CreateAppUserDTO createAppUserDTO) {

        return AppUser.builder()
                .username(createAppUserDTO.getUsername())
                .email(createAppUserDTO.getEmail())
                .password(createAppUserDTO.getPassword())
                .build();
    }

    public AppUser mapCreateAppUserDTOToAppUserAndHashPassword(CreateAppUserDTO createAppUserDTO) {
        var user = mapCreateAppUserDTOToAppUser(createAppUserDTO);

        user.setPassword(hashPassword(createAppUserDTO.getPassword()));

        return user;
    }

    private String hashPassword(String password) {

        return encoder.encode(password);
    }

    public AppUserDTO mapAppUserDTOToAppUser(AppUser user) {

        return AppUserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .secondName(user.getSecondName())
                .firstName(user.getFirstName())
                .roles(mapRolesToRolesDTO(user.getRoles()))
                .build();
    }
}
