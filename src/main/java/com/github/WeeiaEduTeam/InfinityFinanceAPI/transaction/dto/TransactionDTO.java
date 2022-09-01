package com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.AppUser;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.category.Category;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.TransactionType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    long id;

    private TransactionType transactionType;

    private int value;
    private int quantity;

    private String title;
    private String description;
    private String categoryName;
    private String userName;
}
