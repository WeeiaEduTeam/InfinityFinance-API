package com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByAppuserIdAndCategoryId(long userId, long categoryId, Pageable pageable);

    @Query("SELECT DISTINCT t FROM Transaction t JOIN FETCH t.category WHERE t.appuser.id = :appUserId")
    List<Transaction> findAllByAppuserId(long appUserId, Pageable pageable);

    Transaction findByIdAndAppuserId(long transactionId, long AppuserId);
}
