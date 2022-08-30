package com.github.WeeiaEduTeam.InfinityFinanceAPI.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    public static final String TOKEN_PREFIX = "Bearer ";

    private final long ACCESS_TOKEN_EXP_TIME;
    private final long REFRESH_TOKEN_EXP_TIME;
    private final Algorithm ALGORITHM;

    public JwtUtil(
            @Value("${jwt.access-token-expiration-time}") long accessTokenExpTime,
            @Value("${jwt.refresh-token-expiration-time}") long refreshTokenExpTime,
            @Value("${jwt.secret}") String secret) {
        ACCESS_TOKEN_EXP_TIME = accessTokenExpTime;
        REFRESH_TOKEN_EXP_TIME = refreshTokenExpTime;
        ALGORITHM = Algorithm.HMAC256(secret.getBytes());
    }


    public <R> String generateAccessToken(String username, List<String> authorities, String issuer) {
        //validateAuthorities(authorities);
        //validateUsername(username);

        JWTCreator.Builder jwtBuilder = createJwtBuilderWithAuthorities(username, authorities);

        jwtBuilder.withIssuer(issuer);

        return jwtBuilder.sign(ALGORITHM);
    }

    private JWTCreator.Builder createJwtBuilderWithAuthorities(String username, List<String> authorities) {
        return createJwtBuilder(username).withClaim("roles", authorities);
    }

    private JWTCreator.Builder createJwtBuilder(String username) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXP_TIME));
    }

    public String generateRefreshToken(String username, String issuer) {
        JWTCreator.Builder jwtBuilder = createJwtBuilder(username);

        jwtBuilder.withIssuer(issuer);

        return jwtBuilder.sign(ALGORITHM);
    }

    public String extractUsernameFromRefreshToken(String refreshToken, boolean isBearer) {
        //validateRefreshToken(refreshToken, isBearer);
        return getSubjectFromRefreshToken(refreshToken);
    }

    private String getSubjectFromRefreshToken(String refreshToken) {
        return JWT.require(ALGORITHM)
                .build()
                .verify(refreshToken.replace(TOKEN_PREFIX, ""))
                .getSubject();
    }
}
