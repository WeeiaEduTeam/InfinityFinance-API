package com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.exception.ResourceNotFoundException;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto.CreateTransactionDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto.TransactionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1")
class TransactionAdminController {

    private final TransactionAdminService transactionAdminService;

    @GetMapping("/admin/users/{userId:[0-9]+}/transactions/category/{categoryId:[0-9]+}")
    public ResponseEntity<List<TransactionDTO>> getAllTransactionsForGivenUserAndCategory(
            @PathVariable long userId, @PathVariable long categoryId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction,
            @RequestParam(defaultValue = "id") String by){

        var transactions = transactionAdminService.getAllTransactionsForGivenUserAndCategory(
                userId, categoryId, page, direction, by);

        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/admin/users/{userId:[0-9]+}/transactions")
    public ResponseEntity<List<TransactionDTO>> getAllTransactionsForGivenUser(
            @PathVariable long userId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction,
            @RequestParam(defaultValue = "id") String by) {

        var transactions = transactionAdminService.getAllTransactionsForGivenUser(userId, page, direction, by);

        return ResponseEntity.ok(transactions);
    }

    @PostMapping("/admin/users/{userId:[0-9]+}/transactions")
    ResponseEntity<TransactionDTO> createTransactionForGivenUser(@PathVariable long userId, @RequestBody @Valid CreateTransactionDTO createTransactionDTO){
        var transaction = transactionAdminService.createTransactionForGivenUser(userId, createTransactionDTO);

        return ResponseEntity.created(URI.create("/users/" + userId + "/transactions")).body(transaction);
    }

    @PutMapping("/admin/users/{userId:[0-9]+}/transactions/{transactionId:[0-9]+}")
    ResponseEntity<TransactionDTO> replaceTransactionForGivenUserIdAndTransactionId(
            @PathVariable Long userId, @PathVariable Long transactionId,
            @RequestBody CreateTransactionDTO createTransactionDTO) {

        var replacedTransaction = transactionAdminService.replaceTransaction(userId, transactionId, createTransactionDTO);

        return ResponseEntity.ok(replacedTransaction);
    }

    @DeleteMapping("/admin/users/transactions/{transactionId:[0-9]+}")
    ResponseEntity<Void> deleteSingleTransaction(@PathVariable long transactionId) {
        transactionAdminService.deleteOneTransaction(transactionId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
