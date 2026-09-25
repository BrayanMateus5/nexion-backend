package com.nexion.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nexion.backend.dto.TransactionRequest;
import com.nexion.backend.dto.TransactionResponse;
import com.nexion.backend.entity.Category;
import com.nexion.backend.entity.Transaction;
import com.nexion.backend.entity.User;
import com.nexion.backend.entity.Wallet;
import com.nexion.backend.exception.ResourceNotFoundException;
import com.nexion.backend.repository.CategoryRepository;
import com.nexion.backend.repository.TransactionRepository;
import com.nexion.backend.repository.WalletMemberRepository;
import com.nexion.backend.repository.WalletRepository;

@Service
public class TransactionService {

    private final TransactionRepository repository;
    private final WalletRepository walletRepository;
    private final CategoryRepository categoryRepository;
    private final WalletMemberRepository walletMemberRepository;
    private final UserLogService userLogService;

    public TransactionService(TransactionRepository repository, WalletRepository walletRepository,
            CategoryRepository categoryRepository,
            WalletMemberRepository walletMemberRepository, UserLogService userLogService) {
        this.repository = repository;
        this.walletRepository = walletRepository;
        this.categoryRepository = categoryRepository;
        this.walletMemberRepository = walletMemberRepository;
        this.userLogService = userLogService;
    }

    public TransactionResponse criar(Long walletId, TransactionRequest request) {
        verificarMembro(walletId);
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new ResourceNotFoundException("Carteira não encontrada"));
        User createdBy = userLogService.get();

        Transaction transaction = new Transaction();
        transaction.setWallet(wallet);
        transaction.setCreatedBy(createdBy);
        transaction.setType(request.getType());
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setDate(request.getDate());
        aplicarCategoria(transaction, request.getCategoryId());

        return toResponse(repository.save(transaction));
    }

    public List<TransactionResponse> listarPorCateira(Long walletId) {
        verificarMembro(walletId);
        return repository.findByWalletId(walletId).stream().map(this::toResponse).toList();
    }

    public TransactionResponse buscarPorId(Long id) {
        Transaction transaction = buscarEntidade(id);
        verificarMembro(transaction.getWallet().getId());
        return toResponse(transaction);
    }

    public TransactionResponse atualizar(Long id, TransactionRequest request) {
        Transaction transaction = buscarEntidade(id);
        verificarMembro(transaction.getWallet().getId());
        transaction.setType(request.getType());
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setDate(request.getDate());
        aplicarCategoria(transaction, request.getCategoryId());
        return toResponse(repository.save(transaction));
    }

    public void remover(Long id) {
        Transaction transaction = buscarEntidade(id);
        verificarMembro(transaction.getWallet().getId());
        repository.delete(transaction);
    }

    private void aplicarCategoria(Transaction transaction, Long categoryId) {
        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));
            transaction.setCategory(category);
        } else {
            transaction.setCategory(null);
        }
    }

    private Transaction buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transação não encontrada"));
    }

    private TransactionResponse toResponse(Transaction t) {
        TransactionResponse response = new TransactionResponse();
        response.setId(t.getId());
        response.setWalletId(t.getWallet().getId());
        response.setCategoryId(t.getCategory() != null ? t.getCategory().getId() : null);
        response.setCreatedById(t.getCreatedBy().getId());
        response.setType(t.getType());
        response.setAmount(t.getAmount());
        response.setDescription(t.getDescription());
        response.setDate(t.getDate());
        return response;
    }

    private void verificarMembro(Long walletId) {
        Long userId = userLogService.get().getId();
        if (!walletMemberRepository.existsByWalletIdAndUserId(walletId, userId)) {
            throw new ResourceNotFoundException("A carteira não foi encontrada");

        }
    }
}
