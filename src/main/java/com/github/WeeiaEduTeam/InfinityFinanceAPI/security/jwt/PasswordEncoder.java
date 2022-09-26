package com.github.WeeiaEduTeam.InfinityFinanceAPI.security.jwt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

@Configuration
class PasswordEncoder {
    @Bean
    public org.springframework.security.crypto.password.PasswordEncoder bCryptPasswordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}