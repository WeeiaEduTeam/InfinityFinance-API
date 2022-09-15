package com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto.AppUserDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto.CreateAppUserDTO;
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
    ResponseEntity<AppUserDTO> createTransactionForGivenUser(@RequestBody CreateAppUserDTO createAppUserDTO) {

        var user = appUserService.createUser(createAppUserDTO);

        return ResponseEntity.created(URI.create("/users/" + user.getId())).body(user);
    }

    @DeleteMapping("/users/{userId:[0-9]+}")
    ResponseEntity<Void> deleteSingleTransaction(@PathVariable long userId) {
        appUserService.findAndDeleteUserWithRoles(userId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
