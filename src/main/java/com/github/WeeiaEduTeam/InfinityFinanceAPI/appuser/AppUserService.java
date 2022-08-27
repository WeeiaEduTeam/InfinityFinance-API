package com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppUserService {

    private final AppUserRepository appUserRepository;

    public Optional<AppUser> getUserById(long userId) {
        return appUserRepository.findById(userId);
    }

    public Optional<AppUser> getUserByUserName(String userName) {
        return Optional.ofNullable(appUserRepository.findByUsername(userName));
    }
}
