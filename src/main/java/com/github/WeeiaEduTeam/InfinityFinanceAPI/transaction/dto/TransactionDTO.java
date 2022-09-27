package com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.TransactionType;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class TransactionDTO {
    private long id;

    private TransactionType transactionType;

    @Min(value = 1, message = "Movies are mainly more than a minute")
    @Max(value = 300, message = "Movies are less than 5 hours")
    private int value;
    private int quantity;

    private String title;
    private String description;
    private String categoryName;
    private String username;
}
