package com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto.TransactionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1")
public class TransactionController {

    private final TransactionService transactionService;

    /*
        TODO: pageable
        req param income, outcome
     */
    @GetMapping("/users/{userId}/transactions/category/{categoryId}")
    public ResponseEntity<List<TransactionDTO>> getAllTransactionsForGivenUserAndCategory(@PathVariable long userId, @PathVariable long categoryId) {
        var transactions = transactionService.getAllTransactionsForGivenUserAndCategory(userId, categoryId);

        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/users/{userId}/transactions")
    public ResponseEntity<List<TransactionDTO>> getAllTransactionsForGivenUserAndCategory(@PathVariable long userId) {
        var transactions = transactionService.getAllTransactionsForGivenUser(userId);

        return ResponseEntity.ok(transactions);
    }

}
