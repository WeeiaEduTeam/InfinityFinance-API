package com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.AppUser;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.AppUserAdminService;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.category.Category;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.category.CategoryService;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto.CreateTransactionDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto.TransactionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@Slf4j
public class TransactionAdminService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TransactionUtil transactionUtil;
    @Autowired
    private CustomPageable customPageable;

    @Autowired
    private AppUserAdminService appUserAdminService;

    /*
    *   Circular dependency between appUserService
    *   and transactionAdminService is avoided
    *   by lazy loading beans using @PostConstruct
    *   annotion and getters & setters.
    */

    @PostConstruct
    public void init() {
        appUserAdminService.setTransactionAdminService(this);
    }

    public AppUserAdminService getAppUserService() {
        return appUserAdminService;
    }

    public List<TransactionDTO> getAllTransactionsForGivenUserAndCategory(long userId, long categoryId, int pageNumber,
                                                                          Sort.Direction sortDirection, String sortBy) {

        Pageable page = validateAndCreatePageable(pageNumber, sortDirection, sortBy, Transaction.class);

        var foundTransactions = getTransactionsByAppuserIdAndCategoryId(userId, categoryId, page);

        return foundTransactions.stream().map(transactionUtil::mapToTransactionDTO).toList();
    }

    private <T> Pageable validateAndCreatePageable(int pageNumber, Sort.Direction sortDirection, String sortBy, Class<T> clazz) {
        return customPageable.validateAndCreatePageable(pageNumber, sortDirection, sortBy, clazz);
    }


    private List<Transaction> getTransactionsByAppuserIdAndCategoryId(long userId, long categoryId, Pageable page) {
        return transactionRepository.findAllByAppuserIdAndCategoryId(userId, categoryId, page);
    }

    public List<TransactionDTO> getAllTransactionsForGivenUser(long userId, int pageNumber,
                                                               Sort.Direction sortDirection, String sortBy) {
        Pageable page = validateAndCreatePageable(pageNumber, sortDirection, sortBy, Transaction.class);

        var foundTransactions = getTransactionsByAppuserId(userId, page);

        return foundTransactions.stream().map(transactionUtil::mapToTransactionDTO).toList();
    }

    private List<Transaction> getTransactionsByAppuserId(long userId, Pageable page) {
        return transactionRepository.findAllByAppuserId(userId, page);
    }

    public TransactionDTO createTransactionForGivenUser(long userId, CreateTransactionDTO createTransactionDTO) {
        validateArgumentsArePositive(createTransactionDTO.getQuantity(), createTransactionDTO.getValue());

        var transaction = createTransactionFromCreateTransactionDTOAndUserId(createTransactionDTO, userId);

        ifCategoryDoesNotExistsCreate(transaction, createTransactionDTO.getCategoryName());

        saveTransaction(transaction);

        return mapTransactionToTransactionDTO(transaction);
    }

    private Transaction createTransactionFromCreateTransactionDTOAndUserId(CreateTransactionDTO createTransactionDTO, long userId) {
        var transaction = transactionUtil.mapToTransaction(createTransactionDTO);

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
        return getAppUserService().getUserById(userId);
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

        return transactionUtil.mapToTransactionDTO(transaction);
    }

    private void validateArgumentsArePositive(int... values) {
        transactionUtil.validateArgumentsArePositive(values);
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
        categoryService.checkAndDeleteCategoryIfNotRelated(transaction.getCategory().getId()); // N + 1
    }

    Transaction getTransactionByIdAndByAppuserId(long transactionId, long loggedInUserId) {
        return transactionRepository.findByIdAndAppuserId(transactionId, loggedInUserId);
    }

    public void deleteTransactionsRelatedWithUser(long userId) {
        var transactions = getTransactionsByAppuserId(userId, null);

        transactions
                .forEach(this::deleteTransactionWithCategory);
    }
}
