package com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.TransactionType;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTransactionDTO {

    private TransactionType transactionType;
    private int value;
    private int quantity;
    private String categoryName;
}
