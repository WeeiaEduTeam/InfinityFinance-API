package com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto.*;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.Role;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.dto.RoleDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AppUserUserServiceTest {

    @InjectMocks
    private AppUserUserService appUserUserService;
    @Mock
    private AppUserAdminService appUserAdminService;
    @Mock
    private AppUserUtil appUserUtil;

    private AppUser appUserTest;
    private AppUserDTO appUserDTOTest;
    private Role roleTest;
    private RoleDTO roleDTOTest;
    private CreateAppUserUserDTO createAppUserUserDTO;
    private AppUserCredentialsDTO appUserCredentialsDTO;
    private ReplaceAppUserByUserDTO replaceAppUserByUserDTO;
    private ReplaceAppUserAllDetailsDTO replaceAppUserAllDetailsDTO;

    @BeforeEach
    void init() {
        roleTest = Role.builder()
                .id(1L)
                .name("TEST_ROLE")
                .build();

        roleDTOTest = RoleDTO.builder()
                .name("TEST_ROLE")
                .build();

        appUserTest = AppUser.builder()
                .id(1L)
                .password("{noop}example")
                .email("testemail@wp.pl")
                .firstName("John")
                .secondName("Smith")
                .username("smith123")
                .password("123")
                .roles(Collections.singletonList(roleTest))
                .build();

        appUserDTOTest = AppUserDTO.builder()
                .id(1L)
                .email("testemail@wp.pl")
                .firstName("John")
                .secondName("Smith")
                .username("smith123")
                .roles(Collections.singletonList(roleDTOTest))
                .build();

        createAppUserUserDTO = new CreateAppUserUserDTO();
        createAppUserUserDTO.setPassword("{noop}example");
        createAppUserUserDTO.setEmail("example@wp.pl");
        createAppUserUserDTO.setUsername("example");

        appUserCredentialsDTO = AppUserCredentialsDTO.builder()
                .password("{noop}example")
                .username("example")
                .build();

        replaceAppUserByUserDTO = ReplaceAppUserByUserDTO.builder()
                .email("example@wp.pl")
                .firstName("example")
                .secondName("example")
                .build();

        replaceAppUserAllDetailsDTO = new ReplaceAppUserAllDetailsDTO();
        replaceAppUserAllDetailsDTO.setPassword("{noop}example");
        replaceAppUserAllDetailsDTO.setEmail("example@wp.pl");
        replaceAppUserAllDetailsDTO.setUsername("example");
        replaceAppUserAllDetailsDTO.setFirstName("example");
        replaceAppUserAllDetailsDTO.setSecondName("example");
        replaceAppUserAllDetailsDTO.setRoles(Collections.singletonList(roleDTOTest));

    }

    @Test
    @DisplayName("Should get current logged user information.")
    void shouldGetCurrentLoggedUserInformation() {
        //given
        given(appUserAdminService.getLoggedInUserId()).willReturn(appUserTest.getId());
        given(appUserAdminService.getUserById(anyLong())).willReturn(appUserTest);
        given(appUserUtil.mapToAppUserDTO(any(AppUser.class))).willReturn(appUserDTOTest);

        // when
        var user = appUserUserService.getCurrentLoggedUserInformation();

        //then
        assertThat(user, instanceOf(AppUserDTO.class));
        assertThat(user, hasProperty("id", equalTo(1L)));
        assertThat(user, hasProperty("email", equalTo("testemail@wp.pl")));
        assertThat(user, hasProperty("firstName", equalTo("John")));
        assertThat(user, hasProperty("secondName", equalTo("Smith")));
        assertThat(user, hasProperty("username", equalTo("smith123")));
        assertEquals(1, user.getRoles().size());
        assertThat(user.getRoles().get(0), hasProperty("name", equalTo("TEST_ROLE")));
    }

    @Test
    @DisplayName("Should register user.")
    void shouldRegisterUser() {
        given(appUserAdminService.createAccount(any(CreateAppUserUserDTO.class))).willReturn(appUserDTOTest);

        // when
        var user = appUserUserService.createAccount(createAppUserUserDTO);

        //then
        assertThat(user, instanceOf(AppUserDTO.class));
        assertThat(user, hasProperty("id", equalTo(1L)));
        assertThat(user, hasProperty("email", equalTo("testemail@wp.pl")));
        assertThat(user, hasProperty("firstName", equalTo("John")));
        assertThat(user, hasProperty("secondName", equalTo("Smith")));
        assertThat(user, hasProperty("username", equalTo("smith123")));
        assertEquals(1, user.getRoles().size());
        assertThat(user.getRoles().get(0), hasProperty("name", equalTo("TEST_ROLE")));
    }

    @Test
    @DisplayName("Should delete current logged user.")
    void shouldDeleteUser() {
        given(appUserAdminService.getLoggedInUserId()).willReturn(appUserTest.getId());

        // when
        appUserUserService.deleteCurrentLoggedUser();

        //then
        verify(appUserAdminService).findAndDeleteUserWithRolesAndTransactions(appUserTest.getId());
    }

    @Test
    @DisplayName("Should replace current user credentials.")
    void shouldReplaceCurrentLoggedUserCredentials() {
        given(appUserAdminService.getLoggedInUserId()).willReturn(appUserTest.getId());
        given(appUserAdminService.getUserById(anyLong())).willReturn(appUserTest);
        given(appUserAdminService.saveUser(any(AppUser.class))).willReturn(appUserTest);
        given(appUserUtil.overwriteAppUserCredentials(any(AppUser.class), any(AppUserCredentialsDTO.class))).willReturn(appUserTest);
        given(appUserUtil.mapToAppUserDTO(any(AppUser.class))).willReturn(appUserDTOTest);

        // when
        var user = appUserUserService.replaceUserCredentials(appUserCredentialsDTO);

        //then
        assertThat(user, instanceOf(AppUserDTO.class));
        assertThat(user, hasProperty("id", equalTo(1L)));
        assertThat(user, hasProperty("email", equalTo("testemail@wp.pl")));
        assertThat(user, hasProperty("firstName", equalTo("John")));
        assertThat(user, hasProperty("secondName", equalTo("Smith")));
        assertThat(user, hasProperty("username", equalTo("smith123")));
        assertEquals(1, user.getRoles().size());
        assertThat(user.getRoles().get(0), hasProperty("name", equalTo("TEST_ROLE")));
    }

    @Test
    @DisplayName("Should replace current user details.")
    void shouldReplaceCurrentLoggedUserDetails() {
        given(appUserAdminService.getLoggedInUserId()).willReturn(appUserTest.getId());
        given(appUserAdminService.getUserById(anyLong())).willReturn(appUserTest);
        given(appUserAdminService.saveUser(any(AppUser.class))).willReturn(appUserTest);
        given(appUserUtil.overwriteAppUserDetails(any(AppUser.class), any(ReplaceAppUserByUserDTO.class))).willReturn(appUserTest);
        given(appUserUtil.mapToAppUserDTO(any(AppUser.class))).willReturn(appUserDTOTest);

        // when
        var user = appUserUserService.replaceUserDetails(replaceAppUserByUserDTO);

        //then
        assertThat(user, instanceOf(AppUserDTO.class));
        assertThat(user, hasProperty("id", equalTo(1L)));
        assertThat(user, hasProperty("email", equalTo("testemail@wp.pl")));
        assertThat(user, hasProperty("firstName", equalTo("John")));
        assertThat(user, hasProperty("secondName", equalTo("Smith")));
        assertThat(user, hasProperty("username", equalTo("smith123")));
        assertEquals(1, user.getRoles().size());
        assertThat(user.getRoles().get(0), hasProperty("name", equalTo("TEST_ROLE")));
    }
}