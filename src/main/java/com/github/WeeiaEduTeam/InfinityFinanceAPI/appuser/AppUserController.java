package com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto.AppUserCredentialsDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto.AppUserDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto.CreateAppUserDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto.ReplaceAppUserByUserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1")
public class AppUserController {

    private final AppUserService appUserService;

    @GetMapping("/users")
    public ResponseEntity<List<AppUserDTO>> getAllUsers() {

        var users = appUserService.getAllUsers();

        return ResponseEntity.ok(users);
    }

    @PostMapping("/users")
    ResponseEntity<AppUserDTO> createUser(@RequestBody CreateAppUserDTO createAppUserDTO) {

        var user = appUserService.createUser(createAppUserDTO);

        return ResponseEntity.created(URI.create("/users/" + user.getId())).body(user);
    }

    @DeleteMapping("/users/{userId:[0-9]+}")
    ResponseEntity<Void> deleteAllUsersAndAllRelated(@PathVariable long userId) {
        appUserService.findAndDeleteUserWithRolesAndTransactions(userId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/users/{userId:[0-9]+}/credentials")
    ResponseEntity<AppUserDTO> replaceUserCredentials(
            @PathVariable Long userId,
            @RequestBody AppUserCredentialsDTO appUserCredentialsDTO) {

        var replacedUser = appUserService.replaceUserCredentials(userId, appUserCredentialsDTO);

        return ResponseEntity.ok(replacedUser);
    }

    @PutMapping("/users/{userId:[0-9]+}/details")
    ResponseEntity<AppUserDTO> replaceUserDetails(
            @PathVariable Long userId,
            @RequestBody ReplaceAppUserByUserDTO replaceAppUserByUserDTO) {

        var replacedUser = appUserService.replaceUserDetails(userId, replaceAppUserByUserDTO);

        return ResponseEntity.ok(replacedUser);
    }
}
