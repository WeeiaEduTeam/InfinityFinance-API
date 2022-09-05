package com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.AppUserService;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.category.Category;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.category.CategoryService;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto.CreateTransactionDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto.TransactionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AppUserService appUserService;
    private final CategoryService categoryService;

    private final TransactionUtil transactionUtil;

    public List<TransactionDTO> getAllTransactionsForGivenUserAndCategory(long userId, long categoryId) {
        var foundTransactions = transactionRepository.findAllByAppuserIdAndCategoryId(userId, categoryId);

        return foundTransactions.stream()
                .map(transactionUtil::mapTransactionToTransactionDTO)
                .toList();
    }

    public List<TransactionDTO> getAllTransactionsForGivenUser(long userId) {
        var foundTransactions = transactionRepository.findAllByAppuserId(userId);

        return foundTransactions.stream()
                .map(transactionUtil::mapTransactionToTransactionDTO)
                .toList();
    }

    public TransactionDTO createTransactionForGivenUser(long userId, CreateTransactionDTO createTransactionDTO) {
        validateArgumentsArePositive(createTransactionDTO.getQuantity(), createTransactionDTO.getValue());

        var transaction = transactionUtil.createTransactionFromCreateTransactionDTOAndUserId(createTransactionDTO, userId);

        if (transaction.getAppuser() == null) {
            log.error("User not found in appUserService, called from createTransactionForGivenUser\n Create error handler");
        }

        if (transaction.getCategory() == null) {
            var savedCategory = saveCategory(createTransactionDTO.getCategoryName());

            transaction.setCategory(savedCategory);
        }

        Transaction savedTransaction = transactionRepository.save(transaction);

        return transactionUtil.mapTransactionToTransactionDTO(savedTransaction);
    }

    private Category saveCategory(String categoryName) {
        Category category = Category.builder()
                .name(categoryName)
                .build();

        return categoryService.createCategory(category);
    }

    public TransactionDTO replaceTransaction(Long userId, Long transactionId, CreateTransactionDTO createTransactionDTO) {
        validateArgumentsArePositive(createTransactionDTO.getQuantity(), createTransactionDTO.getValue());

        var foundTransaction = transactionRepository.findByIdAndAppuserId(transactionId, userId);

        if (foundTransaction == null)
            throw new RuntimeException("Transaction with given id doesn't exists\n Create error handler");

        var overwrittenTransaction = transactionUtil.overwriteTransactionByCreateTransactionDTO(foundTransaction, createTransactionDTO);

        if (overwrittenTransaction.getCategory() == null) {
            var savedCategory = saveCategory(createTransactionDTO.getCategoryName());

            overwrittenTransaction.setCategory(savedCategory);
        }

        transactionRepository.save(overwrittenTransaction);

        return transactionUtil.mapTransactionToTransactionDTO(overwrittenTransaction);
    }

    private void validateArgumentsArePositive(int... values) {
        transactionUtil.validateIntArgumentsArePositive(values);
    }

    private Transaction getTransactionById(long transactionId) {
        return transactionRepository.findById(transactionId).orElseThrow(() -> new RuntimeException("Transaction not found in the database"));
    }

    public List<TransactionDTO> getAllTransactionsForLoggedUser() {
        long loggedInUserId = appUserService.getLoggedInUserId();

        return getAllTransactionsForGivenUser(loggedInUserId);
    }

    public List<TransactionDTO> getAllTransactionsForLoggedUserAndGivenCategory(long categoryId) {
        long loggedInUserId = getLoggedUserId();

        return getAllTransactionsForGivenUserAndCategory(loggedInUserId, categoryId);
    }

    private long getLoggedUserId() {
        return appUserService.getLoggedInUserId();
    }

    public void deleteOneTransaction(long transactionId) {
        var foundTransaction = getTransactionById(transactionId);

        deleteTransactionWithCategory(foundTransaction);
    }

    private void deleteTransactionWithCategory(Transaction transaction) {
        transactionRepository.delete(transaction);
        categoryService.deleteCategoryIfNotRelated(transaction.getCategory().getId());
    }

    public void deleteSingleTransactionForLoggedUser(long transactionId) {
        long loggedInUserId = getLoggedUserId();

        var foundTransaction = getTransactionById(transactionId);

        if(Long.compare(foundTransaction.getAppuser().getId(), loggedInUserId) != 0) {
            throw new RuntimeException("Transaction id is not related with given logged user id");
        }

        deleteTransactionWithCategory(foundTransaction);
    }
}
