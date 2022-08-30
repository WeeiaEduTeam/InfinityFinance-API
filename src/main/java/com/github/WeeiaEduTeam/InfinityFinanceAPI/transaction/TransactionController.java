package com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto.CreateTransactionDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto.TransactionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/secured")
    public String temporary() {
        return "test";
    }

    @PostMapping("/admin/users/{userId}/transactions")
    ResponseEntity<TransactionDTO> createTransactionForGivenUser(@PathVariable long userId, @RequestBody CreateTransactionDTO createTransactionDTO) {
        var transaction = transactionService.createTransactionForGivenUser(userId, createTransactionDTO);

        return ResponseEntity.created(URI.create("/users/" + userId + "/transactions")).body(transaction);
    }

    /*
        TODO: pageable
        req param income, outcome
        req param category name instead of id?
     */
    @GetMapping("/admin/user/{userId}/transactions/category/{categoryId}")
    public ResponseEntity<List<TransactionDTO>> getAllTransactionsForGivenUserAndCategory(@PathVariable long userId, @PathVariable long categoryId) {
        var transactions = transactionService.getAllTransactionsForGivenUserAndCategory(userId, categoryId);

        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/admin/users/{userId}/transactions")
    public ResponseEntity<List<TransactionDTO>> getAllTransactionsForGivenUserAndCategory(@PathVariable long userId) {
        var transactions = transactionService.getAllTransactionsForGivenUser(userId);

        return ResponseEntity.ok(transactions);
    }

}
