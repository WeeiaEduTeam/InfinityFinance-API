package com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto.*;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.rolestrategy.AppUserRoleStrategyFacade;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.RoleService;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.util.CustomPageable;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.TransactionAdminService;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

class AppUserAdminServiceTest extends AppUserTestHelper {

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
    @Mock
    private CustomPageable customPageable;

    @Test
    @DisplayName("Should get all users")
    void shouldGetAllUsers() {
        //given
        final Page<AppUser> page = new PageImpl<>(List.of(appUserTest));
        given(appUserRepository.findAll(any(Pageable.class))).willReturn(page);
        given(appUserUtil.mapToAppUserDTO(Mockito.any(AppUser.class))).willReturn(appUserDTOTest);
        given(customPageable.validateAndCreatePageable(anyInt(), any(Sort.Direction.class), anyString(), ArgumentMatchers.<Class<A>>any())).willReturn(PageRequest.of(1,1));

        // when
        var users = appUserAdminService.getAllUsers(1, Sort.Direction.ASC, "id");

        //then
        assertEquals(1, users.size());

        var firstUser = users.get(0);
        assertThat(firstUser, instanceOf(AppUserDTO.class));
        assertThat(firstUser, hasProperty("id", equalTo(TEST_ID)));
        assertThat(firstUser, hasProperty("email", equalTo(TEST_EMAIL)));
        assertThat(firstUser, hasProperty("firstName", equalTo(TEST_FIRSTNAME)));
        assertThat(firstUser, hasProperty("secondName", equalTo(TEST_SECONDNAME)));
        assertThat(firstUser, hasProperty("username", equalTo(TEST_USERNAME)));
        assertEquals(1, firstUser.getRoles().size());
        assertThat(firstUser.getRoles().get(0), hasProperty("name", equalTo(TEST_ROLENAME)));
    }

    @Test
    @DisplayName("Should get single user")
    void shouldGetSingleUser() {
        //given
        given(appUserRepository.findById(anyLong())).willReturn(Optional.ofNullable(appUserTest));
        given(appUserUtil.mapToAppUserDTO(Mockito.any(AppUser.class))).willReturn(appUserDTOTest);

        // when
        var user = appUserAdminService.getSingleUser(appUserTest.getId());

        //then
        assertThat(user, instanceOf(AppUserDTO.class));
        assertThat(user, hasProperty("id", equalTo(TEST_ID)));
        assertThat(user, hasProperty("email", equalTo(TEST_EMAIL)));
        assertThat(user, hasProperty("firstName", equalTo(TEST_FIRSTNAME)));
        assertThat(user, hasProperty("secondName", equalTo(TEST_SECONDNAME)));
        assertThat(user, hasProperty("username", equalTo(TEST_USERNAME)));
        assertEquals(1, user.getRoles().size());
        assertThat(user.getRoles().get(0), hasProperty("name", equalTo(TEST_ROLENAME)));
    }

    @Test
    @DisplayName("Should create user")
    void shouldCreateUser() {
        //given
        given(appUserUtil.mapToAppUserFactory(ArgumentMatchers.any())).willReturn(appUserTest);
        given(roleService.getUserRoleOrCreate()).willReturn(roleTest);
        given(appUserRepository.save(Mockito.any(AppUser.class))).willReturn(appUserTest);
        given(appUserUtil.mapToAppUserDTO(Mockito.any(AppUser.class))).willReturn(appUserDTOTest);

        // when
        var user = appUserAdminService.createAccount(createAppUserAdminDTOTest);

        //then
        assertThat(user, instanceOf(AppUserDTO.class));
        assertThat(user, hasProperty("id", equalTo(TEST_ID)));
        assertThat(user, hasProperty("email", equalTo(TEST_EMAIL)));
        assertThat(user, hasProperty("firstName", equalTo(TEST_FIRSTNAME)));
        assertThat(user, hasProperty("secondName", equalTo(TEST_SECONDNAME)));
        assertThat(user, hasProperty("username", equalTo(TEST_USERNAME)));
        assertEquals(1, user.getRoles().size());
        assertThat(user.getRoles().get(0), hasProperty("name", equalTo(TEST_ROLENAME)));
    }

    @Test
    @DisplayName("Should delete user and all relations")
    void shouldDeleteUser() {
        //given
        given(appUserRepository.findById(anyLong())).willReturn(Optional.ofNullable(appUserTest));

        // when
        appUserAdminService.findAndDeleteUserWithRolesAndTransactions(TEST_ID);

        //then
        verify(transactionAdminService).deleteTransactionsRelatedWithUser(TEST_ID);
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
        var user = appUserAdminService.replaceUserAllDetails(TEST_ID, replaceAppUserAllDetailsDTO);

        //then
        assertThat(user, instanceOf(AppUserDTO.class));
        assertThat(user, hasProperty("id", equalTo(TEST_ID)));
        assertThat(user, hasProperty("email", equalTo(TEST_EMAIL)));
        assertThat(user, hasProperty("firstName", equalTo(TEST_FIRSTNAME)));
        assertThat(user, hasProperty("secondName", equalTo(TEST_SECONDNAME)));
        assertThat(user, hasProperty("username", equalTo(TEST_USERNAME)));
        assertEquals(1, user.getRoles().size());
        assertThat(user.getRoles().get(0), hasProperty("name", equalTo(TEST_ROLENAME)));
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
        assertThatThrownBy(() -> appUserAdminService.getUserById(TEST_ID))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found in the database");

    }

}