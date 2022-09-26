package com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1")
class AppUserAdminController {

    private final AppUserAdminService appUserAdminService;

    @GetMapping("/admin/users")
    public ResponseEntity<List<AppUserDTO>> getAllUsers(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction,
            @RequestParam(defaultValue = "id") String by) {

        var users = appUserAdminService.getAllUsers(page, direction, by);

        return ResponseEntity.ok(users);
    }

    @GetMapping("/admin/users{userId:[0-9]+}")
    public ResponseEntity<AppUserDTO> getSingleUser(@PathVariable long userId) {

        var user = appUserAdminService.getSingleUser(userId);

        return ResponseEntity.ok(user);
    }

    @PostMapping("/admin/users")
    ResponseEntity<AppUserDTO> createAccount(@RequestBody CreateAppUserAdminDTO createAppUserAdminDTO) {

        var user = appUserAdminService.createAccount(createAppUserAdminDTO);

        return ResponseEntity.created(URI.create("/users/" + user.getId())).body(user);
    }

    @DeleteMapping("/admin/users/{userId:[0-9]+}")
    ResponseEntity<Void> deleteAllUsersAndAllRelated(@PathVariable long userId) {
        appUserAdminService.findAndDeleteUserWithRolesAndTransactions(userId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/admin/users/{userId:[0-9]+}")
    ResponseEntity<AppUserDTO> replaceUserAllDetails(
            @PathVariable long userId,
            @RequestBody ReplaceAppUserAllDetailsDTO replaceAppUserAllDetailsDTO) {

        var replacedUser = appUserAdminService.replaceUserAllDetails(userId, replaceAppUserAllDetailsDTO);

        return ResponseEntity.ok(replacedUser);
    }

}
