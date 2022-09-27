package com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.TransactionType;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class TransactionDTO {
    private long id;

    private TransactionType transactionType;

    private int quantity;

    @Min(value = 1, message = "Value has to be higher or equal to 1")
    private int value;

    private String description;

    @NotBlank(message = "Title can not be blank")
    private String title;
    
    @NotBlank(message = "Title can not be blank")
    private String categoryName;

    @NotBlank(message = "Title can not be blank")
    private String username;
}
