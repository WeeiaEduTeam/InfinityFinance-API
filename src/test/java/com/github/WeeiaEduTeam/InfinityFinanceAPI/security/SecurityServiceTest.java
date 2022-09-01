package com.github.WeeiaEduTeam.InfinityFinanceAPI.security;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.AppUser;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.AppUserService;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.Role;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.security.jwt.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.github.WeeiaEduTeam.InfinityFinanceAPI.security.jwt.JwtUtil.TOKEN_PREFIX;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityServiceTest {

    @InjectMocks
    private SecurityService securityService;

    @Mock
    private AppUserService appUserService;
    @Mock
    private JwtUtil jwtUtil;

    AppUser userAdminTest;
    List<Role> roles;

    @BeforeEach
    void init() {
        roles = new ArrayList<>(Arrays.asList(
                Role.builder()
                        .name("ROLE_ADMIN")
                        .build(),
                Role.builder()
                        .name("ROLE_USER")
                        .build()
        ));

        userAdminTest = AppUser.builder()
                .username("adm")
                .email("admin@o2.pl")
                .password("{noop}adm")
                .firstName("Ok")
                .secondName("Oki")
                .roles(roles)
                .build();
    }

    /*@Test
    @DisplayName("Should refresh access token.")
    void shouldRefreshAccessToken() {
        // given
        String issuer = "issuer";

        String refreshToken = jwtUtil.generateRefreshToken(userAdminTest.getUsername(), issuer);
        String expectedAccessToken = jwtUtil.generateAccessToken(userAdminTest.getUsername(), new ArrayList<>(), issuer);

        when(jwtUtil.extractUsernameFromRefreshToken(anyString(), anyBoolean())).thenReturn(userAdminTest.getUsername());
        when(appUserService.getUserByUserName(anyString())).thenReturn(userAdminTest);
        when(jwtUtil.generateAccessToken(anyString(), anyList(), anyString())).thenReturn(expectedAccessToken);

        // when
        String accessToken = securityService.refreshAccessToken(refreshToken, issuer);

        // then
        assertThat(accessToken)
                .isNotEmpty()
                .isEqualTo(TOKEN_PREFIX + expectedAccessToken);
    }*/
}