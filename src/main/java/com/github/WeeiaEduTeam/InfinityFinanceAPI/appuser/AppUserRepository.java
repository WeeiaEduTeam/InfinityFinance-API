package com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    default Optional<Long> getLoggedInUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!(auth instanceof AnonymousAuthenticationToken)) {
            return getIdByUsername(auth.getName());
        }

        return Optional.empty();
    }

    @Query("SELECT u.id FROM AppUser u WHERE u.username = :username")
    Optional<Long> getIdByUsername(String username);

    AppUser findByUsername(String username);

    Boolean existsByEmail(String email);
}
