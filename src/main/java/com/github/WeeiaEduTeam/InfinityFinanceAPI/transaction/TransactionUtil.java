package com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.AppUser;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.AppUserService;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.category.Category;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.category.CategoryService;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto.CreateTransactionDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto.TransactionDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.util.BaseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class TransactionUtil extends BaseUtil {

    private final AppUserService appUserService;
    private final CategoryService categoryService;

    public Transaction createTransactionFromCreateTransactionDTOAndUserId(CreateTransactionDTO createTransactionDTO, long userId) {
        AppUser appUser = null;

        if(isNumberPositive(userId)) {
            appUser = getAppUserById(userId);
        }

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

    private boolean isNumberPositive(long number) {
        return isPositive(number);
    }

    public TransactionDTO mapTransactionToTransactionDTO(Transaction transaction) {
        String category = transaction.getCategory().getName();

        return TransactionDTO.builder()
                .id(transaction.getId())
                .categoryName(category)
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

    public Transaction overwriteTransactionByCreateTransactionDTO(Transaction main, CreateTransactionDTO toConvert) {
       var convertedTransaction = createTransactionFromCreateTransactionDTOAndUserId(toConvert, -1);

       main.setTransactionType(convertedTransaction.getTransactionType());
       main.setCategory(convertedTransaction.getCategory());
       main.setValue(convertedTransaction.getValue());
       main.setQuantity(convertedTransaction.getQuantity());

       return main;
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

    public void validateIntArgumentsArePositive(int... values) {
        validateArgumentsArePositive(values);
    }
}
