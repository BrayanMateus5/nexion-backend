package com.nexion.backend.service;
import org.springframework.stereotype.Service;
import com.nexion.backend.entity.Wallet;
import com.nexion.backend.exception.ResourceNotFoundException;
import com.nexion.backend.repository.WalletMemberRepository;
import com.nexion.backend.repository.WalletRepository;

@Service 
public class WalletAccessService {
 
    private final WalletRepository walletRepository;
    private final WalletMemberRepository walletMemberRepository;
    private final UserLogService userLogService;

    public WalletAccessService(WalletRepository repository, WalletMemberRepository memberRepository, UserLogService userLogService) {
        this.walletRepository= repository;
        this.walletMemberRepository = memberRepository;
        this.userLogService = userLogService;
    }
public void verificarMembro(Long walletId) {
    Long userId = userLogService.get().getId();
    if (!walletMemberRepository.existsByWalletIdAndUserId(walletId, userId)) {
    throw new ResourceNotFoundException("A carteira não foi encontrada");
    }
}

public void verificarDono(Long walletId) {
Wallet wallet = walletRepository.findById(walletId).orElseThrow(() -> new ResourceNotFoundException("Carteira não encontrada"));
Long userId = userLogService.get().getId();

if (!wallet.getOwner().getId().equals(userId)) {
    throw new ResourceNotFoundException("Carteira não encontrada");

}
}
}