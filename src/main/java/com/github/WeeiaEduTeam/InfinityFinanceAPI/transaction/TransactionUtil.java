package com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto.CreateTransactionDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto.TransactionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
class TransactionUtil {

    private final TransactionMapperFactory transactionMapperFactory;

    public Transaction mapToTransaction(CreateTransactionDTO createTransactionDTO) {
        return transactionMapperFactory.mapToTransaction(createTransactionDTO);
    }

    public TransactionDTO mapToTransactionDTO(Transaction transaction) {
        return transactionMapperFactory.mapToTransactionDTO(transaction);
    }

    public Transaction overwriteTransactionByCreateTransactionDTO(Transaction main, CreateTransactionDTO toConvert) {
       var convertedTransaction = mapToTransaction(toConvert);

       main.setTransactionType(convertedTransaction.getTransactionType());
       main.setCategory(convertedTransaction.getCategory());
       main.setValue(convertedTransaction.getValue());
       main.setQuantity(convertedTransaction.getQuantity());

       return main;
    }
}
