package com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto.TransactionDTO;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public List<TransactionDTO> getAllTransactionsForGivenUserAndCategory(long userId, long categoryId) {
        var foundTransactions = transactionRepository.findAllByAppuserIdAndCategoryId(userId, categoryId);

        return foundTransactions.stream()
                .map(TransactionUtils::mapTransactionToTransactionDTO)
                .toList();
    }

    public List<TransactionDTO> getAllTransactionsForGivenUser(long userId) {
        var foundTransactions = transactionRepository.findAllByAppuserId(userId);

        return foundTransactions.stream()
                .map(TransactionUtils::mapTransactionToTransactionDTO)
                .toList();
    }


}
