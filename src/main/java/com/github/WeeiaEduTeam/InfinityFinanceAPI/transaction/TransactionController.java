package com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto.CreateTransactionDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto.TransactionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/secured/justAuth")
    public String justAuthenticated() {
        return "auth";
    }

    @GetMapping("/admin/needAdminRole")
    public String justAdmin() {
        return "role_admin";
    }

    /*
        TODO: pageable
        req param income, outcome
        req param category name instead of id?
     */
    @GetMapping("/admin/users/{userId:[0-9]+}/transactions/category/{categoryId:[0-9]+}")
    public ResponseEntity<List<TransactionDTO>> getAllTransactionsForGivenUserAndCategory(@PathVariable long userId, @PathVariable long categoryId) {
        var transactions = transactionService.getAllTransactionsForGivenUserAndCategory(userId, categoryId);

        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/admin/users/{userId:[0-9]+}/transactions")
    public ResponseEntity<List<TransactionDTO>> getAllTransactionsForGivenUserAndCategory(@PathVariable long userId) {
        var transactions = transactionService.getAllTransactionsForGivenUser(userId);

        return ResponseEntity.ok(transactions);
    }

    @PostMapping("/admin/users/{userId:[0-9]+}/transactions")
    ResponseEntity<TransactionDTO> createTransactionForGivenUser(@PathVariable long userId, @RequestBody CreateTransactionDTO createTransactionDTO) {
        var transaction = transactionService.createTransactionForGivenUser(userId, createTransactionDTO);

        return ResponseEntity.created(URI.create("/users/" + userId + "/transactions")).body(transaction);
    }



    @PutMapping("/admin/users/{userId:[0-9]+}/transactions/{transactionId:[0-9]+}")
    ResponseEntity<TransactionDTO> replaceUniversityWithLocation(
            @PathVariable Long userId, @PathVariable Long transactionId,
            @RequestBody CreateTransactionDTO createTransactionDTO) {

        var replacedTransaction = transactionService.replaceTransaction(userId, transactionId, createTransactionDTO);

        return ResponseEntity.ok(replacedTransaction);
    }

    @DeleteMapping("/admin/users/transactions/{transactionId:[0-9]+}")
    ResponseEntity<Void> deleteSingleTransaction(@PathVariable long transactionId) {
        transactionService.deleteOneTransaction(transactionId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
