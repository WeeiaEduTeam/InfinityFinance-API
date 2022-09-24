package com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto.*;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.Role;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.RoleService;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.dto.RoleDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class AppUserMapperFactory {

    @Autowired
    private PasswordEncoder encoder;
    @Autowired //INTO MAPPER SOON
    private RoleService roleService;

    public <T> AppUser mapToAppUserFactory(T dto) {
        AppUser user = null;

        /* Keep taken dtos higher than others
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

    private AppUser mapToAppUser(ReplaceAppUserAllDetailsDTO dto) {

        return AppUser.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .email(dto.getEmail())
                .secondName(dto.getSecondName())
                .firstName(dto.getFirstName())
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


    //INTO MAPPER SOON

    private List<RoleDTO> mapToRolesDTO(List<Role> roles) {
        return roleService.mapToRolesDTO(roles);
    }
}
