package com.github.WeeiaEduTeam.InfinityFinanceAPI.security;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.WeeiaEduTeam.InfinityFinanceAPI.security.JwtUtil.TOKEN_PREFIX;

@Component
@RequiredArgsConstructor
public class RestAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        AppUser user = (AppUser) authentication.getPrincipal();

        String accessToken = jwtUtil.generateAccessToken(
                user.getUsername(),
                authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList(),
                request.getRequestURL().toString());

        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername(), request.getRequestURL().toString());

        setResponseHeaders(response, TOKEN_PREFIX + accessToken, TOKEN_PREFIX + refreshToken);
    }

    private void setResponseHeaders(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setHeader("access_token", accessToken);
        response.setHeader("refresh_token", refreshToken);
    }
}
