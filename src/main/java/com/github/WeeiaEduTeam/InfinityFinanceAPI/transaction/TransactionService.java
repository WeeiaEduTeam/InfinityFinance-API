package com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.category.Category;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.category.CategoryService;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto.CreateTransactionDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto.TransactionDTO;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.github.WeeiaEduTeam.InfinityFinanceAPI.util.Util.isPositive;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryService categoryService;

    private final TransactionUtil transactionUtil;

    public List<TransactionDTO> getAllTransactionsForGivenUserAndCategory(long userId, long categoryId) {
        var foundTransactions = transactionRepository.findAllByAppuserIdAndCategoryId(userId, categoryId);

        return foundTransactions.stream()
                .map(TransactionUtil::mapTransactionToTransactionDTO)
                .toList();
    }

    public List<TransactionDTO> getAllTransactionsForGivenUser(long userId) {
        var foundTransactions = transactionRepository.findAllByAppuserId(userId);

        return foundTransactions.stream()
                .map(TransactionUtil::mapTransactionToTransactionDTO)
                .toList();
    }

    public TransactionDTO createTransactionForGivenUser(long userId, CreateTransactionDTO createTransactionDTO) {
        if(!isPositive(createTransactionDTO.getQuantity()) || !isPositive(createTransactionDTO.getValue())) {
            log.error("Quantity or value is not positive, called from createTransactionForGivenUser\n Create error handler");
        }

        Transaction savedTransaction = null;

        var transaction = transactionUtil.createTransactionFromCreateTransactionDTOAndUserId(createTransactionDTO, userId);

        if (transaction.getAppuser() == null) {
            log.error("User not found in appUserService, called from createTransactionForGivenUser\n Create error handler");
        }

        if (transaction.getCategory() == null) {
            var savedCategory = saveCategory(createTransactionDTO.getCategoryName());

            transaction.setCategory(savedCategory);
        }

        log.info("Saved succesfully");
        savedTransaction = transactionRepository.save(transaction);

        log.info(String.valueOf(transactionUtil));
        log.info(String.valueOf(savedTransaction));

        return TransactionUtil.mapTransactionToTransactionDTO(savedTransaction);
    }

    private Category saveCategory(String categoryName) {
        Category category = Category.builder()
                .name(categoryName)
                .build();

        return categoryService.createCategory(category);
    }
}
