package com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    @Query("SELECT u FROM AppUser u JOIN FETCH u.roles WHERE u.username = :username")
    AppUser findByUsername(String username);


    @Override
    @Query( value = "FROM AppUser u LEFT JOIN FETCH u.roles", /* avoid n + 1 */
            countQuery = "SELECT COUNT(u) FROM AppUser u LEFT JOIN u.roles") /* add pageable without n + 1 */
    @NotNull
    Page<AppUser> findAll(@NotNull Pageable pageable);

}
