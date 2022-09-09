package com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppUserService implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    public AppUser getUserById(long userId) {

        return appUserRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found in the database"));
    }

    public AppUser getUserByUserName(String username) {
        return appUserRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUserByUserName(username);
    }

    public Long getLoggedInUserId() {
        return appUserRepository.getLoggedInUserId().orElseThrow(() -> new UsernameNotFoundException("User not found in the database"));
    }


}
