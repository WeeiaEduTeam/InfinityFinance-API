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
public class TransactionUtil {

    private final AppUserService appUserService;
    private final CategoryService categoryService;

    public Transaction createTransactionFromCreateTransactionDTOAndUserId(CreateTransactionDTO createTransactionDTO, long userId) {
        var appUser = getAppUserById(userId);
        var category = getCategoryByName(createTransactionDTO.getCategoryName());

        return Transaction.builder()
                .transactionType(createTransactionDTO.getTransactionType())
                .value(createTransactionDTO.getValue())
                .quantity(createTransactionDTO.getQuantity())
                .title(createTransactionDTO.getTitle())
                .description(createTransactionDTO.getDescription())
                .appuser(appUser)
                .category(category)
                .build();
    }

    public static TransactionDTO mapTransactionToTransactionDTO(Transaction transaction) {
        return TransactionDTO.builder()
                .categoryName(transaction.getCategory().getName())
                .transactionType(transaction.getTransactionType())
                .value(transaction.getValue())
                .quantity(transaction.getQuantity())
                .title(transaction.getTitle())
                .description(transaction.getDescription())
                .userName(transaction.getAppuser().getUsername())
                .build();
    }

    public Transaction mapTransactionDTOToTransaction(TransactionDTO transactionDTO) {
        return Transaction.builder()
                .value(transactionDTO.getValue())
                .quantity(transactionDTO.getQuantity())
                .title(transactionDTO.getTitle())
                .description(transactionDTO.getDescription())
                .appuser(getAppUserByUserName(transactionDTO.getUserName()))
                .category(getCategoryByName(transactionDTO.getCategoryName()))
                .build();
    }

    private Category getCategoryByName(String categoryName) {
        var foundCategory = categoryService.getCategoryByName(categoryName);

        return foundCategory.orElse(null);
    }

    private AppUser getAppUserByUserName(String userName) {

        return appUserService.getUserByUserName(userName);
    }

    private AppUser getAppUserById(long userId) {
        var foundUser = appUserService.getUserById(userId);

        return foundUser.orElse(null);
    }
}
