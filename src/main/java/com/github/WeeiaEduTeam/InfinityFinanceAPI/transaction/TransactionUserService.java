package com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.AppUserAdminService;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto.CreateTransactionDTO;
import com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction.dto.TransactionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
class TransactionUserService {
    private final AppUserAdminService appUserAdminService;

    private final TransactionAdminService transactionAdminService;

    public List<TransactionDTO> getAllTransactionsForLoggedUser(int pageNumber, Sort.Direction sortDirection, String sortBy) {
        long loggedInUserId = getLoggedUserId();

        return null;

        //return transactionAdminService.getAllTransactionsForGivenUser(loggedInUserId, pageNumber, sortDirection, sortBy);
    }

    public List<TransactionDTO> getAllTransactionsForLoggedUserAndGivenCategory(long categoryId, int pageNumber, Sort.Direction sortDirection, String sortBy) {
        long loggedInUserId = getLoggedUserId();

        return transactionAdminService.getAllTransactionsForGivenUserAndCategory(loggedInUserId, categoryId, pageNumber, sortDirection, sortBy);
    }
    public void deleteSingleTransactionForLoggedUser(long transactionId) {
        long loggedInUserId = getLoggedUserId();

        var foundTransaction = transactionAdminService.getTransactionByIdAndByAppuserId(transactionId, loggedInUserId);

        transactionAdminService.deleteTransactionWithCategory(foundTransaction);
    }

    public TransactionDTO createTransactionForLoggedUser(CreateTransactionDTO createTransactionDTO) {
        long loggedInUserId = getLoggedUserId();

        return null;//return transactionAdminService.createTransactionForGivenUser(loggedInUserId, createTransactionDTO);
    }

    public TransactionDTO replaceTransactionForLoggedUser(Long transactionId, CreateTransactionDTO createTransactionDTO) {
        long loggedInUserId = getLoggedUserId();

        return transactionAdminService.replaceTransaction(loggedInUserId, transactionId, createTransactionDTO);
    }

    private long getLoggedUserId() {
        return appUserAdminService.getLoggedInUserId();
    }
}
