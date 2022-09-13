package com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto.AppUserDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.dto.CreateAppUserDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto.CreateTransactionDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto.TransactionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
}
