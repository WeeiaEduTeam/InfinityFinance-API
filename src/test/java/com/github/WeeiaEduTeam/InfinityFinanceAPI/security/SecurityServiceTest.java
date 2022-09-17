package com.github.WeeiaEduTeam.InfinityFinanceAPI.security;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.AppUser;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.AppUserService;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.Role;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.RoleService;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.security.jwt.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.github.WeeiaEduTeam.InfinityFinanceAPI.security.jwt.JwtUtil.TOKEN_PREFIX;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class SecurityServiceTest {

    @InjectMocks
    private SecurityService securityService;

    @Mock
    private AppUserService appUserService;
    @Mock
    private JwtUtil jwtUtil;

    AppUser userAdminTest;
    Role roleTest;

    @BeforeEach
    void init() {
        roleTest = Role.builder()
                    .name("ROLE_ADMIN")
                    .build();

        userAdminTest = AppUser.builder()
                .username("adm")
                .email("admin@o2.pl")
                .password("{noop}adm")
                .firstName("Ok")
                .secondName("Oki")
                .roles(List.of(roleTest))
                .build();
    }

    @Test
    @DisplayName("Should refresh access token.")
    void shouldRefreshAccessToken() {
        // given
        String issuer = "issuer";
        String refreshToken = "refresh";
        String expectedToken = "expected";

        given(jwtUtil.extractUsernameFromRefreshToken(anyString(), anyBoolean())).willReturn(userAdminTest.getUsername());

        given(appUserService.getUserByUserName(anyString())).willReturn(userAdminTest);
        given(jwtUtil.generateAccessToken(anyString(), anyList(), anyString())).willReturn(expectedToken);

        // when
        String accessToken = securityService.refreshAccessToken(refreshToken, issuer);

        // then
        assertThat(accessToken)
                .isNotEmpty()
                .isEqualTo(TOKEN_PREFIX + expectedToken);
    }
}