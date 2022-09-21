package com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto.AppUserCredentialsDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto.AppUserDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto.CreateAppUserAdminDTO;
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
public class AppUserAdminController {

    private final AppUserAdminService appUserAdminService;

    @GetMapping("/admin/users")
    public ResponseEntity<List<AppUserDTO>> getAllUsers() {

        var users = appUserAdminService.getAllUsers();

        return ResponseEntity.ok(users);
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

    // admin zmienia calego uzytkownika przez createAppUserAdminDTO

}
