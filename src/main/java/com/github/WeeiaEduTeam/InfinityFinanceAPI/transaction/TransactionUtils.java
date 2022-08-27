package com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto.TransactionDTO;

public class TransactionUtils {
    static TransactionDTO mapTransactionToTransactionDTO(Transaction transaction) {
        return TransactionDTO.builder()
                .categoryName(transaction.getCategory().getName())
                .transactionType(transaction.getTransactionType())
                .value(transaction.getValue())
                .quantity(transaction.getQuantity())
                .userName(transaction.getAppuser().getUsername())
                .build();
    }
}
