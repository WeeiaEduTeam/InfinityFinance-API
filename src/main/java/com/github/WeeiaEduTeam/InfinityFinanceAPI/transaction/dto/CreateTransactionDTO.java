package com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.TransactionType;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import javax.validation.constraints.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTransactionDTO {

    private TransactionType transactionType;

    private int quantity;

    @Min(value = 1, message = "Value has to be higher or equal to 1")
    private int value;

    private String description;

    @NotBlank(message = "Title can not be blank")
    private String title;

    @NotBlank(message = "Title can not be blank")
    private String categoryName;
}
