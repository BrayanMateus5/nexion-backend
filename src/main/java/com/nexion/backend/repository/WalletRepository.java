package com.nexion.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nexion.backend.entity.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

}
