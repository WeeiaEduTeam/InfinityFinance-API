package com.github.WeeiaEduTeam.InfinityFinanceAPI.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto.AppUserCredentialsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@RequiredArgsConstructor
public class JsonObjectAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            return authenticateUser(request);
        } catch (IOException err) {
            throw new IllegalArgumentException(err.getMessage());
        }
    }

    private Authentication authenticateUser(HttpServletRequest request) throws IOException {
        AppUserCredentialsDTO appUserCredentialsDTO = extractCredentialsFromRequest(request.getReader());

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                appUserCredentialsDTO.getUsername(), appUserCredentialsDTO.getPassword()
        );

        setDetails(request, token);

        return this.getAuthenticationManager().authenticate(token);
    }

    private AppUserCredentialsDTO extractCredentialsFromRequest(BufferedReader bufferedReader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }

        return objectMapper.readValue(stringBuilder.toString(), AppUserCredentialsDTO.class);
    }
}
