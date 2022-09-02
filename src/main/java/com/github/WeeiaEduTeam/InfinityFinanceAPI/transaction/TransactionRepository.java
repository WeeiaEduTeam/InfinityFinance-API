package com.github.WeeiaEduTeam.InfinityFinanceAPI.transaction;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByAppuserIdAndCategoryId(long userId, long categoryId);
    List<Transaction> findAllByAppuserId(long appUserId);
}
