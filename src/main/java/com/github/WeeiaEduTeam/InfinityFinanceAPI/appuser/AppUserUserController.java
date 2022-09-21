package com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

/*
 * If you hit the
 * - DELETE "/users" endpoint
 * - PUT "/users/credentials" endpoint
 *  make sure that you logout
 *  current user to ensure getting
 *  troubleshoot errors.
 */

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1")
public class AppUserUserController {

    private final AppUserUserService appUserUserService;

    @GetMapping("/users")
    public ResponseEntity<AppUserDTO> getCurrentLoggedUserInformation() {

        var user = appUserUserService.getCurrentLoggedUserInformation();

        return ResponseEntity.ok(user);
    }

    @PostMapping("/users")
    ResponseEntity<AppUserDTO> createAccount(@RequestBody CreateAppUserUserDTO createAppUserUserDTO) {

        var user = appUserUserService.createAccount(createAppUserUserDTO);

        return ResponseEntity.created(URI.create("/users/" + user.getId())).body(user);
    }

    @DeleteMapping("/users")
    ResponseEntity<Void> deleteCurrentLoggedUser() {
        appUserUserService.deleteCurrentLoggedUser();

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/users/credentials")
    ResponseEntity<AppUserDTO> replaceUserCredentials(
            @RequestBody AppUserCredentialsDTO appUserCredentialsDTO) {

        var replacedUser = appUserUserService.replaceUserCredentials(appUserCredentialsDTO);

        return ResponseEntity.ok(replacedUser);
    }

    @PutMapping("/users/details")
    ResponseEntity<AppUserDTO> replaceUserDetails(
            @RequestBody ReplaceAppUserByUserDTO replaceAppUserByUserDTO) {

        var replacedUser = appUserUserService.replaceUserDetails(replaceAppUserByUserDTO);

        return ResponseEntity.ok(replacedUser);
    }

}
