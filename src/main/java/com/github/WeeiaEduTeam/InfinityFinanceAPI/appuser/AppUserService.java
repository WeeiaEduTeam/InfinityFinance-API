package com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppUserService {

    private final AppUserRepository appUserRepository;

    public Optional<AppUser> getUserById(long userId) {

        return appUserRepository.findById(userId);
    }

    public Optional<AppUser> getUserByUserName(String userName) {

        return Optional.ofNullable(appUserRepository.findByUsername(userName));
    }
}
