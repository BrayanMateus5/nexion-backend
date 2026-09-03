package com.nexion.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nexion.backend.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByWalletId(Long walletId);

}
