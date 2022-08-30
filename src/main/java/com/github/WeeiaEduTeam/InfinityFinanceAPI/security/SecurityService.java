package com.github.WeeiaEduTeam.InfinityFinanceAPI.security;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.AppUser;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.AppUserService;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.role.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.github.WeeiaEduTeam.InfinityFinanceAPI.security.JwtUtil.TOKEN_PREFIX;

@RequiredArgsConstructor
@Service
public class SecurityService {
    private final AppUserService appUserService;
    private final JwtUtil jwtUtil;

    public String refreshAccessToken(String refreshToken, String issuer) {
        String username = jwtUtil.extractUsernameFromRefreshToken(refreshToken, true);

        AppUser user = appUserService.getUserByUserName(username);
        String accessToken = jwtUtil.generateAccessToken(
                username,
                user.getRoles().stream().map(Role::getName).toList(),
                issuer);

        return TOKEN_PREFIX + accessToken;
    }
}