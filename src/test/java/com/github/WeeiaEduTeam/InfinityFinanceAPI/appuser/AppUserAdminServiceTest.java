package com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto.*;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.strategy.AppUserRoleStrategyFacade;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.Role;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.RoleService;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.dto.RoleDTO;
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
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AppUserAdminServiceTest {

    @InjectMocks
    private AppUserAdminService appUserAdminService;

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private AppUserUtil appUserUtil;
    @Mock
    private RoleService roleService;
    @Mock
    private TransactionAdminService transactionAdminService;
    @Mock
    private AppUserRoleStrategyFacade appUserRoleStrategyFacade;

    private AppUser appUserTest;
    private AppUserDTO appUserDTOTest;
    private Role roleTest;
    private RoleDTO roleDTOTest;
    private CreateAppUserAdminDTO createAppUserAdminDTOTest;
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

        createAppUserAdminDTOTest = new CreateAppUserAdminDTO();
        createAppUserAdminDTOTest.setPassword("{noop}example");
        createAppUserAdminDTOTest.setEmail("example@wp.pl");
        createAppUserAdminDTOTest.setUsername("example");

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
    @DisplayName("Should get all users")
    void shouldGetAllUsers() {
        //given
        given(appUserRepository.findAll()).willReturn(List.of(appUserTest));
        given(appUserUtil.mapToAppUserDTO(Mockito.any(AppUser.class))).willReturn(appUserDTOTest);

        // when
        var users = appUserAdminService.getAllUsers();

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
        given(appUserUtil.mapToAppUserFactory(Mockito.any(CreateAppUserAdminDTO.class))).willReturn(appUserTest);
        given(roleService.getUserRoleOrCreate()).willReturn(roleTest);
        given(appUserRepository.save(Mockito.any(AppUser.class))).willReturn(appUserTest);
        given(appUserUtil.mapToAppUserDTO(Mockito.any(AppUser.class))).willReturn(appUserDTOTest);

        // when
        var user = appUserAdminService.createAccount(createAppUserAdminDTOTest);

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
        appUserAdminService.findAndDeleteUserWithRolesAndTransactions(1L);

        //then
        verify(transactionAdminService).deleteTransactionsRelatedWithUser(1L);
        verify(appUserRepository).delete(appUserTest);
        verify(appUserRoleStrategyFacade).removeRoles(appUserTest);
    }


    @Test
    @DisplayName("Should replace user all details")
    void shouldReplaceAllUserDetails() {
        //given
        given(appUserRepository.findById(anyLong())).willReturn(Optional.ofNullable(appUserTest));
        given(appUserUtil.overwriteAppUserAllDetails(Mockito.any(AppUser.class), Mockito.any(ReplaceAppUserAllDetailsDTO.class))).willReturn(appUserTest);
        given(appUserRepository.save(Mockito.any(AppUser.class))).willReturn(appUserTest);
        given(appUserUtil.mapToAppUserDTO(Mockito.any(AppUser.class))).willReturn(appUserDTOTest);

        // when
        var user = appUserAdminService.replaceUserAllDetails(1L, replaceAppUserAllDetailsDTO);

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
    @DisplayName("Should throw when logged in user does not exist")
    void shouldThrowExceptionWhenLoggedInUserDoesNotExist() {
        //given
        given(appUserRepository.getIdByUsername(anyString())).willReturn(null);

        // when
        assertThatThrownBy(() -> appUserAdminService.getLoggedInUserId())
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found in the database");
    }

    @Test
    @DisplayName("Should throw when user does not exist in db")
    void shouldThrowWhenUserDoesNotExist() {
        //given
        given(appUserRepository.findById(anyLong())).willReturn(Optional.ofNullable(null));

        // when
        assertThatThrownBy(() -> appUserAdminService.getUserById(1L))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found in the database");

    }

}