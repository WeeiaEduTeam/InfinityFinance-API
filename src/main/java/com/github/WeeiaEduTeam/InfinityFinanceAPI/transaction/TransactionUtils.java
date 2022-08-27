package com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.AppUser;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.AppUserService;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.category.Category;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.category.CategoryService;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto.CreateTransactionDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto.TransactionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class TransactionUtils {

    private final AppUserService appUserService;
    private final CategoryService categoryService;

    public static TransactionDTO mapTransactionToTransactionDTO(Transaction transaction) {
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
        var foundCategory = categoryService.getCategoryByName(categoryName);

        if(foundCategory.isPresent())
            return foundCategory.get();

        return null;
    }

    private AppUser getAppUserByUserName(String userName) {
        var foundUser = appUserService.getUserByUserName(userName);

        if(foundUser.isPresent())
            return foundUser.get();

        return null;
    }

    private AppUser getAppUserById(long userId) {
        var foundUser = appUserService.getUserById(userId);

        if(foundUser.isPresent())
            return foundUser.get();

        return null;
    }

    public Transaction createTransactionFromCreateTransactionDTOAndUserId(CreateTransactionDTO createTransactionDTO, long userId) {
        var appUser = getAppUserById(userId);
        var category = getCategoryByName(createTransactionDTO.getCategoryName());



        return Transaction.builder()
                .quantity(createTransactionDTO.getQuantity())
                .value(createTransactionDTO.getValue())
                .transactionType(createTransactionDTO.getTransactionType())
                .appuser(appUser)
                .category(category)
                .build();
    }
}
