package com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto.CreateTransactionDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto.TransactionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1")
public class TransactionUserController {

    private final TransactionUserService transactionUserService;

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/users/transactions")
    public ResponseEntity<List<TransactionDTO>> getAllTransactionsForLoggedUser() {
        var transactions = transactionUserService.getAllTransactionsForLoggedUser();

        return ResponseEntity.ok(transactions);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/users/transactions/category/{categoryId:[0-9]+}")
    public ResponseEntity<List<TransactionDTO>> getAllTransactionsForLoggedUserAndGivenCategory(@PathVariable long categoryId) {
        var transactions = transactionUserService.getAllTransactionsForLoggedUserAndGivenCategory(categoryId);

        return ResponseEntity.ok(transactions);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @DeleteMapping("/users/transactions/{transactionId:[0-9]+}")
    ResponseEntity<Void> deleteSingleTransactionForLoggedUser(@PathVariable long transactionId) {
        transactionUserService.deleteSingleTransactionForLoggedUser(transactionId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/users/transactions")
    ResponseEntity<TransactionDTO> createTransactionForLoggedUser( @RequestBody CreateTransactionDTO createTransactionDTO) {
        var transaction = transactionUserService.createTransactionForLoggedUser(createTransactionDTO);

        return ResponseEntity.created(URI.create("/users/transactions" + transaction.getId())).body(transaction);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PutMapping("/users/transactions/{transactionId:[0-9]+}")
    ResponseEntity<TransactionDTO> replaceUniversityWithLocationForLoggedUser(
            @PathVariable Long transactionId,
            @RequestBody CreateTransactionDTO createTransactionDTO) {

        var replacedTransaction = transactionUserService.replaceTransactionForLoggedUser(transactionId, createTransactionDTO);

        return ResponseEntity.ok(replacedTransaction);
    }
}
