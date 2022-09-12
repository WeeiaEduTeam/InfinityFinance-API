package com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.AppUser;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.AppUserService;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.category.Category;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.category.CategoryService;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto.CreateTransactionDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto.TransactionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionAdminService {

    private final TransactionRepository transactionRepository;
    private final AppUserService appUserService;
    private final CategoryService categoryService;

    private final TransactionUtil transactionUtil;
    private final CustomPageable customPageable;

    public List<TransactionDTO> getAllTransactionsForGivenUserAndCategory(long userId, long categoryId, int pageNumber,
                                                                          Sort.Direction sortDirection, String sortBy) {

        Pageable page = validateAndCreatePageable(pageNumber, sortDirection, sortBy, Transaction.class);

        var foundTransactions = getTransactionsByAppuserIdAndCategoryId(userId, categoryId, page);

        return foundTransactions.stream().map(transactionUtil::mapTransactionToTransactionDTO).toList();
    }

    private <T> Pageable validateAndCreatePageable(int pageNumber, Sort.Direction sortDirection, String sortBy, Class<T> clazz) {
        return customPageable.validateAndCreatePageable(pageNumber, sortDirection, sortBy);//, clazz);
    }


    private List<Transaction> getTransactionsByAppuserIdAndCategoryId(long userId, long categoryId, Pageable page) {
        return transactionRepository.findAllByAppuserIdAndCategoryId(userId, categoryId, page);
    }

    public List<TransactionDTO> getAllTransactionsForGivenUser(long userId) {
        var foundTransactions = getTransactionsByAppuserId(userId);

        return foundTransactions.stream().map(transactionUtil::mapTransactionToTransactionDTO).toList();
    }

    private List<Transaction> getTransactionsByAppuserId(long userId) {
        return transactionRepository.findAllByAppuserId(userId);
    }

    public TransactionDTO createTransactionForGivenUser(long userId, CreateTransactionDTO createTransactionDTO) {
        validateArgumentsArePositive(createTransactionDTO.getQuantity(), createTransactionDTO.getValue());

        var transaction = createTransactionFromCreateTransactionDTOAndUserId(createTransactionDTO, userId);

        ifCategoryDoesNotExistsCreate(transaction, createTransactionDTO.getCategoryName());

        saveTransaction(transaction);

        return mapTransactionToTransactionDTO(transaction);
    }

    private Transaction createTransactionFromCreateTransactionDTOAndUserId(CreateTransactionDTO createTransactionDTO, long userId) {
        var transaction = transactionUtil.mapCreateTransactionDTOToTransaction(createTransactionDTO, userId);

        var appUser = getUserById(userId);

        var category = getCategoryByName(createTransactionDTO.getCategoryName());

        transaction.setAppuser(appUser);
        transaction.setCategory(category);

        return transaction;
    }

    private Category getCategoryByName(String categoryName) {
        return categoryService.getCategoryByName(categoryName);
    }

    private AppUser getUserById(long userId) {
        return appUserService.getUserById(userId);
    }

    private void ifCategoryDoesNotExistsCreate(Transaction transaction, String categoryName) {
        if (transaction != null && transaction.getCategory() == null) {
            var savedCategory = saveCategory(categoryName);

            transaction.setCategory(savedCategory);
        }
    }

    private Category saveCategory(String categoryName) {
        return categoryService.createCategory(categoryName);
    }

    public TransactionDTO replaceTransaction(Long userId, Long transactionId, CreateTransactionDTO createTransactionDTO) {
        validateArgumentsArePositive(createTransactionDTO.getQuantity(), createTransactionDTO.getValue());

        var foundTransaction = getTransactionByIdAndByAppuserId(transactionId, userId);

        if(foundTransaction == null)
            throw new RuntimeException("Transaction not found or is transaction id is not related with user id");

        var overwrittenTransaction = transactionUtil.overwriteTransactionByCreateTransactionDTO(foundTransaction, createTransactionDTO);

        ifCategoryDoesNotExistsCreate(overwrittenTransaction, createTransactionDTO.getCategoryName());

        saveTransaction(overwrittenTransaction);

        return mapTransactionToTransactionDTO(overwrittenTransaction);
    }

    private void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    private TransactionDTO mapTransactionToTransactionDTO(Transaction transaction) {

        return transactionUtil.mapTransactionToTransactionDTO(transaction);
    }

    private void validateArgumentsArePositive(int... values) {
        transactionUtil.validateIntArgumentsArePositive(values);
    }

    private Transaction getTransactionById(long transactionId) {
        return transactionRepository.findById(transactionId).orElseThrow(() -> new RuntimeException("Transaction not found in the database"));
    }

    public void deleteOneTransaction(long transactionId) {
        var foundTransaction = getTransactionById(transactionId);

        deleteTransactionWithCategory(foundTransaction);
    }

    void deleteTransactionWithCategory(Transaction transaction) {
        transactionRepository.delete(transaction);
        categoryService.checkAndDeleteCategoryIfNotRelated(transaction.getCategory().getId());
    }

    Transaction getTransactionByIdAndByAppuserId(long transactionId, long loggedInUserId) {
        return transactionRepository.findByIdAndAppuserId(transactionId, loggedInUserId);
    }
}
