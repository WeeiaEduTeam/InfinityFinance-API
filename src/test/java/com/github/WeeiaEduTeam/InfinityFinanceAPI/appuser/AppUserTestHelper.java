package com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto.*;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.Role;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.dto.RoleDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Collections;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AppUserTestHelper extends  AppUserNameHolder{
    protected AppUser appUserTest;
    protected AppUserDTO appUserDTOTest;
    protected Role roleTest;
    protected RoleDTO roleDTOTest;
    protected CreateAppUserAdminDTO createAppUserAdminDTOTest;
    protected CreateAppUserUserDTO createAppUserUserDTO;
    protected AppUserCredentialsDTO appUserCredentialsDTO;
    protected ReplaceAppUserByUserDTO replaceAppUserByUserDTO;
    protected ReplaceAppUserAllDetailsDTO replaceAppUserAllDetailsDTO;


    @BeforeEach
    void init() {
        roleTest = Role.builder()
                .id(TEST_ID)
                .name(TEST_ROLENAME)
                .build();

        roleDTOTest = RoleDTO.builder()
                .name(TEST_ROLENAME)
                .build();

        appUserTest = AppUser.builder()
                .id(TEST_ID)
                .email(TEST_EMAIL)
                .firstName(TEST_FIRSTNAME)
                .secondName(TEST_SECONDNAME)
                .username(TEST_USERNAME)
                .password(TEST_PLAINTEXT_PASSWORD)
                .roles(Collections.singletonList(roleTest))
                .build();

        appUserDTOTest = AppUserDTO.builder()
                .id(TEST_ID)
                .email(TEST_EMAIL)
                .firstName(TEST_FIRSTNAME)
                .secondName(TEST_SECONDNAME)
                .username(TEST_USERNAME)
                .roles(Collections.singletonList(roleDTOTest))
                .build();

        createAppUserAdminDTOTest = new CreateAppUserAdminDTO();
        createAppUserAdminDTOTest.setPassword(TEST_PLAINTEXT_PASSWORD);
        createAppUserAdminDTOTest.setEmail(TEST_EMAIL);
        createAppUserAdminDTOTest.setUsername(TEST_USERNAME);

        createAppUserUserDTO = CreateAppUserUserDTO.builder()
                .email(TEST_EMAIL)
                .username(TEST_USERNAME)
                .password(TEST_PLAINTEXT_PASSWORD)
                .build();

        appUserCredentialsDTO = AppUserCredentialsDTO.builder()
                .password(TEST_PLAINTEXT_PASSWORD)
                .username(TEST_USERNAME)
                .build();

        replaceAppUserByUserDTO = ReplaceAppUserByUserDTO.builder()
                .email(TEST_EMAIL)
                .firstName(TEST_FIRSTNAME)
                .secondName(TEST_SECONDNAME)
                .build();

        replaceAppUserAllDetailsDTO = new ReplaceAppUserAllDetailsDTO();
        replaceAppUserAllDetailsDTO.setPassword(TEST_PLAINTEXT_PASSWORD);
        replaceAppUserAllDetailsDTO.setEmail(TEST_EMAIL);
        replaceAppUserAllDetailsDTO.setUsername(TEST_USERNAME);
        replaceAppUserAllDetailsDTO.setFirstName(TEST_FIRSTNAME);
        replaceAppUserAllDetailsDTO.setSecondName(TEST_SECONDNAME);
        replaceAppUserAllDetailsDTO.setRoles(Collections.singletonList(roleDTOTest));

    }
}
