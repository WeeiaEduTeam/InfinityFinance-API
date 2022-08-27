package com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.AppUser;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.AppUserService;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.category.Category;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.category.CategoryService;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto.CreateTransactionDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto.TransactionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TransactionUtils {

    private final AppUserService appUserService;
    private final CategoryService categoryService;

    public TransactionDTO mapTransactionToTransactionDTO(Transaction transaction) {
        return TransactionDTO.builder()
                .categoryName(transaction.getCategory().getName())
                .transactionType(transaction.getTransactionType())
                .value(transaction.getValue())
                .quantity(transaction.getQuantity())
                .userName(transaction.getAppuser().getUsername())
                .build();
    }

    public Transaction mapTransactionDTOToTransaction(TransactionDTO transactionDTO) {
        return Transaction.builder()
                .quantity(transactionDTO.getQuantity())
                .value(transactionDTO.getValue())
                .appuser(getAppUserByUserName(transactionDTO.getUserName()))
                .category(getCategoryByName(transactionDTO.getCategoryName()))
                .build();
    }

    private Category getCategoryByName(String categoryName) {
        return categoryService.getCategoryByName(categoryName);
    }

    private AppUser getAppUserByUserName(String userName) {
        var foundUser = appUserService.getUserByUserName(userName);

        return foundUser.orElse(null);
    }

    private AppUser getAppUserById(long userId) {
        var foundUser = appUserService.getUserById(userId);

        return foundUser.orElse(null);
    }

    public Transaction createTransactionFromCreateTransactionDTOAndUserId(CreateTransactionDTO createTransactionDTO, long userId) {
        return Transaction.builder()
                .quantity(createTransactionDTO.getQuantity())
                .value(createTransactionDTO.getValue())
                .appuser(getAppUserById(userId))
                .category(getCategoryByName(createTransactionDTO.getCategoryName()))
                .build();
    }
}
