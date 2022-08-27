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
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AppUserService appUserService;
    private TransactionUtils transactionUtils;

    public List<TransactionDTO> getAllTransactionsForGivenUserAndCategory(long userId, long categoryId) {
        return null;
        /*var foundTransactions = transactionRepository.findAllByAppuserIdAndCategoryId(userId, categoryId);

        return foundTransactions.stream()
                .map(e -> transactionUtils.mapTransactionToTransactionDTO(e))
                .toList();*/
    }

    public List<TransactionDTO> getAllTransactionsForGivenUser(long userId) {
        return null;
        /*var foundTransactions = transactionRepository.findAllByAppuserId(userId);

        return foundTransactions.stream()
                .map(e -> transactionUtils.mapTransactionToTransactionDTO(e))
                .toList();*/
    }

    public TransactionDTO createTransactionForGivenUser(long userId, CreateTransactionDTO createTransactionDTO) {
        return null;
        /*var transaction = transactionUtils.createTransactionFromCreateTransactionDTOAndUserId(createTransactionDTO, userId);

        Transaction savedTransaction = null;

        if (transaction.getAppuser() == null || transaction.getCategory() == null) {
            log.error("User not found in appUserService, called from createTransactionForGivenUser\n Create error handler");
        } else {
            log.info("Saved succesfully");
            savedTransaction = transactionRepository.save(transaction);

        }

        log.info(String.valueOf(transactionUtils));

        return transactionUtils.mapTransactionToTransactionDTO(savedTransaction);*/
    }
}
