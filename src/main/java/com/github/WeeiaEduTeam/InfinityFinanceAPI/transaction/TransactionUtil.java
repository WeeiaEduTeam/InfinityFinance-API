package com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto.CreateTransactionDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto.TransactionDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.util.BaseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class TransactionUtil extends BaseUtil {

    public Transaction mapCreateTransactionDTOToTransaction(CreateTransactionDTO createTransactionDTO, long userId) {
        return Transaction.builder()
                .transactionType(createTransactionDTO.getTransactionType())
                .value(createTransactionDTO.getValue())
                .quantity(createTransactionDTO.getQuantity())
                .title(createTransactionDTO.getTitle())
                .description(createTransactionDTO.getDescription())
                .build();
    }

    public TransactionDTO mapTransactionToTransactionDTO(Transaction transaction) {
        String category = transaction.getCategory().getName();

        return TransactionDTO.builder()
                .id(transaction.getId())
                .categoryName(category)
                .transactionType(transaction.getTransactionType())
                .value(transaction.getValue())
                .quantity(transaction.getQuantity())
                .title(transaction.getTitle())
                .description(transaction.getDescription())
                .username(transaction.getAppuser().getUsername())
                .build();
    }

    public Transaction overwriteTransactionByCreateTransactionDTO(Transaction main, CreateTransactionDTO toConvert) {
       var convertedTransaction = mapCreateTransactionDTOToTransaction(toConvert, -1);

       main.setTransactionType(convertedTransaction.getTransactionType());
       main.setCategory(convertedTransaction.getCategory());
       main.setValue(convertedTransaction.getValue());
       main.setQuantity(convertedTransaction.getQuantity());

       return main;
    }

    public void validateIntArgumentsArePositive(int... values) {
        validateArgumentsArePositive(values);
    }
}
