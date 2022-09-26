package com.github.WeeiaEduTeam.InfinityFinanceAPI.security;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.AppUser;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.AppUserAdminService;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.Role;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.github.WeeiaEduTeam.InfinityFinanceAPI.security.jwt.JwtUtil.TOKEN_PREFIX;

@RequiredArgsConstructor
@Service
@Slf4j
class SecurityService {
    private final AppUserAdminService appUserAdminService;
    private final JwtUtil jwtUtil;

    public String refreshAccessToken(String refreshToken, String issuer) {

        String username = jwtUtil.extractUsernameFromRefreshToken(refreshToken, true);

        AppUser user = appUserAdminService.getUserByUserName(username);

        var roleNames = user.getRoles().stream().map(Role::getName).toList();

        String accessToken = jwtUtil.generateAccessToken(
                username,
                roleNames,
                issuer);

        return TOKEN_PREFIX + accessToken;
    }
}