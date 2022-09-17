package com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.TransactionType;
import lombok.*;
import lombok.experimental.FieldNameConstants;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class TransactionDTO {
    private long id;

    private TransactionType transactionType;

    private int value;
    private int quantity;

    private String title;
    private String description;
    private String categoryName;
    private String username;
}
