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
        var foundTransactions = getTransactionsByAppuserIdAndCategoryId(userId, categoryId);

        return foundTransactions.stream()
                .map(transactionUtil::mapTransactionToTransactionDTO)
                .toList();
    }

    private List<Transaction> getTransactionsByAppuserIdAndCategoryId(long userId, long categoryId) {
        return transactionRepository.findAllByAppuserIdAndCategoryId(userId, categoryId);
    }

    public List<TransactionDTO> getAllTransactionsForGivenUser(long userId) {
        var foundTransactions = getTransactionsByAppuserId(userId);

        return foundTransactions.stream()
                .map(transactionUtil::mapTransactionToTransactionDTO)
                .toList();
    }

    private List<Transaction> getTransactionsByAppuserId(long userId) {
        return transactionRepository.findAllByAppuserId(userId);
    }

    public TransactionDTO createTransactionForGivenUser(long userId, CreateTransactionDTO createTransactionDTO) {
        validateArgumentsArePositive(createTransactionDTO.getQuantity(), createTransactionDTO.getValue());

        var transaction = transactionUtil.createTransactionFromCreateTransactionDTOAndUserId(createTransactionDTO, userId);

        if (transaction.getAppuser() == null) {
            throw new UsernameNotFoundException("User not found during mapping");
        }

        if (transaction.getCategory() == null) {
            var savedCategory = saveCategory(createTransactionDTO.getCategoryName());

            transaction.setCategory(savedCategory);
        }

        Transaction savedTransaction = transactionRepository.save(transaction);

        return transactionUtil.mapTransactionToTransactionDTO(savedTransaction);
    }

    private Category saveCategory(String categoryName) {
        Category category = new Category(categoryName);

        return categoryService.createCategory(category);
    }

    public TransactionDTO replaceTransaction(Long userId, Long transactionId, CreateTransactionDTO createTransactionDTO) {
        validateArgumentsArePositive(createTransactionDTO.getQuantity(), createTransactionDTO.getValue());

        var foundTransaction = getTransactionByIdAndByAppuserId(transactionId, userId);

        if (foundTransaction == null) {
            throw new RuntimeException("Transaction with given id doesn't exists\n Create error handler");
        }

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

        var foundTransaction = getTransactionByIdAndByAppuserId(transactionId, loggedInUserId);

        deleteTransactionWithCategory(foundTransaction);
    }

    private Transaction getTransactionByIdAndByAppuserId(long transactionId, long loggedInUserId) {
        return transactionRepository.findByIdAndAppuserId(transactionId, loggedInUserId);
    }

    public TransactionDTO createTransactionForLoggedUser(CreateTransactionDTO createTransactionDTO) {
        long loggedInUserId = getLoggedUserId();

        return createTransactionForGivenUser(loggedInUserId, createTransactionDTO);
    }

    public TransactionDTO replaceTransactionForLoggedUser(Long transactionId, CreateTransactionDTO createTransactionDTO) {
        long loggedInUserId = getLoggedUserId();

        return replaceTransaction(loggedInUserId, transactionId, createTransactionDTO);
    }
}
