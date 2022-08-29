package com.github.WeeiaEduTeam.InfinityFinanceAPI.security;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.AppUser;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SecurityService {
    private final AppUserService appUserService;

    public String refreshAccessToken(String refreshToken, String issuer) {
        return "aha";
    }

}