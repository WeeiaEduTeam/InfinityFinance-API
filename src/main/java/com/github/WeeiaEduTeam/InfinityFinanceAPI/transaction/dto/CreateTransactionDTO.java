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

    @Min(45)
    private int value;
    @Min(value = 1, message = "Movies are mainly more than a minute")
    @Max(value = 300, message = "Movies are less than 5 hours")
    private int quantity;

    @NotEmpty
    @Email
    private String title;

    @Pattern(regexp = "^[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}$")
    private String description;
    @Email
    private String categoryName;
}
