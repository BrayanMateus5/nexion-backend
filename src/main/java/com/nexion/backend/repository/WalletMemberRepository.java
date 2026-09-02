package com.nexion.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nexion.backend.entity.WalletMember;

public interface WalletMemberRepository extends JpaRepository<WalletMember, Long> {
    List<WalletMember> findByWalletId(Long walletId);

    boolean existsByWalletIdAndUserId(Long walletId, Long userId);

    Optional<WalletMember> findByWalletIdAndUserId(Long walletId, Long userId);

}
