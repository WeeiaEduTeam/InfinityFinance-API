package com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.AppUserService;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto.CreateTransactionDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto.TransactionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionUserService {
    private final AppUserService appUserService;

    private final TransactionAdminService transactionAdminService;

    public List<TransactionDTO> getAllTransactionsForLoggedUser() {
        long loggedInUserId = getLoggedUserId();

        return transactionAdminService.getAllTransactionsForGivenUser(loggedInUserId);
    }

    public List<TransactionDTO> getAllTransactionsForLoggedUserAndGivenCategory(long categoryId) {
        long loggedInUserId = getLoggedUserId();

        return transactionAdminService.getAllTransactionsForGivenUserAndCategory(loggedInUserId, categoryId);
    }
    public void deleteSingleTransactionForLoggedUser(long transactionId) {
        long loggedInUserId = getLoggedUserId();

        var foundTransaction = transactionAdminService.getTransactionByIdAndByAppuserId(transactionId, loggedInUserId);

        transactionAdminService.deleteTransactionWithCategory(foundTransaction);
    }

    public TransactionDTO createTransactionForLoggedUser(CreateTransactionDTO createTransactionDTO) {
        long loggedInUserId = getLoggedUserId();

        return transactionAdminService.createTransactionForGivenUser(loggedInUserId, createTransactionDTO);
    }

    public TransactionDTO replaceTransactionForLoggedUser(Long transactionId, CreateTransactionDTO createTransactionDTO) {
        long loggedInUserId = getLoggedUserId();

        return transactionAdminService.replaceTransaction(loggedInUserId, transactionId, createTransactionDTO);
    }

    private long getLoggedUserId() {
        return appUserService.getLoggedInUserId();
    }
}
