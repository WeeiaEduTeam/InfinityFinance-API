package com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto.CreateTransactionDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto.TransactionDTO;
import org.springframework.stereotype.Component;

@Component
class TransactionMapper {

    public Transaction mapToTransaction(CreateTransactionDTO createTransactionDTO) {
        return Transaction.builder()
                .transactionType(createTransactionDTO.getTransactionType())
                .value(createTransactionDTO.getValue())
                .quantity(createTransactionDTO.getQuantity())
                .title(createTransactionDTO.getTitle())
                .description(createTransactionDTO.getDescription())
                .build();
    }

    public TransactionDTO mapToTransactionDTO(Transaction transaction) {

        return TransactionDTO.builder()
                .id(transaction.getId())
                .categoryName(transaction.getCategory().getName())
                .transactionType(transaction.getTransactionType())
                .value(transaction.getValue())
                .quantity(transaction.getQuantity())
                .title(transaction.getTitle())
                .description(transaction.getDescription())
                .username(transaction.getAppuser().getUsername())
                .build();
    }

}
