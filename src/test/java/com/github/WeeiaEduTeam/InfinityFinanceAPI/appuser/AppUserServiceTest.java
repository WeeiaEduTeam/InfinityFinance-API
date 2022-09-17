package com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto.AppUserCredentialsDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto.AppUserDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto.CreateAppUserDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto.ReplaceAppUserByUserDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.Role;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.RoleService;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.role.RoleDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.TransactionAdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AppUserServiceTest {

    @InjectMocks
    private AppUserService appUserService;

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private AppUserUtil appUserUtil;
    @Mock
    private RoleService roleService;
    @Mock
    private TransactionAdminService transactionAdminService;

    private AppUser appUserTest;
    private AppUserDTO appUserDTOTest;
    private Role roleTest;
    private RoleDTO roleDTOTest;
    private CreateAppUserDTO createAppUserDTOTest;
    private AppUserCredentialsDTO appUserCredentialsDTO;
    private ReplaceAppUserByUserDTO replaceAppUserByUserDTO;

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

        createAppUserDTOTest = CreateAppUserDTO.builder()
                .email("example@wp.pl")
                .username("example")
                .password("{noop}example")
                .build();

        appUserCredentialsDTO = AppUserCredentialsDTO.builder()
                .password("{noop}example")
                .username("example")
                .build();

        replaceAppUserByUserDTO = ReplaceAppUserByUserDTO.builder()
                .email("example@wp.pl")
                .firstName("example")
                .secondName("example")
                .build();
    }

    @Test
    @DisplayName("Should throw UsernameNotFoundException when logged in user does not exist")
    void shouldThrowExceptionWhenLoggedInUserDoesNotExist() {
        //given
        given(appUserRepository.getIdByUsername(anyString())).willReturn(null);

        // when
        assertThatThrownBy(() -> appUserService.getLoggedInUserId())
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found in the database");
    }

    @Test
    @DisplayName("Should get all users")
    void shouldGetAllUsers() {
        //given
        given(appUserRepository.findAll()).willReturn(List.of(appUserTest));
        given(appUserUtil.mapToAppUserDTO(Mockito.any(AppUser.class))).willReturn(appUserDTOTest);

        // when
        var users = appUserService.getAllUsers();

        //then
        assertEquals(1, users.size());

        var firstUser = users.get(0);
        assertThat(firstUser, instanceOf(AppUserDTO.class));
        assertThat(firstUser, hasProperty("id", equalTo(1L)));
        assertThat(firstUser, hasProperty("email", equalTo("testemail@wp.pl")));
        assertThat(firstUser, hasProperty("firstName", equalTo("John")));
        assertThat(firstUser, hasProperty("secondName", equalTo("Smith")));
        assertThat(firstUser, hasProperty("username", equalTo("smith123")));
        assertEquals(1, firstUser.getRoles().size());
        assertThat(firstUser.getRoles().get(0), hasProperty("name", equalTo("TEST_ROLE")));
    }

    @Test
    @DisplayName("Should create user")
    void shouldCreateUser() {
        //given
        given(appUserUtil.mapToAppUserFactory(Mockito.any(CreateAppUserDTO.class))).willReturn(appUserTest);
        given(roleService.getUserRoleOrCreate()).willReturn(roleTest);
        given(appUserRepository.save(Mockito.any(AppUser.class))).willReturn(appUserTest);
        given(appUserUtil.mapToAppUserDTO(Mockito.any(AppUser.class))).willReturn(appUserDTOTest);


        // when
        var user = appUserService.createUser(createAppUserDTOTest);

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
    @DisplayName("Should delete user and all relations")
    void shouldDeleteUser() {
        //given
        given(appUserRepository.findById(anyLong())).willReturn(Optional.ofNullable(appUserTest));

        // when
        appUserService.findAndDeleteUserWithRolesAndTransactions(1L);

        //then
        verify(transactionAdminService).deleteTransactionsRelatedWithUser(1L);
        verify(roleService).deleteRoleFromUser(appUserTest);
        verify(appUserRepository).delete(appUserTest);
    }


    @Test
    @DisplayName("Should replace user credentials")
    void shouldReplaceUserCredentials() {
        //given
        given(appUserRepository.findById(anyLong())).willReturn(Optional.ofNullable(appUserTest));
        given(appUserUtil.overwriteAppUserCredentials(Mockito.any(AppUser.class), Mockito.any(AppUserCredentialsDTO.class))).willReturn(appUserTest);
        given(appUserRepository.save(Mockito.any(AppUser.class))).willReturn(appUserTest);
        given(appUserUtil.mapToAppUserDTO(Mockito.any(AppUser.class))).willReturn(appUserDTOTest);

        // when
        var user = appUserService.replaceUserCredentials(1L, appUserCredentialsDTO);

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
    @DisplayName("Should replace user details")
    void shouldReplaceUserDetails() {
        //given
        given(appUserRepository.findById(anyLong())).willReturn(Optional.ofNullable(appUserTest));
        given(appUserUtil.overwriteAppUserDetails(Mockito.any(AppUser.class), Mockito.any(ReplaceAppUserByUserDTO.class))).willReturn(appUserTest);
        given(appUserRepository.save(Mockito.any(AppUser.class))).willReturn(appUserTest);
        given(appUserUtil.mapToAppUserDTO(Mockito.any(AppUser.class))).willReturn(appUserDTOTest);

        // when
        var user = appUserService.replaceUserDetails(1L, replaceAppUserByUserDTO);

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