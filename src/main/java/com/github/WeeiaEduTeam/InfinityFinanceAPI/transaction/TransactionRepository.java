package com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction;

import com.github.WeeiaEduTeam.InfinityFinanceAPI.appuser.AppUser;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByAppuserIdAndCategoryId(long userId, long categoryId, Pageable pageable);

    @Query("SELECT DISTINCT t" +
            " FROM Transaction t" +
            " LEFT JOIN FETCH t.category" +
            " LEFT JOIN FETCH t.appuser user" +
            " LEFT JOIN t.appuser.roles" +
            " WHERE user.id = :appUserId")
    List<Transaction> findAllByAppuserId(long appUserId, Pageable pageable);

    Transaction findByIdAndAppuserId(long transactionId, long AppuserId);
}
