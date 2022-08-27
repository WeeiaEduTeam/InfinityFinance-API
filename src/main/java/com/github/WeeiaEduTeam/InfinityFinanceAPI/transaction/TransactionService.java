package com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.AppUserService;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.category.Category;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.category.CategoryService;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto.CreateTransactionDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto.TransactionDTO;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryService categoryService;


    @Autowired
    private TransactionUtils transactionUtils;

    public List<TransactionDTO> getAllTransactionsForGivenUserAndCategory(long userId, long categoryId) {
        var foundTransactions = transactionRepository.findAllByAppuserIdAndCategoryId(userId, categoryId);

        return foundTransactions.stream()
                .map(TransactionUtils::mapTransactionToTransactionDTO)
                .toList();
    }

    public List<TransactionDTO> getAllTransactionsForGivenUser(long userId) {
        var foundTransactions = transactionRepository.findAllByAppuserId(userId);

        return foundTransactions.stream()
                .map(TransactionUtils::mapTransactionToTransactionDTO)
                .toList();
    }

    public TransactionDTO createTransactionForGivenUser(long userId, CreateTransactionDTO createTransactionDTO) {
        Transaction savedTransaction = null;

        var transaction = transactionUtils.createTransactionFromCreateTransactionDTOAndUserId(createTransactionDTO, userId);

        if (transaction.getAppuser() == null) {
            log.error("User not found in appUserService, called from createTransactionForGivenUser\n Create error handler");
        }

        if (transaction.getCategory() == null) {
            Category category = Category.builder()
                    .name(createTransactionDTO.getCategoryName())
                    .build();

            var savedCategoryDTO = categoryService.createCategory(category);


            transaction.setCategory(category);
        }

            log.info("Saved succesfully");
            savedTransaction = transactionRepository.save(transaction);


        log.info(String.valueOf(transactionUtils));
        log.info(String.valueOf(savedTransaction));

        return TransactionUtils.mapTransactionToTransactionDTO(savedTransaction);
    }
}
